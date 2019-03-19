package opencloset.ec.com.opencloset.activity.notificaciones;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.MainActivity;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class SinConexionActivity extends AppCompatActivity {

    private MensajeSistema mensajeSistema;
    private boolean hayConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_conexion);

        mensajeSistema = new MensajeSistema(this);

        Button btnIrAWiFi = findViewById(R.id.btnWiFi);
        Button btnRefrescar = findViewById(R.id.btnRefrescar);

        Globales.regresoDesdeSinConexionActivity = true;

        btnIrAWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                //finish();
            }
        });

        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seVerificaConexion();
            }
        });
    }

    @Override
    public void onBackPressed() {
        hayConexion = MetodoPara.isConnected(this);

        if (hayConexion) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            System.out.println("-------------------------------------------------");
            System.out.println("-----  onBackPressed me quedo en SinConexionActivity  ------");
            System.out.println("-------------------------------------------------");
        }
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mensajeSistema.mostrarMensajeToastActivity("Compruebe sus ajustes");
    }

    @Override
    public void onStop() {
        super.onStop();
//no vale usar este metodo porque crea 2 veces el mainActivity
       // seVerificaConexion();
    }

    @Override
    protected void onResume() {
        super.onResume();

        seVerificaConexion();
    }

    public void seVerificaConexion() {

        hayConexion = MetodoPara.isConnected(this);

        if (hayConexion) {
            finish();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            System.out.println("-------------------------------------------------");
            System.out.println("-----  onBackPressed me quedo en SinConexionActivity  ------");
            System.out.println("-------------------------------------------------");
        }else{
            mensajeSistema.mostrarMensajeToastActivity("Compruebe sus ajustes");
        }
    }
}
