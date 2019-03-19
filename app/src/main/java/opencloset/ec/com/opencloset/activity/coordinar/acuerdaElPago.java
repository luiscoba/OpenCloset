package opencloset.ec.com.opencloset.activity.coordinar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ec.deployits.ventaDeRopa.bean.BeanArriendos;
import com.ec.deployits.ventaDeRopa.bean.BeanCompras;
import com.ec.deployits.ventaDeRopa.bean.BeanProducto;
import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;

import java.text.SimpleDateFormat;
import java.util.Date;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.sesion.LlenarRegistroFaltante;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;

public class acuerdaElPago extends AppCompatActivity {

    private Conexion conexion = new Conexion();
    private BeanRespuesta respuesta;
    private Bundle bundle;
    private BeanUsuario usuarioVendedor, usuarioRentador;
    private Integer idProducto, cantidad_comprada, cantidad_rentada;
    private int idTipoTransaccion;
    private String strImagenProducto;

    private SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acuerda_el_pago);


        bundle = getIntent().getExtras();
        idTipoTransaccion =  bundle.getInt("idTipoTransaccion");

        if (idTipoTransaccion == Globales.tipo_venta) {
            usuarioVendedor = (BeanUsuario) bundle.getSerializable("usuarioVendedor");
            idProducto = bundle.getInt("idProducto");
            cantidad_comprada = bundle.getInt("cantidad_comprada");
            strImagenProducto = bundle.getString("strImagenProducto");
        }

        if (idTipoTransaccion == Globales.tipo_renta) {
            usuarioRentador = (BeanUsuario) bundle.getSerializable("usuarioRentador");
            idProducto = bundle.getInt("idProducto");
            cantidad_rentada = bundle.getInt("cantidad_rentada");
            strImagenProducto = bundle.getString("strImagenProducto");
        }

    }

    public void onClickEntendido(View view) {
        BeanUsuario bUsuarioMovil = new BeanUsuario();
        bUsuarioMovil.setId_usuario(Globales.beanUsuario.getId_usuario());

        if (idTipoTransaccion == Globales.tipo_venta) {
            BeanCompras compra = new BeanCompras();
            compra.setUsuario_movil(bUsuarioMovil);
            compra.setId_usuario_vendedor(usuarioVendedor.getId_usuario());
            compra.setFecha_de_compra(formato.format(new Date()));
            compra.setObservacion("Póngase en contacto con el Vendedor");
            compra.setCantidad_comprada(cantidad_comprada);
            BeanProducto bProducto = new BeanProducto();
            bProducto.setId_producto(idProducto);
            compra.setProducto(bProducto);

            try {
                respuesta = conexion.comprar(compra);

            } catch (ConexionHostExcepcion conexionHostExcepcion) {
                conexionHostExcepcion.printStackTrace();
            }

            if (respuesta.getCodigoError() == 20) {//osea no tiene registrado el celular
                Intent irACompletarRegistroComprador = new Intent(this, LlenarRegistroFaltante.class);
                startActivity(irACompletarRegistroComprador);
            } else {
                finish();

                Intent intent = new Intent(this, pagoVendedor.class);
                intent.putExtra("usuarioVendedor", usuarioVendedor);
                intent.putExtra("strImagenProducto", strImagenProducto);
                startActivity(intent);
            }
        }

        if (idTipoTransaccion == Globales.tipo_renta) {
            BeanArriendos arriendo = new BeanArriendos();
            arriendo.setUsuario_movil(bUsuarioMovil);
            arriendo.setId_usuario_arrendatario(usuarioRentador.getId_usuario());
            arriendo.setFecha_de_renta(formato.format(new Date()));
            arriendo.setObservacion("Póngase en contacto con el Rentador");
            arriendo.setCantidad_arrendada(cantidad_rentada);
            BeanProducto bProducto = new BeanProducto();
            bProducto.setId_producto(idProducto);
            arriendo.setProducto(bProducto);

            try {
                respuesta = conexion.arrendar(arriendo);

                System.out.println("beanRespuesta " + respuesta);

            } catch (ConexionHostExcepcion conexionHostExcepcion) {
                conexionHostExcepcion.printStackTrace();
            }

            if (respuesta.getCodigoError() == 10) {//osea no tiene registrado el celular
                Intent irACompletarRegistroArrendador = new Intent(this, LlenarRegistroFaltante.class);
                startActivity(irACompletarRegistroArrendador);
            } else {
                finish();

                Intent intent = new Intent(this, pagoRentador.class);
                intent.putExtra("usuarioRentador", usuarioRentador);
                intent.putExtra("strImagenProducto", strImagenProducto);
                startActivity(intent);
            }
        }
    }

}