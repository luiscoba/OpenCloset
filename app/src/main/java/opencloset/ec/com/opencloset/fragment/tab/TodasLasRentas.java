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
import opencloset.ec.com.opencloset.adaptador.RentasAdaptador;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.fragment.items.detalles.MultipleItems;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class TodasLasRentas extends Fragment {

    private RentasAdaptador rentasAdaptador;
    private GridView grdvTodasLasRentas;
    private Conexion conexion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatex) {

        View view = inflater.inflate(R.layout.activity_todas_las_rentas, container, false);

        conexion = new Conexion();
        grdvTodasLasRentas = view.findViewById(R.id.grdvRentas);
        if (Globales.lstRentasPorCategoriaAdaptador == null) {
        } else {
            rentasAdaptador = new RentasAdaptador(this.getContext(), Globales.lstRentasPorCategoriaAdaptador);
            grdvTodasLasRentas.setAdapter(rentasAdaptador);
            grdvTodasLasRentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int pos, long id) {
                    //se oculta el tabLayout
                    Globales.tabLayout.setVisibility(View.GONE);
                    ArrayList <BeanFotos> lstFotos = null;
                    try {
                        lstFotos = conexion.obtenerFotosDelProducto(Globales.lstRentasPorCategoriaAdaptador.get(pos).getProducto().getId_producto());
                    } catch (ConexionHostExcepcion conexionHostExcepcion) {
                        if (!MetodoPara.isConnected(getContext()))
                            Toast.makeText(getContext(), "No hay conexión a wifi", Toast.LENGTH_LONG).show();
                    }
                    if (lstFotos != null) {
                        Intent rentaIntent = new Intent(getContext(), MultipleItems.class); // con esto abre MainActivity
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("renta", Globales.lstRentasPorCategoriaAdaptador.get(pos));
                        bundle.putSerializable("fotos", lstFotos);
                        bundle.putString("strImagenProducto", lstFotos.get(0).getNombre());
                        bundle.putInt("idTipoTransaccion", Globales.tipo_renta);
                        rentaIntent.putExtras(bundle);
                        startActivity(rentaIntent);
                    }
                }
            });
        }

        return view;
    }

}
