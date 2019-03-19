package opencloset.ec.com.opencloset.activity.sesion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.menu.Categorias;
import opencloset.ec.com.opencloset.global.Globales;

public class SplashActivity extends Activity {
    private Thread mSplashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1000);
                    }
                } catch (InterruptedException ex) {
                }

                if (Globales.seActivoLogin == true) {

                } else {
                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                }
                finish();
            }
        };
        mSplashThread.start();
    }
}