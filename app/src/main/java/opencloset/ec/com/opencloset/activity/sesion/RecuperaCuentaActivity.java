package opencloset.ec.com.opencloset.activity.sesion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;

import java.util.regex.Pattern;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class RecuperaCuentaActivity extends AppCompatActivity {

    private MensajeSistema mensaje;
    private Conexion conexion;

    private EditText txtCorreo;
    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_cuenta);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtCorreo = findViewById(R.id.correo);

        MetodoPara.permisoDeEjecucion();

        mensaje = new MensajeSistema(this);
        conexion = new Conexion();
    }

    public void onClickRecuperaCuenta(View view) {

        BeanUsuario beanUsuario = new BeanUsuario();

        View focusView = null;

        correo = txtCorreo.getText().toString();
        // Comprueba si hay una contraseña válida, si el usuario ingresó una.
        if (correo.isEmpty()) {
            txtCorreo.setError(getString(R.string.errorCampoRequerido));
            focusView = txtCorreo;
            mensaje.mostrarMensajeToastActivity("Ingrese correo electrónico");
        } else {// Revisa si es valido el correo.
            if (!validarEmail(correo)) {//revisa si tiene formato de correo
                txtCorreo.setError("Email no válido");
                focusView = txtCorreo;
            } else {
                beanUsuario.setCorreo(correo);
                //               beanCliente.setDocumento(correo);
                try {
                    BeanRespuesta res = conexion.recuperarCuenta(beanUsuario);
                    mensaje.mostrarMensajeToastActivity(res.getDescripcionError());

                    Intent irAloginActivity = new Intent(this, LoginActivity.class);
                    startActivity(irAloginActivity);//ahora se dispara el método
                    Globales.beanUsuario = conexion.obtenerDatosDeUsuario(beanUsuario);
                    finish();
                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

                } catch (ConexionHostExcepcion conexionHostExcepcion) {
                    conexionHostExcepcion.printStackTrace();
                }
            }
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();//aqui ya regresa al anterior activity
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//aca debes colocar el metodo que deseas que retorne
        return true;
    }

}
