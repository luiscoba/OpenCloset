package opencloset.ec.com.opencloset.activity.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.adaptador.CategoriasAdaptador;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class Categorias extends AppCompatActivity {

    private GridView grdvTodosLasCategorias;
    private CategoriasAdaptador categoriasAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todas_las_categorias);

        MetodoPara.permisoDeEjecucion();

        grdvTodosLasCategorias = findViewById(R.id.grdvCategorias);
        categoriasAdaptador = new CategoriasAdaptador(getApplicationContext(), Globales.lstCategorias);

        categoriasAdaptador.setNotifyOnChange(true);
        grdvTodosLasCategorias.setAdapter(categoriasAdaptador);

        grdvTodosLasCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int posicion, long id) {
                boolean estadoEstrella = Globales.lstCategorias.get(posicion).getFavorito();
                if (estadoEstrella)
                    estadoEstrella = false;
                else
                    estadoEstrella = true;

                System.out.println("Categorias estadoEstrella " + estadoEstrella);
            }
        });

    }

    public void onClickListo(View view) {

        MetodoPara.ordenarCuponesComerciosPorCategorias();

        categoriasAdaptador.notifyDataSetChanged();
        finish();
        // esta es una animacion que desvanece el activity actual para dar paso al siguiente
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
