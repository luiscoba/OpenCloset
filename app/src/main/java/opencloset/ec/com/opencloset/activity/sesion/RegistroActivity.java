package opencloset.ec.com.opencloset.activity.sesion;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ec.deployits.ventaDeRopa.bean.BeanDispositivoPosicion;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;

import java.util.regex.Pattern;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.fragment.dialogs.NuevaClave;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;


public class RegistroActivity extends AppCompatActivity {
    private MensajeSistema mensaje;
    private Conexion conexion;

    private EditText txtNombre, txtApellido, txtEmail;
    private Button btnRegistrase;
    private ProgressDialog progressDialog;
    //private InfoUsuario db;
    String correoStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtNombre = findViewById(R.id.nombreReg);
        txtApellido = findViewById(R.id.apellidoReg);
        txtEmail = findViewById(R.id.emailReg);

        mensaje = new MensajeSistema(this);
        //permisos para la ejecucion de hilos
        MetodoPara.permisoDeEjecucion();

        conexion = new Conexion();

    }

    public void onClickRegistrarNuevoUsuario(View view) {
        if (validaInformacion()) {

            BeanUsuario bUsuario = new BeanUsuario();
            BeanDispositivoPosicion bDispositivoPosicion = new BeanDispositivoPosicion();

            String nombreStr = txtNombre.getText().toString();
            String apellidoStr = txtApellido.getText().toString();
            String emailStr = txtEmail.getText().toString();

            bUsuario.setCelular(null);  // celularStr este campo luego se obtendrá el numero de telefono
            correoStr = emailStr;

            bUsuario.setCorreo(correoStr);
            bUsuario.setNombre(nombreStr);
            bUsuario.setApellido(apellidoStr);
            bUsuario.setDomicilio("");
            bUsuario.setClave("");
            bUsuario.setCorreo(emailStr);

            DialogFragment mostrarDialogFragment = new NuevaClave();
            Bundle bundle = new Bundle();
            bundle.putString("correo", correoStr);
            bundle.putSerializable("beanUsuario", bUsuario);

            mostrarDialogFragment.setArguments(bundle);
            mostrarDialogFragment.setCancelable(false);
            mostrarDialogFragment.show(getSupportFragmentManager(), "dialogoParaNuevaClave");

        } else {
            mensaje.mostrarMensajeToastActivity("complete los campos requeridos");
        }
    }

    private boolean validaInformacion() {

        boolean activarButton = true;

        if (txtNombre.getText().toString().trim().isEmpty()) {
            txtNombre.setError(getString(R.string.errorCampoRequerido));
            txtNombre.requestFocus();
            mensaje.mostrarMensajeToastActivity("Ingrese su nombre");
            activarButton = false;
        } else if (txtApellido.getText().toString().trim().isEmpty()) {
            txtApellido.setError(getString(R.string.errorCampoRequerido));
            txtApellido.requestFocus();
            mensaje.mostrarMensajeToastActivity("Ingrese su apellido");
            activarButton = false;
        } else if (txtEmail.getText().toString().trim().isEmpty()) {
            txtEmail.setError(getString(R.string.errorCampoRequerido));
            txtEmail.requestFocus();
            mensaje.mostrarMensajeToastActivity("Ingrese su Email");
            activarButton = false;
        } else if (!validarEmail(txtEmail.getText().toString())) {//revisa si tiene formato de correo
            txtEmail.requestFocus();
            txtEmail.setError("Email no válido");
            activarButton = false;
        }

        return activarButton;
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

