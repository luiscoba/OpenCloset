package opencloset.ec.com.opencloset.fragment.items.detalles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import opencloset.ec.com.opencloset.R;

public class MultipleItems extends AppCompatActivity {

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_item);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        bundle = getIntent().getExtras();
    }

    public Bundle getDatos() {

        bundle = getIntent().getExtras();

        return bundle;
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
