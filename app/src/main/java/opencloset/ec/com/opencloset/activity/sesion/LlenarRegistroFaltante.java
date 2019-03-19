package opencloset.ec.com.opencloset.activity.sesion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class LlenarRegistroFaltante extends AppCompatActivity {

    private BeanUsuario usuario;
    private BeanRespuesta respuesta;
    private Conexion conexion;
    private AutoCompleteTextView convencional, celular, direccion;
    private int nDigCelular, nDigConvencional;
    private MensajeSistema mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completa_registro_faltante);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        conexion = new Conexion();
        usuario = new BeanUsuario();
        mensaje = new MensajeSistema(this);

        convencional = findViewById(R.id.telfConvencional);
        celular = findViewById(R.id.telfCelular);
        direccion = findViewById(R.id.direccion);

        celular.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nDigCelular = celular.length();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        convencional.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nDigConvencional = convencional.length();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void onClickCompletarRegistro(View view) {

        if (validaInformacion()) {
            usuario.setId_usuario(Globales.beanUsuario.getId_usuario());
            usuario.setCelular(celular.getText().toString());
            usuario.setDireccion_foto(direccion.getText().toString());
            usuario.setConvencional(convencional.getText().toString());

            try {
                respuesta = conexion.completaRegistroFaltante(usuario);

                mensaje.mostrarMensajeToastActivity(respuesta.getDescripcionError());

                finish();
            } catch (ConexionHostExcepcion conexionHostExcepcion) {
                conexionHostExcepcion.printStackTrace();
            }
        }
    }

    private boolean validaInformacion() {

        boolean validaInformacion = true;
        // Reset errors.
        celular.setError(null);
        convencional.setError(null);
        // Comprueba si hay un celular válido, si el usuario ingresó uno.
        if (nDigCelular != 10 || celular.getText().toString().trim().isEmpty()) {
            celular.setError(getString(R.string.errorCelularInvalido));
            celular.requestFocus();
            validaInformacion = false;
        }

        if (nDigConvencional > 0) {
            if (nDigConvencional != 9) {
                convencional.setError(getString(R.string.errorConvencionalInvalido));
                convencional.requestFocus();
                validaInformacion = false;
            } else
                usuario.setConvencional(celular.getText().toString());
        }

        return validaInformacion;
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
