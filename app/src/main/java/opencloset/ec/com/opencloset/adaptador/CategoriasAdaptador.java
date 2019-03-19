package opencloset.ec.com.opencloset.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.BeanCategoria;
import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.squareup.picasso.Picasso;

import java.util.List;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.global.Globales;

public class CategoriasAdaptador extends ArrayAdapter<BeanCategoria> {

    private Conexion conexion;

    private BeanCategoria beanCategoria;
    private BeanRespuesta res;
    private Context context;

    public CategoriasAdaptador(Context context, @NonNull List<BeanCategoria> lstCategorias) {
        super(context, R.layout.todas_las_categorias, lstCategorias);
        this.context = context;

        Globales.lstCategorias = lstCategorias;

        beanCategoria = new BeanCategoria();

        conexion = new Conexion();

    }

    @Override
    public View getView(final int posicion, View item, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        item = inflater.inflate(R.layout.activity_item_categoria, null);

        TextView nombreCategoria = item.findViewById(R.id.txtvNombreCategoria);
        nombreCategoria.setText(Globales.lstCategorias.get(posicion).getNombre());

        ImageView imagen = item.findViewById(R.id.imgCategoria);
//        byte[] image = Base64.decode(Globales.lstCategorias.get(posicion).getImagen(), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//        imagen.setImageBitmap(bitmap);
        Picasso.with(getContext())
                .load(Globales.lstCategorias.get(posicion).getDireccion_imagen())
                .into(imagen);

        return item;
    }

}
