package opencloset.ec.com.opencloset.fragment.items;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.fragment.items.detalles.MultipleItems;
import opencloset.ec.com.opencloset.fragment.mapa.GoogleMapsFragmento;
import opencloset.ec.com.opencloset.global.Globales;

public class Mapa extends Fragment {

    private Button btnVerMapa;
    private TextView provincia_ciudad, direccion, dirOpcional;

    private VentasSendToMovil venta;
    private RentasSendToMovil renta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detalle_mapa, container, false);
        MultipleItems activity = (MultipleItems) getActivity();
        Bundle bundle = activity.getDatos();

        final int idTipoTransaccion = bundle.getInt("idTipoTransaccion");

        if (idTipoTransaccion == Globales.tipo_venta) {

            venta = (VentasSendToMovil) bundle.get("venta");

            provincia_ciudad = view.findViewById(R.id.txtProvinciaCiudad);
            direccion = view.findViewById(R.id.direccion);
            dirOpcional = view.findViewById(R.id.dirOpcional);
            btnVerMapa = view.findViewById(R.id.btnVerMapa);

            provincia_ciudad.setText(venta.getProvincia().getNombre());
            direccion.setText(venta.getProducto().getDireccion());
            if (venta.getProducto().getDireccion_opcional()==null)
                dirOpcional.setText("");
            else
                dirOpcional.setText(venta.getProducto().getDireccion_opcional());

            btnVerMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitud", venta.getProducto().getLatitud());
                    bundle.putDouble("longitud", venta.getProducto().getLongitud());

                    GoogleMapsFragmento googleMapsFragmento = new GoogleMapsFragmento();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    googleMapsFragmento.setArguments(bundle);
                    transaction.replace(R.id.layoutMultItem, googleMapsFragmento, "googleMapsFragmento");
                    transaction.addToBackStack(null);//con esto se activa el botón 'Atrás'
                    transaction.commit();
                }
            });
        }

        if (idTipoTransaccion == Globales.tipo_renta) {
            renta = (RentasSendToMovil) bundle.get("renta");

            provincia_ciudad = view.findViewById(R.id.txtProvinciaCiudad);
            provincia_ciudad.setText(renta.getProvincia().getNombre());
            btnVerMapa = view.findViewById(R.id.btnVerMapa);


            btnVerMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitud", renta.getProducto().getLatitud());
                    bundle.putDouble("longitud", renta.getProducto().getLongitud());

                    GoogleMapsFragmento googleMapsFragmento = new GoogleMapsFragmento();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    googleMapsFragmento.setArguments(bundle);
                    transaction.replace(R.id.layoutMultItem, googleMapsFragmento, "googleMapsFragmento");
                    transaction.addToBackStack(null);//con esto se activa el botón 'Atrás'
                    transaction.commit();
                }
            });
        }

        return view;
    }

}
