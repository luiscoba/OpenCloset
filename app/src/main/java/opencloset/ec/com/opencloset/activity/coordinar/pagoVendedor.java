package opencloset.ec.com.opencloset.activity.coordinar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;
import com.squareup.picasso.Picasso;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class pagoVendedor extends AppCompatActivity {

    private TextView nombre, telefonos, correo;
    private ImageView imagenProducto;
    private BeanUsuario vendedor;
    private MensajeSistema mensaje;

    private String direccion_foto_producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_vendedor);

        mensaje= new MensajeSistema(this);

        imagenProducto = findViewById(R.id.imgProducto);
        nombre = findViewById(R.id.txtNombreVendedor);
        telefonos = findViewById(R.id.txtTelf);
        correo = findViewById(R.id.txtCorreo);

        Bundle bundle = getIntent().getExtras();
        vendedor = (BeanUsuario) bundle.get("usuarioVendedor");
        direccion_foto_producto = bundle.getString("strImagenProducto");

        System.out.println("direccion foto producto "+ direccion_foto_producto);

        Picasso.with(this)
                .load(direccion_foto_producto)
                .into(imagenProducto);
        nombre.setText(vendedor.getNombre()+ " "+ vendedor.getApellido());
        telefonos.setText(vendedor.getCelular() + " , " + vendedor.getConvencional());
        correo.setText(vendedor.getCorreo());

        mensaje.mostrarMensajeToastActivity("Revise su compra en su correo");
    }

}
