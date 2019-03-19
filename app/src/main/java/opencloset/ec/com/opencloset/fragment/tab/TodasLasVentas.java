package opencloset.ec.com.opencloset.fragment.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ec.deployits.ventaDeRopa.bean.BeanFotos;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.adaptador.VentasAdaptador;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.fragment.items.detalles.MultipleItems;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

//aqui se muestran todos los los articulos a la venta
public class TodasLasVentas extends Fragment {

    private VentasAdaptador ventasAdaptador;
    private GridView grdvTodasLasVentas;
    private Conexion conexion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatex) {
        View view = inflater.inflate(R.layout.activity_todas_las_ventas, container, false);

        conexion = new Conexion();
        grdvTodasLasVentas = view.findViewById(R.id.grdvVentas);
        if (Globales.lstVentasPorCategoriaAdaptador == null) {
        } else {
            ventasAdaptador = new VentasAdaptador(this.getContext(), Globales.lstVentasPorCategoriaAdaptador);
            grdvTodasLasVentas.setAdapter(ventasAdaptador);
            grdvTodasLasVentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int pos, long id) {
                    //se oculta el tabLayout
                    Globales.tabLayout.setVisibility(View.GONE);
                    ArrayList <BeanFotos> lstFotos = null;
                    try {
                        lstFotos = conexion.obtenerFotosDelProducto(Globales.lstVentasPorCategoriaAdaptador.get(pos).getProducto().getId_producto());
                    } catch (ConexionHostExcepcion conexionHostExcepcion) {
                        if (!MetodoPara.isConnected(getContext()))
                            Toast.makeText(getContext(), "No hay conexión a wifi", Toast.LENGTH_LONG).show();
                    }

                    if (lstFotos != null) {
                        Intent ventaIntent = new Intent(getContext(), MultipleItems.class); // con esto abre MainActivity
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("venta", Globales.lstVentasPorCategoriaAdaptador.get(pos));
                        bundle.putSerializable("fotos", lstFotos);
                        bundle.putString("strImagenProducto", lstFotos.get(0).getNombre());
                        bundle.putInt("idTipoTransaccion", Globales.tipo_venta);
                        ventaIntent.putExtras(bundle);
                        startActivity(ventaIntent);
                    }
                }

            });
        }

        return view;
    }

}
