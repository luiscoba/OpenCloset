package opencloset.ec.com.opencloset.fragment.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.hilos.HiloCargarListasDeUsuarioAlSistema;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class LoginDialog extends DialogFragment {

    private MensajeSistema mensaje;
    private Conexion conexion;
    private EditText correo, password;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        MetodoPara.permisoDeEjecucion();

        conexion = new Conexion();
        mensaje = new MensajeSistema(getActivity());

        builder.setTitle("Inicia sesión");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null);

        correo = view.findViewById(R.id.correo);
        password = view.findViewById(R.id.password);

        builder.setView(view);
        builder.setPositiveButton(R.string.logIng, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                validaInformacion();

                String correoStr = correo.getText().toString();
                String passwordStr = password.getText().toString();

                if (correo.equals("") || password.equals("")) {
                    //mensaje.mostrarMensajeToast("Ingrese una contraseña");
                    // dismiss();   es para cerrar el DialogFragment
                } else {
                    BeanUsuario beanUsuario = new BeanUsuario();
                    beanUsuario.setCorreo(correoStr);
                    beanUsuario.setClave(passwordStr);
                    BeanRespuesta res;

                    try {
                        res = conexion.inicioSesionCliente(beanUsuario);

                        if (res.getCodigoError() == 0) {//inicia correctamente
                            Globales.beanUsuario = conexion.obtenerDatosDeUsuario(beanUsuario);

                            //este hilo tiene un finish adentro que finaliza la actividad
                            new HiloCargarListasDeUsuarioAlSistema(getContext(), getView()).execute();

                            try {
                                Globales.beanUsuario = conexion.obtenerDatosDeUsuario(beanUsuario);
                            } catch (ConexionHostExcepcion conexionHostExcepcion) {
                                conexionHostExcepcion.printStackTrace();
                            }

                            MetodoPara.guardarPreferenciaBeanUsuario(getContext());

                            mensaje.mostrarMensajeToastActivity("¡Iniciado sesíon correctamente!");

                        } else if (res.getCodigoError() == 10) {// UsuarioBloqueado | UsuarioNoExiste | ContraseniaNoRegistrada

                            mensaje.mostrarMensajeToastActivity(res.getDescripcionError());

                        } else if (res.getCodigoError() == 11) {// ContraseniaExpirada
                            mensaje.mostrarMensajeToastActivity(res.getDescripcionError());

                            //mostramos el dialogFragment
                            DialogFragment mostrarDialogFragment = new NuevaClave();
                            Bundle bundle = new Bundle();
                            bundle.putString("correo", correoStr);
                            bundle.putString("password", passwordStr);
                            mostrarDialogFragment.setArguments(bundle);
                            mostrarDialogFragment.setCancelable(false);
                            mostrarDialogFragment.show(getActivity().getSupportFragmentManager(), "dialogoParaNuevaClave");
                        }
                    } catch (ConexionHostExcepcion conexionHostExcepcion) {
                        mensaje.mostrarMensajeToastActivity("Error al conectarse al servidor");
                        System.out.println("Error al conectarse al servidor");
                    }
                }
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    private void validaInformacion() {
        // Reset errors.
        correo.setError(null);
        password.setError(null);

        View focusView = null;

        // Comprueba si hay una contraseña válida, si el usuario ingresó una.
        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getString(R.string.errorPasswordInvalido));
            focusView = password;
            //          mensaje.mostrarMensajeToast("Ingrese password");
        }
        // Revisa si es valido el documento.
        if (correo.getText().toString().trim().isEmpty()) {
            correo.setError(getString(R.string.errorCampoRequerido));
            focusView = correo;
            //          mensaje.mostrarMensajeToast("Ingrese documento");
        }
    }

}