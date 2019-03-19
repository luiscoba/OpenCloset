package opencloset.ec.com.opencloset.activity.subir;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.RentasSendToServer;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToServer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.subir.fotos.Agrega_fotos;
import opencloset.ec.com.opencloset.activity.subir.mapa.MapaDireccion;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.DateNumberFormatDetector;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class Publicar extends AppCompatActivity {

    private VentasSendToServer venta;
    private RentasSendToServer renta;
    private MensajeSistema mensajeSistema;

    private List <String> lstNombreFoto;
    private Map <String, Integer> mapProvincia;

    private EditText precio, stock, fechaExpiracion;
    private Calendar c, myCalendar;
    private TextView direccion, numFotos;

    private String direccion_opcional, provincia, strPrecio, strCantidad;
    private Double latitud, longitud;

    private SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mensajeSistema = new MensajeSistema(Publicar.this);

        Globales.numFotos = 0;
        Globales.direccion = getResources().getString(R.string.esperandoPorDireccion);

        lstNombreFoto = new ArrayList <>();
        mapProvincia = new HashMap <>();

        precio = (EditText) findViewById(R.id.etxtPrecio);
        stock = (EditText) findViewById(R.id.etxtStock);
        numFotos = findViewById(R.id.txvNumFotos);
        direccion = findViewById(R.id.txvDireccion);

        myCalendar = Calendar.getInstance();
        c = Calendar.getInstance();

        fechaExpiracion = (EditText) findViewById(R.id.etxtFechaExpira);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        fechaExpiracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Publicar.this
                        , date
                        , myCalendar.get(Calendar.YEAR)
                        , myCalendar.get(Calendar.MONTH)
                        , myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                c.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH) + 30);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        renta = new RentasSendToServer();
        venta = new VentasSendToServer();

        Bundle bundle;
        if (Globales.idTipoTransaccion == Globales.tipo_venta) {
            bundle = getIntent().getExtras();
            venta = (VentasSendToServer) bundle.get("venta");
            mapProvincia = (Map) bundle.get("provincias");
        }
        if (Globales.idTipoTransaccion == Globales.tipo_renta) {
            bundle = getIntent().getExtras();
            renta = (RentasSendToServer) bundle.get("renta");
            mapProvincia = (Map) bundle.get("provincias");
        }

    }

    private void updateLabel() {
        //se usa DateNumberFormatDetector que detecta el idioma del dispositivo
        fechaExpiracion.setText(DateNumberFormatDetector.getSimpleDateFormat().format(myCalendar.getTime()));
        venta.setFecha_fin_venta(formato.format(myCalendar.getTime()));
    }

    public void onClickAgregarFotos(View view) {
        Intent intent = new Intent(Publicar.this, Agrega_fotos.class);
        intent.putExtra("lstNombreFoto", (Serializable) lstNombreFoto);
        startActivityForResult(intent, 1);
    }

    public void onClickDireccionDeEntrega(View view) {
        Intent intent = new Intent(Publicar.this, MapaDireccion.class);
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        intent.putExtra("provincia", provincia);
        intent.putExtra("direccion_opcional", direccion_opcional);
        startActivityForResult(intent, 2);
    }

    public void onClickPublicar(View view) {

        if (validaInformacion()) {
            if (!validaFotos())
                mensajeSistema.mostrarMensajeToastActivity("Elija fotos");
            else if (!validaDireccion())
                mensajeSistema.mostrarMensajeToastActivity("Elija una Dirección");
            else {

                if (Globales.idTipoTransaccion == Globales.tipo_venta) {
                    venta.setDireccion_prod_opcional(direccion_opcional);
                    venta.setLstNombreFoto(lstNombreFoto);
                    venta.setDireccion_producto(Globales.direccion);
                    strPrecio = precio.getText().toString();
                    venta.setPrecio_producto(Double.parseDouble(strPrecio));
                    strCantidad = stock.getText().toString();
                    venta.setCantidad_vendida(Integer.parseInt(strCantidad));

/*
                    System.out.println("venta.getFecha_fin_venta() " + venta.getFecha_fin_venta());
                    System.out.println("Publicar venta" + venta);
*/
                    Intent irARevisar = new Intent(this, Revisar.class);
                    irARevisar.putExtra("venta", venta);
                    startActivityForResult(irARevisar, 3);
                }

                if (Globales.idTipoTransaccion == Globales.tipo_renta) {
                    renta.setDireccion_prod_opcional(direccion_opcional);
                    renta.setLstNombreFoto(lstNombreFoto);
                    renta.setDireccion_producto(Globales.direccion);
                    strPrecio = precio.getText().toString();
                    renta.setPrecio_producto(Double.parseDouble(strPrecio));
                    strCantidad = stock.getText().toString();
                    renta.setCantidad_arrendada(Integer.parseInt(strCantidad));

                    Intent irARevisar = new Intent(this, Revisar.class);
                    irARevisar.putExtra("renta", renta);
                    startActivityForResult(irARevisar, 3);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Publicar resultCode " + resultCode);
        switch (requestCode) {
            case (1): {
                if (resultCode == Activity.RESULT_OK) {
                    lstNombreFoto = data.getStringArrayListExtra("lstNombreFoto");
                }
                break;
            }
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    latitud = data.getDoubleExtra("latitud", 0.0);
                    longitud = data.getDoubleExtra("longitud", 0.0);
                    provincia = data.getStringExtra("provincia");
                    direccion_opcional = data.getStringExtra("direccion_opcional");

                    if (Globales.idTipoTransaccion == Globales.tipo_venta) {
                        venta.setLatitud_producto(latitud);
                        venta.setLongitud_producto(longitud);

                        if (!mapProvincia.containsKey(provincia)) {
                            venta.setNuevaProvincia(provincia);
                        } else {
                            venta.setIdProvincia(mapProvincia.get(provincia));
                            venta.setNuevaProvincia(null);
                        }
                    }
                    if (Globales.idTipoTransaccion == Globales.tipo_renta) {
                        renta.setLatitud_producto(latitud);
                        renta.setLongitud_producto(longitud);

                        if (!mapProvincia.containsKey(provincia)) {
                            renta.setNuevaProvincia(provincia);
                        } else {
                            renta.setIdProvincia(mapProvincia.get(provincia));
                            renta.setNuevaProvincia(null);
                        }
                    }
                }
                break;
            }
            case (3): {
                if (resultCode == 0) {
                    setResult(0);
                    finish();
                }
            }
        }
    }

    private boolean validaInformacion() {
        boolean validaInformacion = true;
        precio.setError(null);
        stock.setError(null);
        fechaExpiracion.setError(null);
        if (fechaExpiracion.getText().toString().trim().isEmpty()) {
            fechaExpiracion.setError(getString(R.string.errorFechaExpiraInvalido));
            fechaExpiracion.requestFocus();
            validaInformacion = false;
        }
        // si el EditText precio y stock esta en blanco
        if (precio.getText().toString().trim().isEmpty()) {
            precio.setError(getString(R.string.errorPrecioInvalido));
            precio.requestFocus();
            validaInformacion = false;
        }
        if (Integer.parseInt(precio.getText().toString()) == 0) {
            precio.setError(getString(R.string.errorPrecioIgualAcero));
            precio.requestFocus();
            validaInformacion = false;
        }
        if (stock.getText().toString().trim().isEmpty()) {
            stock.setError(getString(R.string.errorStockInvalido));
            stock.requestFocus();
            validaInformacion = false;
        }
        if (Integer.parseInt(stock.getText().toString()) == 0) {
            stock.setError(getString(R.string.errorStockIgualAcero));
            stock.requestFocus();
            validaInformacion = false;
        }
        return validaInformacion;
    }

    private boolean validaFotos() {
        boolean validaFotos = true;
        if (numFotos.getText().equals(getResources().getString(R.string.esperandoPorFotos))) {
            validaFotos = false;
        }
        return validaFotos;
    }

    private boolean validaDireccion() {
        boolean validaDireccion = true;
        if (direccion.getText().equals(getResources().getString(R.string.esperandoPorDireccion))) {
            validaDireccion = false;
        }
        return validaDireccion;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Globales.numFotos == 0)
            numFotos.setText(R.string.esperandoPorFotos);
        else {
            venta.setLstNombreFoto(lstNombreFoto);
            numFotos.setText("Se cargó " + Globales.numFotos + " fotos");
        }
        if (Globales.direccion.equals(getResources().getString(R.string.esperandoPorDireccion)))
            direccion.setText(R.string.esperandoPorDireccion);
        else
            direccion.setText(Globales.direccion);
    }

    @Override
    public void onBackPressed() {
        setResult(10);// para que no se vaya directo al mainPrincipal
        super.onBackPressed();//aqui ya regresa al anterior activity
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//aca debes colocar el metodo que deseas que retorne
        return true;
    }

}
