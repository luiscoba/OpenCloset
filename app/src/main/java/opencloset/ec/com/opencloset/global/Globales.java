package opencloset.ec.com.opencloset.global;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.widget.Spinner;

import com.ec.deployits.ventaDeRopa.bean.BeanCategoria;
import com.ec.deployits.ventaDeRopa.bean.BeanCompras;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Globales {

    public static BeanUsuario beanUsuario;

    public static Integer tipo_venta = 1;
    public static Integer tipo_renta = 2;

    public static List <BeanCategoria> lstCategorias;
    public static List <String> lstCategoriasStrSpinner;// lista de categorias que se muestra en el Spinner

    public static List <VentasSendToMovil> lstVentasSistema;
    public static List <RentasSendToMovil> lstRentasSistema;
    public static List <BeanCompras> lstComprasSistema;

    public static List <VentasSendToMovil> lstVentasPorCategoriaAdaptador;
    public static List <RentasSendToMovil> lstRentasPorCategoriaAdaptador;
    public static List <BeanCompras> lstComprasPorCategoriaAdaptador;

    public static String item_Spinner_todas_las_categorias = "Todas las categorias";

    public static TabLayout tabLayout;
    public static Spinner spinner;
    public static FloatingActionButton fab;

    public static boolean regresoMainActivityBtnFlotante;
    public static boolean regresoDesdeSinConexionActivity;

    public static boolean seActivoLogin;

    public static String politica_de_privacidad;

    public static Map <String, Integer> mapTransaccion = new HashMap <String, Integer>();
    public static Integer idTipoTransaccion;

    public static String condicion_nuevo = "articulo nuevo";
    public static String condicion_usado = "articulo usado";

    public static String direccion;
    public static int numFotos;

//    public static String ipHost = "http://186.4.165.101:8080/OpenClose/";  // con esta salimos a internet
       public static String ipHost = "http://192.168.100.110:8080/OpenClose/"; // mi máquina

}
