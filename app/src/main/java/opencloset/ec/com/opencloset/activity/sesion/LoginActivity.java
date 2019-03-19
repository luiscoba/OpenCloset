package opencloset.ec.com.opencloset.activity.sesion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.hilos.HiloCargarListasDeUsuarioAlSistema;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.fragment.dialogs.NuevaClave;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class LoginActivity extends AppCompatActivity {
    private MensajeSistema mensaje;
    private Conexion conexion;

    // UI references.
    private AutoCompleteTextView actxvwCorreo;
    private EditText txbxPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MetodoPara.permisoDeEjecucion();

        conexion = new Conexion();
        mensaje = new MensajeSistema(this);

        actxvwCorreo = findViewById(R.id.correo);
        txbxPass = findViewById(R.id.password);

        TextView txtvPolitica = findViewById(R.id.textViewPolitica);
        SpannableString textoSubrayado = new SpannableString(txtvPolitica.getText().toString());
        textoSubrayado.setSpan(new UnderlineSpan(), 0, textoSubrayado.length(), 0);
        txtvPolitica.setText(textoSubrayado);
    }

    public void onClickLinkPolitica(View view) {
        String direccion = Globales.politica_de_privacidad;
        Uri uri = Uri.parse(direccion);
        Intent intentNav = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intentNav);
    }

    private boolean validaInformacion() {

        boolean validaInformacion = true;
        // Reset errors.
        actxvwCorreo.setError(null);
        txbxPass.setError(null);
        // Comprueba si hay una contraseña válida, si el usuario ingresó una.
        if (txbxPass.getText().toString().trim().isEmpty()) {
            txbxPass.setError(getString(R.string.errorPasswordInvalido));
            txbxPass.requestFocus();
            //          mensaje.mostrarMensajeToast("Ingrese password");
            validaInformacion = false;
        }
        // Revisa si es valido el correo.
        if (actxvwCorreo.getText().toString().trim().isEmpty()) {
            actxvwCorreo.setError(getString(R.string.errorCampoRequerido));
            actxvwCorreo.requestFocus();
            validaInformacion = false;
        }
        return validaInformacion;
    }

    public void onClickInicioSesion(View view) {

        if (validaInformacion()) {

            String correo = actxvwCorreo.getText().toString();
            String password = txbxPass.getText().toString();

            BeanUsuario beanUsuario = new BeanUsuario();
            beanUsuario.setCorreo(correo);
            beanUsuario.setClave(password);
            BeanRespuesta res = null;
            try {

                res = conexion.inicioSesionCliente(beanUsuario);

                if (res.getCodigoError() == 0) {//inicia correctamente

                    System.out.println("beanRespuesta.getCodigoError() " + res.getCodigoError());
                    System.out.println("beanRespuesta.getDescripcionError() " + res.getDescripcionError());

                    try {
                        Globales.beanUsuario = conexion.obtenerDatosDeUsuario(beanUsuario);

                    } catch (ConexionHostExcepcion conexionHostExcepcion1) {
                        conexionHostExcepcion1.printStackTrace();
                    }

                    new HiloCargarListasDeUsuarioAlSistema(LoginActivity.this, LoginActivity.this).execute();

                    MetodoPara.guardarPreferenciaBeanUsuario(LoginActivity.this);
//                finish(); ya se finaliza en el hilo
                } else if (res.getCodigoError() == 10) {// UsuarioBloqueado | UsuarioNoExiste | ContraseniaNoRegistrada
//                mensaje.mostrarMensajeToast(res.getDescripcionError());
                } else if (res.getCodigoError() == 11) {// ContraseniaExpirada
//                mensaje.mostrarMensajeToast(res.getDescripcionError());

                    //mostramos el dialogFragment
                    DialogFragment mostrarDialogFragment = new NuevaClave();
                    Bundle bundle = new Bundle();
                    bundle.putString("correo", correo);
                    bundle.putString("password", password);
                    mostrarDialogFragment.setArguments(bundle);
                    mostrarDialogFragment.setCancelable(false);
                    mostrarDialogFragment.show(getSupportFragmentManager(), "dialogoParaNuevaClave");
                }
                mensaje.mostrarMensajeToastActivity(res.getDescripcionError());
            } catch (
                    ConexionHostExcepcion conexionHostExcepcion) {
                mensaje.mostrarMensajeToastActivity("Error al conectarse al servidor");
                System.out.println("Error al conectarse al servidor");
            }
        }
    }

    public void onClickCrearCuenta(View view) {//partimos de este activity, indicamos al activity al q nos vamos
        Intent crearCuenta = new Intent(this, RegistroActivity.class);
        startActivity(crearCuenta);//ahora se dispara el método
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    public void onClickRecuperaCuenta(View view) {//partimos de este activity,vamos a RecuperaCuentaActivity
        Intent recuperaCuenta = new Intent(this, RecuperaCuentaActivity.class);
        startActivity(recuperaCuenta);//ahora se dispara el método
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}