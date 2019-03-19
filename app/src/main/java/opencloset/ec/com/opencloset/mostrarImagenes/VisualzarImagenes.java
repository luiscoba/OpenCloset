package opencloset.ec.com.opencloset.mostrarImagenes;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ec.deployits.ventaDeRopa.bean.BeanFotos;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;

public class VisualzarImagenes extends AppCompatActivity {
    ViewPager viewPager;
    SwipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizador_de_fotos);

        Bundle bundle = getIntent().getExtras();
        ArrayList<BeanFotos> fotos = (ArrayList <BeanFotos>) bundle.getSerializable("fotos");

        System.out.println("fotos VisualizadorImagenes " + fotos);
        viewPager = findViewById(R.id.view_pager);
        adapter = new SwipeAdapter(this, fotos);
        viewPager.setAdapter(adapter);
    }
}
