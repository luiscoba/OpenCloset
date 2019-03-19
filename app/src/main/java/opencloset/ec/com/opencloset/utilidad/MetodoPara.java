package opencloset.ec.com.opencloset.utilidad;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.ec.deployits.ventaDeRopa.bean.BeanCategoria;
import com.ec.deployits.ventaDeRopa.bean.BeanCompras;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;

import static android.content.Context.MODE_PRIVATE;

public class MetodoPara {/* - - Deployits Julio 2018 - - */

    static Conexion conexion = new Conexion();

    /* MetodoPara que concede acceso a las politicas */
    public static void permisoDeEjecucion() {
        //permisos para la ejecucion de hilos
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    //I check for both Wi-fi and Mobile internet as follows
    public static boolean haveNetworkConnection(Activity activity) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isConnected(Context context) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); //= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //- - Preferencias para guardarlas en el telefono

    /* MetodoParas para guardar y leer valores que son Preferencias, para que no se logue nuevamente */
    private static String PREFS_KEY = "mispreferencias";

    public static void guardarValor(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }

    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return preferences.getString(keyPref, "");
    }

    public static void guardarValorBool(Context context, String keyPref, boolean valor) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor;
        editor = prefs.edit();
        editor.putBoolean(keyPref, valor);
        editor.commit();
    }

    @NonNull
    public static boolean leerValorBool(Context context, String keyPref) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(keyPref, false);
        //       Boolean yourLocked = prefs.getBoolean("locked", false);
    }

    ////////////////////////

    public static BeanUsuario traerPreferenciaBeanUsuario(Context context) {

        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> TRAER PREFERENCIAS BEANUSUARIO ");
        System.out.println("______________________________________________________________________________________");

        BeanUsuario beanUsuario = new BeanUsuario();
        //si trae el idUsuario significa que se el usuario se logueo alguna vez
        String idUsuarioStr = leerValor(context, "idUsuario");

        if (idUsuarioStr == "")

            return new BeanUsuario();//el usuario no se a logueado por eso se retorna null en beanUsuario

        else {
            Integer idUsuario = Integer.parseInt(idUsuarioStr);
            String correo = leerValor(context, "correo");
            String nombre = leerValor(context, "nombre");
            String apellido = leerValor(context, "apellido");
            String domicilio = leerValor(context, "domicilio");
            String celular = leerValor(context, "celular");
            String convencional = leerValor(context, "convencional");
            String clave = leerValor(context, "password");
            String direccion_foto = leerValor(context, "direccion_foto");

            beanUsuario.setId_usuario(idUsuario);
            beanUsuario.setCorreo(correo);
            beanUsuario.setNombre(nombre);
            beanUsuario.setApellido(apellido);
            beanUsuario.setDomicilio(domicilio);
            beanUsuario.setCelular(celular);
            beanUsuario.setConvencional(convencional);
            beanUsuario.setClave(clave);
            beanUsuario.setDireccion_foto(direccion_foto);

            System.out.println("beanUsuario " + beanUsuario);

            return beanUsuario;
        }

    }

    public static void ponerBotonFlotante(View view, final FragmentActivity activity) {

        Globales.regresoMainActivityBtnFlotante = true;

        Globales.fab = view.findViewById(R.id.fab);
        Globales.fab.setVisibility(View.VISIBLE);
/*
        System.out.println("public static void ponerBotonFlotante");
        for (BeanPromocion cupon : Globales.lstCuponesPorCategoriaAdaptador) {
            System.out.println("empresa " + cupon.getNombreEmpresa() + "  idPromocion " + cupon.getIdPromocion());
        }

        Globales.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Inicio de la app", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //metodo para vaciar los fragments, es decir eliminar los fragments
                FragmentManager fm = activity.getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                Globales.tabLayout.setVisibility(View.VISIBLE);
                Globales.spinner.setVisibility(View.VISIBLE);
            }
        });
*/
    }

    //son ventas que se muestran en la aplicación
    public static void cargarListaVentas() {

        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> CARGA LISTA DE VENTAS ");
        System.out.println("______________________________________________________________________________________");

        Globales.lstVentasPorCategoriaAdaptador = new ArrayList <VentasSendToMovil>();

        try {
            Globales.lstVentasPorCategoriaAdaptador = conexion.obtenerTodasLasVentas();
            System.out.println("Globales.lstVentasAdaptador " + Globales.lstVentasPorCategoriaAdaptador);
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        Globales.lstVentasSistema = new ArrayList <VentasSendToMovil>();
        if (Globales.lstVentasPorCategoriaAdaptador != null)
            for (VentasSendToMovil venta : Globales.lstVentasPorCategoriaAdaptador) {
                Globales.lstVentasSistema.add(venta);
            }
    }

    public static void cargarListaRentas() {
        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> CARGA LISTA DE RENTAS ");
        System.out.println("______________________________________________________________________________________");

        Globales.lstRentasPorCategoriaAdaptador = new ArrayList <>();
        try {
            Globales.lstRentasPorCategoriaAdaptador = conexion.obtenerTodasLasRentas();
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        Globales.lstRentasSistema = new ArrayList <>();
        if (Globales.lstRentasPorCategoriaAdaptador != null) {
            for (RentasSendToMovil renta : Globales.lstRentasPorCategoriaAdaptador) {
                Globales.lstRentasSistema.add(renta);
            }
        }
    }
    //son las compras que ha realizado el cliente(usuario)
    public static void cargarListaCompras() {
        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> CARGA LISTA DE COMPRAS ");
        System.out.println("______________________________________________________________________________________");

        Globales.lstComprasPorCategoriaAdaptador = new ArrayList <>();
        try {
            System.out.println("Globales.beanUsuario.getId_usuario() " + Globales.beanUsuario.getId_usuario());
            Globales.lstComprasPorCategoriaAdaptador = conexion.obtenerTodasLasCompras(Globales.beanUsuario.getId_usuario());
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        Globales.lstComprasSistema = new ArrayList <>();
        if (Globales.lstComprasPorCategoriaAdaptador != null)
            for (BeanCompras compra : Globales.lstComprasPorCategoriaAdaptador) {
                Globales.lstComprasSistema.add(compra);
            }

    }


    public static void cargarListaCategorias() {

        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> CARGA LISTA DE CATEGORIAS ");
        System.out.println("______________________________________________________________________________________");

        Conexion conexion = new Conexion();
        Globales.lstCategorias = new ArrayList <>();

        try {
            Globales.lstCategorias = conexion.obtenerTodasLasCategorias();
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            try {
                Globales.lstCategorias = conexion.obtenerTodasLasCategorias();
            } catch (ConexionHostExcepcion conexionHostExcepcion1) {
                conexionHostExcepcion1.printStackTrace();
            }
        }

        Globales.lstCategoriasStrSpinner = new ArrayList <>();
        // carga de la lista de categoriasStr que sirve para cargar el Spinner de categorias
        for (BeanCategoria categoria : Globales.lstCategorias) {
            Globales.lstCategoriasStrSpinner.add(categoria.getNombre());
        }// agregamos en la posicion 0, la palabra "todas las categorias"

        Globales.lstCategoriasStrSpinner.add(0, Globales.item_Spinner_todas_las_categorias); //se inserta en la primera posicion
        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> FIN CARGA LISTA DE CATEGORIAS ");
        System.out.println("______________________________________________________________________________________");

    }

    public static void ordenarCuponesComerciosPorCategorias() {
/*
        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> ORDENA LISTA DE CATEGORIAS ");
        System.out.println("______________________________________________________________________________________");

        System.out.println("ANTES");
        for (BeanSucursal sucursal : Globales.lstComerciosPorCategoriaAdaptador) {
            System.out.println("idSucursal " + sucursal.getIdSucursal() + " favorita " + sucursal.getCategoriaFavorita());
        }

        //se realiza la ORDENACION de lstComerciosPorCategoriaAdaptador
        Comparator <BeanSucursal> c2 = new Comparator <BeanSucursal>() {
            public int compare(BeanSucursal p1, BeanSucursal p2) {//se multiplica por -1 para orden inverso
                return (-1) * p1.getCategoriaFavorita().compareTo(p2.getCategoriaFavorita());
            }
        };
        Collections.sort(Globales.lstComerciosPorCategoriaAdaptador, c2);

        System.out.println("DESPUES");
        for (BeanSucursal sucursal : Globales.lstComerciosPorCategoriaAdaptador) {
            System.out.println("idSucursal " + sucursal.getIdSucursal() + " favorita " + sucursal.getCategoriaFavorita());
        }


        //ordenamos las Promociones
        Comparator <BeanPromocion> c1 = new Comparator <BeanPromocion>() {
            public int compare(BeanPromocion p1, BeanPromocion p2) {
                return (-1) * p1.getCategoriaFavorita().compareTo(p2.getCategoriaFavorita());
            }
        };
        Collections.sort(Globales.lstCuponesPorCategoriaAdaptador, c1);
     */
    }


    public static void cargarValoresDelSistema() {

        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> CARGA VENTAS Y RENTAS AL SISTEMA ");
        System.out.println("______________________________________________________________________________________");

        cargarLstAdaptadores();
    }

    public static void cargarLstAdaptadores() {
        // carga las listas de ventas y rentas a sus respectivos adaptadores

        // se asigna lstTodasLasVentas a lstVentasPorCategoriaAdaptador
        Globales.lstVentasPorCategoriaAdaptador = new ArrayList <>();
        if (Globales.lstVentasSistema != null)
            for (VentasSendToMovil venta : Globales.lstVentasSistema) {
                Globales.lstVentasPorCategoriaAdaptador.add(venta);
            }
        // se asigna lstTodasLasRentas a lstRentasPorCategoriaAdaptador
        Globales.lstRentasPorCategoriaAdaptador = new ArrayList <>();
        if (Globales.lstRentasSistema != null)
            for (RentasSendToMovil renta : Globales.lstRentasSistema) {
                Globales.lstRentasPorCategoriaAdaptador.add(renta);
            }
    }

    public static void guardarPreferenciaBeanUsuario(Context context) {

        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> GUARDAR PREFERENCIAS beanUsuario ");
        System.out.println("______________________________________________________________________________________");

        guardarValor(context, "idUsuario", String.valueOf(Globales.beanUsuario.getId_usuario()));
        System.out.println("Globales.beanUsuario.getIdUsuario() " + Globales.beanUsuario.getId_usuario());

        guardarValor(context, "correo", Globales.beanUsuario.getCorreo());
        System.out.println("Globales.beanUsuario.getCorreo() " + Globales.beanUsuario.getCorreo());

        guardarValor(context, "nombre", Globales.beanUsuario.getNombre());
        System.out.println("Globales.beanUsuario.getNombre() " + Globales.beanUsuario.getNombre());

        guardarValor(context, "apellido", Globales.beanUsuario.getApellido());
        System.out.println("Globales.beanUsuario.getApellido() " + Globales.beanUsuario.getApellido());

        guardarValor(context, "correo", Globales.beanUsuario.getDomicilio());
        System.out.println("Globales.beanUsuario.getDomicilio() " + Globales.beanUsuario.getDomicilio());

        guardarValor(context, "telefono", Globales.beanUsuario.getCelular());
        System.out.println("Globales.beanUsuario.getCelular() " + Globales.beanUsuario.getCelular());

        guardarValor(context, "correo", Globales.beanUsuario.getConvencional());
        System.out.println("Globales.beanUsuario.getConvencional() " + Globales.beanUsuario.getConvencional());

        guardarValor(context, "password", Globales.beanUsuario.getClave());
        System.out.println("Globales.beanUsuario.getClave() " + Globales.beanUsuario.getClave());

        guardarValor(context, "domicilio", Globales.beanUsuario.getDireccion_foto());
        System.out.println("Globales.beanUsuario.getDireccion_foto() " + Globales.beanUsuario.getDireccion_foto());
    }
    // ----------->  -----------> -----------> -----------> ----------->

    /**
     * Dialogo basico de alerta que solo tiene un boton y que se le puede pasar el titulo y el contenido
     *
     * @param context Contexto en el que se va a mostrar
     * @param titulo  Titulo del dialogo
     * @param cadena  Contenido del dialogo
     */
    public static void alerta(Context context, String titulo, String cadena) {
        new AlertDialog.Builder(context)
                .setMessage(cadena)
                .setCancelable(true).setTitle(titulo)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
