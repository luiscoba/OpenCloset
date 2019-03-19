package opencloset.ec.com.opencloset.fragment.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.BeanFotos;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.fragment.items.detalles.MultipleItems;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.mostrarImagenes.VisualzarImagenes;

public class Fotos extends Fragment {

    private ImageView foto;
    private TextView numero_de_fotos, categoria;

    private ArrayList <BeanFotos> fotos;
    private VentasSendToMovil venta;
    private RentasSendToMovil renta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detalle_fotos, container, false);

        MultipleItems activity = (MultipleItems) getActivity();
        Bundle bundle = activity.getDatos();
        int idTipoTransaccion = bundle.getInt("idTipoTransaccion");

        fotos = (ArrayList <BeanFotos>) bundle.getSerializable("fotos");

        foto = view.findViewById(R.id.imgFotoPrincipal);
        numero_de_fotos = (TextView) view.findViewById(R.id.txtNumFotos);
        categoria = (TextView) view.findViewById(R.id.txtCategoria);

        String txtNumFotos;
        if (fotos == null) {
            txtNumFotos = "sin foto";
        } else {
            txtNumFotos = String.valueOf(fotos.size()) + " fotos";
        }
        numero_de_fotos.setText(txtNumFotos);

        if (idTipoTransaccion == Globales.tipo_venta) {
            venta = (VentasSendToMovil) bundle.get("venta");
            categoria.setText(venta.getCategoria().getNombre());

            Picasso.with(getContext())
                    .load(Globales.ipHost + "image?img=" + venta.getUbicacion_primera_foto())
                    .into(foto);
        }

        // -- SECCIÓN RENTAR
        if (idTipoTransaccion == Globales.tipo_renta) {
            renta = (RentasSendToMovil) bundle.get("renta");
            categoria.setText(renta.getCategoria().getNombre());

            Picasso.with(getContext())
                    .load(Globales.ipHost + "image?img=" + renta.getUbicacion_primera_foto())
                    .into(foto);
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irAvisualizarImagenes = new Intent(getContext(), VisualzarImagenes.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fotos", fotos);
                System.out.println("fotos Fotos " + fotos);
                irAvisualizarImagenes.putExtras(bundle);
                startActivity(irAvisualizarImagenes);
            }
        });

        return view;
    }
}
