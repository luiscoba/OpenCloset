package opencloset.ec.com.opencloset.activity.subir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.BeanItemsProducto;
import com.ec.deployits.ventaDeRopa.bean.BeanTalla;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToServer;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToServer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.fragment.dialogs.DeseaIniciarSesion;
import opencloset.ec.com.opencloset.fragment.dialogs.dlgFragmentLista.ListasEnDetallesPublicacion;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class Detalles_publicacion extends AppCompatActivity implements ListasEnDetallesPublicacion.ISelectedData {

    private Conexion conexion;
    private VentasSendToServer venta;
    private RentasSendToServer renta;
    private MensajeSistema mensajeSistema;

    private BeanItemsProducto itemProducto;
    private Map <String, Integer> mapTalla, mapOcasion;

    private Button btnOcasion, btnTalla;
    private EditText titulo, detalle, color;
    private Spinner spnCondicion, spnCategoria, spnMarca;

    private String TITLE_KEY = "title";
    private String DATO_KEY_MAP = "mapDeDatos";
,
    private SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onSelectedData(Map <String, Integer> mapResultado, int R_id_Button, String title_key) {

        String acumula = " ";
        Button btn = findViewById(R_id_Button);
        if (mapResultado.size() > 0) {
            for (Map.Entry <String, Integer> entry : mapResultado.entrySet()) {
                acumula = acumula + " - " + entry.getKey();
            }
            acumula = acumula + " - ";

            btn.setText(acumula);

            if (btn == btnOcasion) {
                mapOcasion = mapResultado;
            } else if (btn == btnTalla) {
                mapTalla = mapResultado;
            }
        } else {
            btn.setText(title_key);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);

        mensajeSistema = new MensajeSistema(Detalles_publicacion.this);

        mapTalla = new HashMap <String, Integer>();
        mapOcasion = new HashMap <String, Integer>();

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        venta = new VentasSendToServer();
        renta = new RentasSendToServer();

        String mOpcion = getIntent().getExtras().getString("mOpcion");
        TextView transaccion = findViewById(R.id.txtTransaccion);
        transaccion.setText(mOpcion.toUpperCase());

        conexion = new Conexion();
        try {
            itemProducto = conexion.obtenerItemsDelProducto();
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        titulo = findViewById(R.id.etxtTitulo);
        detalle = findViewById(R.id.etxtDetalle);
        color = findViewById(R.id.etxtColor);

        btnOcasion = findViewById(R.id.btnOcasion);
        btnOcasion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("btn", R.id.btnOcasion);
                args.putString(TITLE_KEY, "Elige la ocasión");//este valor se pondrá en caso de no elegir nada
                args.putSerializable(DATO_KEY_MAP, (Serializable) itemProducto.getOcasionMap());

                DialogFragment preguntarOcasion = new ListasEnDetallesPublicacion();
                preguntarOcasion.setArguments(args);
                preguntarOcasion.show(Detalles_publicacion.this.getSupportFragmentManager(), "preguntarOcasion");
            }
        });

        btnTalla = findViewById(R.id.btnTalla);
        btnTalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeanTalla talla = new BeanTalla();
                Bundle args = new Bundle();
                args.putInt("btn", R.id.btnTalla);
                args.putString(TITLE_KEY, "Elige la talla");//este valor se pondrá en caso de no elegir nada
                args.putSerializable(DATO_KEY_MAP, (Serializable) itemProducto.getTallaMap());

                DialogFragment preguntarOcasion = new ListasEnDetallesPublicacion();
                preguntarOcasion.setArguments(args);
                preguntarOcasion.show(Detalles_publicacion.this.getSupportFragmentManager(), "preguntarOcasion");
            }
        });

        spnCondicion = (Spinner) findViewById(R.id.spnCondicion);
        String[] arraySpinnerCond = new String[]{Globales.condicion_nuevo, Globales.condicion_usado};
        ArrayAdapter <String> adapterCondicion = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, arraySpinnerCond);
        adapterCondicion.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnCondicion.setAdapter(adapterCondicion);

        spnCategoria = (Spinner) findViewById(R.id.spnCategoria);
        String[] spinnerArray = itemProducto.getCategoriaMap().keySet().toArray(new String[itemProducto.getCategoriaMap().size()]);
        ArrayAdapter <String> adapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(adapter);

        spnMarca = (Spinner) findViewById(R.id.spnMarca);
        spinnerArray = itemProducto.getMarcaMap().keySet().toArray(new String[itemProducto.getMarcaMap().size()]);
        ArrayAdapter <String> adapterMarca = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnMarca.setAdapter(adapterMarca);
    }

    private boolean validaInformacion() {
        boolean validaInformacion = true;
        // Reset errors.
        color.setError(null);
        titulo.setError(null);
        detalle.setError(null);
        // si el EditText Titulo esta en blanco
        if (titulo.getText().toString().trim().isEmpty()) {
            titulo.setError(getString(R.string.errorTituloInvalido));
            titulo.requestFocus();
            validaInformacion = false;
        }
        if (detalle.getText().toString().trim().isEmpty()) {
            detalle.setError(getString(R.string.errorDescripcionInvalido));
            detalle.requestFocus();
            validaInformacion = false;
        }
        if (color.getText().toString().trim().isEmpty()) {
            color.setError(getString(R.string.errorColorInvalido));
            color.requestFocus();
            validaInformacion = false;
        }
        return validaInformacion;
    }

    private boolean validaTalla() {
        boolean validaTalla = true;

        if (btnTalla.getText().equals("Elige la Talla"))
            validaTalla = false;

        return validaTalla;
    }

    private boolean validaOcasion() {
        boolean validaOcasion = true;

        if (btnOcasion.getText().equals("Elige la Ocasión"))
            validaOcasion = false;

        return validaOcasion;
    }

    public void onSiguiente(View view) {
        System.out.println("Globales.beanUsuario" + Globales.beanUsuario);
        if (Globales.beanUsuario.getCorreo() == null) {//si su correo es null
            DialogFragment deseaIniciarSesionDialogFragment = new DeseaIniciarSesion();
            deseaIniciarSesionDialogFragment.setCancelable(false);
            deseaIniciarSesionDialogFragment.show(getSupportFragmentManager(), "dialogoParaPreguntarSiIniciaSesion");
        } else {

            if (validaInformacion()) {
                if (!validaTalla()) {
                    mensajeSistema.mostrarMensajeToastActivity("Elija una talla");
                } else {
                    if (!validaOcasion())
                        mensajeSistema.mostrarMensajeToastActivity("Elija la ocasión");
                    else {

                        if (Globales.idTipoTransaccion == Globales.tipo_venta) {

                            venta.setNombre_producto(titulo.getText().toString());
                            venta.setDescripcion_producto(detalle.getText().toString());

                            venta.setIdUsuario_movil(Globales.beanUsuario.getId_usuario());
                            venta.setIdTipo_transaccion(Globales.idTipoTransaccion);
                            venta.setFecha_inicio_venta(formato.format(new Date())); // es la fecha de publicacion
                            venta.setCondicion(spnCondicion.getSelectedItem().toString());
                            venta.setIdCategoria(itemProducto.getCategoriaMap().get(spnCategoria.getSelectedItem().toString()));
                            venta.setIdMarca(itemProducto.getMarcaMap().get(spnMarca.getSelectedItem().toString()));

                            List <Integer> ocasionIdList = new ArrayList <>(mapOcasion.values());
                            venta.setLstOcasion(ocasionIdList);

                            List <Integer> tallaIdList = new ArrayList <>(mapTalla.values());
                            venta.setLstTalla(tallaIdList);

                            venta.setColor_producto(color.getText().toString());

                            Intent irApublicar = new Intent(this, Publicar.class);
                            irApublicar.putExtra("venta", venta);
                            irApublicar.putExtra("provincias", (Serializable) itemProducto.getProvinciaMap());
                            startActivityForResult(irApublicar, 1);
                        }

                        if (Globales.idTipoTransaccion == Globales.tipo_renta) {

                            renta.setNombre_producto(titulo.getText().toString());
                            renta.setDescripcion_producto(detalle.getText().toString());

                            renta.setIdUsuario_movil(Globales.beanUsuario.getId_usuario());
                            renta.setIdTipo_transaccion(Globales.idTipoTransaccion);
                            renta.setFecha_inicio_renta(formato.format(new Date())); // es la fecha de publicacion
                            renta.setCondicion(spnCondicion.getSelectedItem().toString());
                            renta.setIdCategoria(itemProducto.getCategoriaMap().get(spnCategoria.getSelectedItem().toString()));
                            renta.setIdMarca(itemProducto.getMarcaMap().get(spnMarca.getSelectedItem().toString()));

                            List <Integer> ocasionIdList = new ArrayList <>(mapOcasion.values());
                            renta.setLstOcasion(ocasionIdList);

                            List <Integer> tallaIdList = new ArrayList <>(mapTalla.values());
                            renta.setLstTalla(tallaIdList);

                            renta.setColor_producto(color.getText().toString());

                            Intent irApublicar = new Intent(this, Publicar.class);
                            irApublicar.putExtra("renta", renta);
                            irApublicar.putExtra("provincias", (Serializable) itemProducto.getProvinciaMap());
                            startActivityForResult(irApublicar, 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Detalles_publicacion - resultCode " + resultCode);
        switch (requestCode) {
            case (1): {
                if (resultCode == 0) {
                    setResult(0);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//aca debes colocar el metodo que deseas que retorne
        return true;
    }

}
