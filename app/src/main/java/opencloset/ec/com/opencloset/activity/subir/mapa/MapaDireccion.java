package opencloset.ec.com.opencloset.activity.subir.mapa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.global.Globales;

public class MapaDireccion extends AppCompatActivity implements GoogleMapsFrgBuscaCiudad.IobtieneDireccion {

    private EditText direccion_opcional;
    private TextView direccion;
    private Button aceptar;

    private Double latitud, longitud;
    private String provincia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_direccion);

        setResult(10);// para que no se cierre el activity Publicar

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        direccion_opcional = findViewById(R.id.edtDireccion_opcional);
        direccion = findViewById(R.id.txtDireccion);
        aceptar = findViewById(R.id.btnHecho);

        GoogleMapsFrgBuscaCiudad fragment = new GoogleMapsFrgBuscaCiudad();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmMap, fragment);
        transaction.commit();
        // este codigo quita el focus del TextView
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onDireccion(String direccion, String provincia, Double latitud, Double longitud) {
        Globales.direccion = direccion;

        this.direccion.setText(direccion);
        this.latitud = latitud;
        this.longitud = longitud;
        this.provincia = provincia;
    }

    public void onClickRegresaApublicar(View view) {
        //enviamos los datos recogidos en este activity
        Intent intent = new Intent();
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        intent.putExtra("provincia", provincia);
        System.out.println("direccion_opcional.getText() " + direccion_opcional.getText());
        intent.putExtra("direccion_opcional", direccion_opcional.getText().toString());
        setResult(Activity.RESULT_OK, intent);

        finish();
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
