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

import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.global.Globales;

public class RentasAdaptador extends ArrayAdapter <RentasSendToMovil> implements Filterable {

    private Holder holder;
    private Context context;
    private List <RentasSendToMovil> lstBeanRentas;
    private List <RentasSendToMovil> lstBeanRentasFilter;
    private CustomFilter mFilter;

    public RentasAdaptador(Context context, @NonNull List <RentasSendToMovil> lstBeanRentas) {
        super(context, R.layout.activity_lista_rentas, lstBeanRentas);

        this.context = context;
        this.lstBeanRentas = lstBeanRentas;
        this.lstBeanRentasFilter = new ArrayList<>();
        this.lstBeanRentasFilter.addAll(lstBeanRentas);
        this.mFilter = new CustomFilter(RentasAdaptador.this);
    }

    @NonNull
    @Override
    public View getView(final int pos, View item, ViewGroup parent) {
        //cuando es null es la primera vez que se crea el item
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            item = inflater.inflate(R.layout.activity_lista_rentas, parent, false);

            holder = new RentasAdaptador.Holder();

            holder.nomProducto = item.findViewById(R.id.txtvNomProducto);
            holder.descProducto = item.findViewById(R.id.txtvDescripcion);
            holder.imagen = item.findViewById(R.id.imgProd);
            holder.precio = item.findViewById(R.id.txtvPrecio);

            //esto hace que se coloque super bien la imagen
            scaleImage(holder.imagen);
            item.setTag(holder);
        } else {
            holder = (RentasAdaptador.Holder) item.getTag();
        }

        holder.nomProducto.setText(lstBeanRentas.get(pos).getProducto().getNombre());
        holder.descProducto.setText(lstBeanRentas.get(pos).getProducto().getDescripcion());
        holder.precio.setText("U$S " + String.valueOf(lstBeanRentas.get(pos).getProducto().getPrecio()));
        Picasso.with(getContext())
                .load(Globales.ipHost + "image?img=" + lstBeanRentas.get(pos).getUbicacion_primera_foto())
                .fit()
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
        return lstBeanRentasFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    /*Filtro*/
    public class CustomFilter extends Filter {
        private RentasAdaptador listAdapter;

        private CustomFilter(RentasAdaptador listAdapter) {
            super();
            this.listAdapter = listAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            lstBeanRentasFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                lstBeanRentasFilter.addAll(lstBeanRentas);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final RentasSendToMovil renta : lstBeanRentas) {
                    if (renta.getProducto().getNombre().toLowerCase().contains(filterPattern)) {
                        lstBeanRentasFilter.add(renta);
                    }
                }
            }
            results.values = lstBeanRentasFilter;
            results.count = lstBeanRentasFilter.size();
            Globales.lstRentasPorCategoriaAdaptador = lstBeanRentasFilter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.listAdapter.notifyDataSetChanged();
        }
    }
}
