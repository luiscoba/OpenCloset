package opencloset.ec.com.opencloset.conexion;

import com.ec.deployits.ventaDeRopa.bean.BeanArriendos;
import com.ec.deployits.ventaDeRopa.bean.BeanCategoria;
import com.ec.deployits.ventaDeRopa.bean.BeanCompras;
import com.ec.deployits.ventaDeRopa.bean.BeanFotos;
import com.ec.deployits.ventaDeRopa.bean.BeanItemsProducto;
import com.ec.deployits.ventaDeRopa.bean.BeanRespuesta;
import com.ec.deployits.ventaDeRopa.bean.BeanTipoTransaccion;
import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToServer;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.global.Globales;

public class Conexion implements IConexion {

    @Override
    public BeanRespuesta inicioSesionCliente(BeanUsuario beanUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/inicioSesionCliente/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public BeanUsuario obtenerDatosDeUsuario(BeanUsuario beanUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strBeanUsuario = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerDatosDeUsuario/");

        try {
            strBeanUsuario = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strBeanUsuario, BeanUsuario.class);
    }

    @Override
    public BeanRespuesta enrolamientoCliente(BeanUsuario beanUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/enrolamientoCliente/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public BeanRespuesta recuperarCuenta(BeanUsuario beanUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/recuperarCuenta/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public List <BeanTipoTransaccion> obtenerTiposTransacion() throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strTipoTransaccion = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerTiposTransacion/");

        try {
            strTipoTransaccion = conn.invocarWS("POST", "application/json", "application/json,text/plain");
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        TypeToken <List <BeanTipoTransaccion>> token = new TypeToken <List <BeanTipoTransaccion>>() {
        };
        List <BeanTipoTransaccion> lstTipoTransaccion = new ArrayList <>();
        lstTipoTransaccion = new Gson().fromJson(strTipoTransaccion, token.getType());

        return lstTipoTransaccion;
    }

    @Override
    public List <BeanCategoria> obtenerTodasLasCategorias() throws ConexionHostExcepcion {
        String strLstBeanCategorias = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerTodasLasCategorias/");

        try {
            strLstBeanCategorias = conn.invocarWS("POST", "application/json", "application/json,text/plain");
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        TypeToken <List <BeanCategoria>> token = new TypeToken <List <BeanCategoria>>() {
        };

        return new Gson().fromJson(strLstBeanCategorias, token.getType());
    }

    @Override
    public List <RentasSendToMovil> obtenerTodasLasRentas() throws ConexionHostExcepcion {

        String strLstBeanRentas = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerTodasLasRentas/");

        try {
            strLstBeanRentas = conn.invocarWS("POST", "application/json", "application/json,text/plain");
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        TypeToken <List <RentasSendToMovil>> token = new TypeToken <List <RentasSendToMovil>>() {
        };

        return new Gson().fromJson(strLstBeanRentas, token.getType());
    }

    @Override
    public List <VentasSendToMovil> obtenerTodasLasVentas() throws ConexionHostExcepcion {

        String strLstBeanVentas = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerTodasLasVentas/");

        try {
            strLstBeanVentas = conn.invocarWS("POST", "application/json", "application/json,text/plain");
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        TypeToken <List <VentasSendToMovil>> token = new TypeToken <List <VentasSendToMovil>>() {
        };

        return new Gson().fromJson(strLstBeanVentas, token.getType());
    }

    @Override
    public List <BeanCompras> obtenerTodasLasCompras(Integer idUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strListaCompras = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerTodasLasCompras/");
        try {
            strListaCompras = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(idUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }
        TypeToken <List <BeanCompras>> token = new TypeToken <List <BeanCompras>>() {
        };
        ArrayList <BeanCompras> lstCompras = new ArrayList <>();
        lstCompras = new Gson().fromJson(strListaCompras, token.getType());

        return lstCompras;
    }

    @Override
    public List <BeanArriendos> obtenerTodosLosArriendos(Integer idUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strListaArriendos = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerTodosLosArriendos/");
        try {
            strListaArriendos = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(idUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }
        TypeToken <List <BeanArriendos>> token = new TypeToken <List <BeanArriendos>>() {
        };
        ArrayList <BeanArriendos> lstArriendos = new ArrayList <>();
        lstArriendos = new Gson().fromJson(strListaArriendos, token.getType());

        return lstArriendos;
    }

    @Override
    public ArrayList <BeanFotos> obtenerFotosDelProducto(Integer idProducto) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strListaDeFotos = "";
        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerFotosDelProducto/");
        try {
            strListaDeFotos = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(idProducto));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            throw new ConexionHostExcepcion("Error de conexión al servidor");
        }
        TypeToken <List <BeanFotos>> token = new TypeToken <List <BeanFotos>>() {
        };
        ArrayList <BeanFotos> lstFotos = new ArrayList <>();
        lstFotos = new Gson().fromJson(strListaDeFotos, token.getType());

        return lstFotos;
    }

    @Override
    public BeanUsuario obtenerUsuarioVendedor(Integer idProducto) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strBeanVendedor = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerUsuarioVendedor/");

        try {
            strBeanVendedor = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(idProducto));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strBeanVendedor, BeanUsuario.class);
    }

    @Override
    public BeanUsuario obtenerUsuarioRentador(Integer idProducto) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strBeanRentador = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerUsuarioRentador/");

        try {
            strBeanRentador = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(idProducto));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strBeanRentador, BeanUsuario.class);
    }

    @Override
    public BeanRespuesta comprar(BeanCompras beanCompras) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/comprar/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanCompras));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public BeanRespuesta arrendar(BeanArriendos beanArriendos) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/arrendar/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanArriendos));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public BeanRespuesta subirVenta(VentasSendToServer beanVentas) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/subirVenta/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanVentas));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public BeanRespuesta subirRenta(RentasSendToServer beanRentas) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/subirRenta/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanRentas));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

    @Override
    public BeanItemsProducto obtenerItemsDelProducto() throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strBeanItemsProducto = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/obtenerItemsDelProducto/");

        try {
            strBeanItemsProducto = conn.invocarWS("POST", "application/json", "application/json,text/plain");
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strBeanItemsProducto, BeanItemsProducto.class);
    }

    @Override
    public BeanRespuesta completaRegistroFaltante(BeanUsuario beanUsuario) throws ConexionHostExcepcion {
        Gson gson = new Gson();
        String strRespuesta = "";

        ConexionRest conn = new ConexionRest(Globales.ipHost + "core/servicio/completaRegistroFaltante/");

        try {
            strRespuesta = conn.invocarWS("POST", "application/json", "application/json,text/plain", gson.toJson(beanUsuario));
        } catch (ConexionHostExcepcion conexionHostExcepcion) {
            conexionHostExcepcion.printStackTrace();
        }

        return gson.fromJson(strRespuesta, BeanRespuesta.class);
    }

}
