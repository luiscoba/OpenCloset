package opencloset.ec.com.opencloset.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.BeanCompras;
import com.squareup.picasso.Picasso;

import java.util.List;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.global.Globales;

public class ComprasAdaptador extends ArrayAdapter <BeanCompras> {
    private ViewHolder viewHolder;

    private List <BeanCompras> lstCompras;
    private Context context;

    public ComprasAdaptador(Context context, @NonNull List <BeanCompras> lstBeanCompras) {
        super(context, R.layout.fragment_compra, lstBeanCompras);

        this.context = context;
        lstCompras = lstBeanCompras;
    }

    @NonNull
    @Override
    public View getView(final int posicion, View item, ViewGroup parent) {
        //cuando es null es la primera vez que se crea el item

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.fragment_compra, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.estado = item.findViewById(R.id.txtEstado);
            viewHolder.imagen = item.findViewById(R.id.imgProd);

            item.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) item.getTag();
        }

        viewHolder.estado.setText(lstCompras.get(posicion).getObservacion());

        Picasso.with(getContext())
                .load(Globales.ipHost + "image?img=" + lstCompras.get(posicion).getUbicacion_primera_foto())

                .into(viewHolder.imagen);

        // Devolvemos la vista para que se muestre en el GridView.
        return item;

    }

    static class ViewHolder {
        ImageView imagen;
        TextView estado;
    }
}
