package opencloset.ec.com.opencloset.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.global.Globales;

public class VentasAdaptador extends ArrayAdapter <VentasSendToMovil> implements Filterable {

    private Holder holder;
    private Context mContext;
    private List <VentasSendToMovil> lstBeanVentas;
    private List <VentasSendToMovil> lstBeanVentasFilter;
    private CustomFilter mFilter;

    public VentasAdaptador(Context context, @NonNull List <VentasSendToMovil> lstBeanVentas) {
        super(context, R.layout.activity_lista_ventas, lstBeanVentas);

        this.mContext = context;
        this.lstBeanVentas = lstBeanVentas;
        this.lstBeanVentasFilter = new ArrayList <>();
        this.lstBeanVentasFilter.addAll(lstBeanVentas);
        this.mFilter = new CustomFilter(VentasAdaptador.this);
    }

    @NonNull
    @Override
    public View getView(final int pos, View item, ViewGroup parent) {
        //cuando es null es la primera vez que se crea el item
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            item = inflater.inflate(R.layout.activity_lista_ventas, parent, false);

            holder = new VentasAdaptador.Holder();

            holder.nomProducto = item.findViewById(R.id.txtvNomProducto);
            holder.descProducto = item.findViewById(R.id.txtvDescripcion);
            holder.imagen = item.findViewById(R.id.imgProd);
            holder.precio = item.findViewById(R.id.txtvPrecio);

            //esto hace que se coloque super bien la imagen
            scaleImage(holder.imagen);
            item.setTag(holder);
        } else {
            holder = (VentasAdaptador.Holder) item.getTag();
        }

        holder.nomProducto.setText(lstBeanVentasFilter.get(pos).getProducto().getNombre());
        holder.descProducto.setText(lstBeanVentasFilter.get(pos).getProducto().getDescripcion());
        holder.precio.setText("U$S " + String.valueOf(lstBeanVentasFilter.get(pos).getProducto().getPrecio()));
        Picasso.with(getContext())
                .load(Globales.ipHost + "image?img=" + lstBeanVentasFilter.get(pos).getUbicacion_primera_foto())
                .into(holder.imagen);
        // Devolvemos la vista para que se muestre en el GridView.
        return item;
    }

    static class Holder {
        ImageView imagen;
        TextView nomProducto, descProducto, precio;
    }

    private void scaleImage(ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
//        Log.i("Test", "original width = " + Integer.toString(width));
//        Log.i("Test", "original height = " + Integer.toString(height));
//        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
//        Log.i("Test", "xScale = " + Float.toString(xScale));
//        Log.i("Test", "yScale = " + Float.toString(yScale));
//        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
//        Log.i("Test", "scaled width = " + Integer.toString(width));
//        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

//        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    public int getCount() {
        return lstBeanVentasFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    /*Filtro*/
    public class CustomFilter extends Filter {
        private VentasAdaptador listAdapter;

        private CustomFilter(VentasAdaptador listAdapter) {
            super();
            this.listAdapter = listAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            lstBeanVentasFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                lstBeanVentasFilter.addAll(lstBeanVentas);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final VentasSendToMovil Venta : lstBeanVentas) {
                    if (Venta.getProducto().getNombre().toLowerCase().contains(filterPattern)) {
                        lstBeanVentasFilter.add(Venta);
                    }
                }
            }
            results.values = lstBeanVentasFilter;
            results.count = lstBeanVentasFilter.size();
            Globales.lstVentasPorCategoriaAdaptador = lstBeanVentasFilter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.listAdapter.notifyDataSetChanged();
        }
    }
}
