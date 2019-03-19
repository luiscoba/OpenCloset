package opencloset.ec.com.opencloset.activity.subir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToServer;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToServer;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.sesion.LlenarRegistroFaltante;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;

public class Revisar extends AppCompatActivity {

    private Conexion conexion;
    private VentasSendToServer venta;
    private RentasSendToServer renta;
    private BeanRespuesta respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisar);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        conexion = new Conexion();
        venta = new VentasSendToServer();
        renta = new RentasSendToServer();
        respuesta = new BeanRespuesta();

        if (Globales.idTipoTransaccion == Globales.tipo_venta) {
            Bundle bundle = getIntent().getExtras();
            venta = (VentasSendToServer) bundle.get("venta");
        }
        if (Globales.idTipoTransaccion == Globales.tipo_renta) {
            Bundle bundle = getIntent().getExtras();
            renta = (RentasSendToServer) bundle.get("renta");
        }
    }

    public void onIrAPublicar(View view) {

        if (Globales.idTipoTransaccion == Globales.tipo_venta) {
            try {
                respuesta = conexion.subirVenta(venta);
                if (respuesta.getCodigoError() == 20) {//osea no tiene registrado el celular
                    Intent irACompletarRegistroVendedor = new Intent(this, LlenarRegistroFaltante.class);
                    Toast.makeText(this, respuesta.getDescripcionError(), Toast.LENGTH_LONG).show();
                    startActivity(irACompletarRegistroVendedor);
                } else {
                    if (respuesta.getCodigoError() == 0) { //si es correcto
                        setResult(0);
                    } else {
                        setResult(10);
                    }
                    Toast.makeText(this, respuesta.getDescripcionError(), Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (ConexionHostExcepcion conexionHostExcepcion) {
                conexionHostExcepcion.printStackTrace();
            }
        }

        if (Globales.idTipoTransaccion == Globales.tipo_renta) {
            try {
                respuesta = conexion.subirRenta(renta);
                if (respuesta.getCodigoError() == 20) {//osea no tiene registrado el celular
                    Intent irACompletarRegistroRentador = new Intent(this, LlenarRegistroFaltante.class);
                    Toast.makeText(this, respuesta.getDescripcionError(), Toast.LENGTH_LONG).show();
                    startActivity(irACompletarRegistroRentador);
                } else {
                    if (respuesta.getCodigoError() == 0) { //si es correcto
                        setResult(0);
                    } else {
                        setResult(10);
                    }
                    Toast.makeText(this, respuesta.getDescripcionError(), Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (ConexionHostExcepcion conexionHostExcepcion) {
                conexionHostExcepcion.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(10);// para que no se vaya directo al mainPrincipal
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//aca debes colocar el metodo que deseas que retorne
        return true;
    }
}
