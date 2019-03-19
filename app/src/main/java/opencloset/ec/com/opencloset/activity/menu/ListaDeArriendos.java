package opencloset.ec.com.opencloset.activity.menu;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ec.deployits.ventaDeRopa.bean.BeanArriendos;

import java.util.ArrayList;
import java.util.List;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.menu.transacciones.ArriendoFragment;
import opencloset.ec.com.opencloset.adaptador.ArriendosAdaptador;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class ListaDeArriendos extends AppCompatActivity {

    private MensajeSistema mensaje;
    private ArriendosAdaptador todosLosArriendos;
    private GridView lstvTodosLosArriendos;
    private List<BeanArriendos> lstBeanArriendos = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_arriendos);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Globales.fab = findViewById(R.id.fab);
        Globales.fab.setVisibility(View.GONE);

        mensaje = new MensajeSistema(this);

        lstvTodosLosArriendos = findViewById(R.id.grdvArriendos);

        Conexion conexion = new Conexion();

        try {
            lstBeanArriendos = conexion.obtenerTodosLosArriendos(Globales.beanUsuario.getId_usuario());

            System.out.println("lstBeanArriendos " + lstBeanArriendos);
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }
        if (lstBeanArriendos == null) {
            mensaje.mostrarMensajeToastActivity("No tiene arriendos realizados");
            finish();
        } else {
            todosLosArriendos = new ArriendosAdaptador(this, lstBeanArriendos);
            lstvTodosLosArriendos.setAdapter(todosLosArriendos);

            lstvTodosLosArriendos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int posicion, long id) {
/*
                    Integer idSucursal    = Globales.lstComerciosPorCategoriaAdaptador.get(posicion).getIdSucursal();
                    Integer idEmpresa     = Globales.lstComerciosPorCategoriaAdaptador.get(posicion).getIdEmpresa();
                    String nombreSucursal = Globales.lstComerciosPorCategoriaAdaptador.get(posicion).getNombre();

                    Bundle args = new Bundle();
                    args.putInt("empresa_favorita_seleccionada", idSucursal);
                    args.putInt("idEmpresa_seleccionada", idEmpresa);
                    args.putString("nombre_empresa_seleccionada", nombreSucursal);
*/
                    ArriendoFragment arriendo = new ArriendoFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    compra.setArguments(args);
                    transaction.replace(R.id.contenedor, arriendo, "arriendoFragment");
                    transaction.addToBackStack(null);//con esto se activa el botón 'Atrás'
                    transaction.commit();
                }
            });
        }
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