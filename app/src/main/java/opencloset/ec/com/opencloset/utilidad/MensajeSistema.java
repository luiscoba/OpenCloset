package opencloset.ec.com.opencloset.utilidad;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class MensajeSistema {

    private Activity activity;
    private Context context;

    public MensajeSistema(Activity activity) {

        this.activity = activity;
    }

    public MensajeSistema(Context context) {

        this.context = context;
    }

    public void mostrarMensajeToastActivity(String mensaje) {
        Toast.makeText(this.activity, mensaje, Toast.LENGTH_LONG).show();
    }

    public void mostrarMensajeToastContext(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    public void notificacionPromociones(String titulo, String contenido) {

    }
}
