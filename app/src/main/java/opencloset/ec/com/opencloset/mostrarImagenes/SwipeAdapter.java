package opencloset.ec.com.opencloset.mostrarImagenes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ec.deployits.ventaDeRopa.bean.BeanFotos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.global.Globales;

public class SwipeAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList <BeanFotos> lstFotos;

    public SwipeAdapter(Context context, ArrayList <BeanFotos> lstFotos) {
        this.context = context;
        this.lstFotos = lstFotos;
    }

    @Override
    public int getCount() {
        return lstFotos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);
        ImageView imageView = item_view.findViewById(R.id.image_view);

        Picasso.with(context)
                .load(Globales.ipHost + "image?img=" + lstFotos.get(position).getNombre())
                .into(imageView);

        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
