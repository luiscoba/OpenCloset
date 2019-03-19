package opencloset.ec.com.opencloset.activity.menu;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ec.deployits.ventaDeRopa.bean.BeanCompras;

import java.util.ArrayList;
import java.util.List;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.adaptador.ComprasAdaptador;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.activity.menu.transacciones.CompraFragment;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class ListaDeCompras extends AppCompatActivity {

    private MensajeSistema mensaje;
    private ComprasAdaptador todasLasCompras;
    private GridView lstvTodasLasCompras;
    private List <BeanCompras> lstBeanCompras = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_compras);

        Toolbar toolbar = findViewById(R.id.toolbarManual);
        setSupportActionBar(toolbar);
        //       getSupportActionBar().setIcon(R.drawable.usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Globales.fab = findViewById(R.id.fab);
        Globales.fab.setVisibility(View.GONE);

        mensaje = new MensajeSistema(this);

        lstvTodasLasCompras = findViewById(R.id.grdvCompras);

        Conexion conexion = new Conexion();

        try {
            lstBeanCompras = conexion.obtenerTodasLasCompras(Globales.beanUsuario.getId_usuario());

            System.out.println("lstBeanCompras " + lstBeanCompras);
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }
        if (lstBeanCompras == null) {
            mensaje.mostrarMensajeToastActivity("No tiene compras realizadas");
            finish();
        } else {
            todasLasCompras = new ComprasAdaptador(this, lstBeanCompras);
            lstvTodasLasCompras.setAdapter(todasLasCompras);

            lstvTodasLasCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    CompraFragment compra = new CompraFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    compra.setArguments(args);
                    transaction.replace(R.id.contenedor, compra, "compraFragment");
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