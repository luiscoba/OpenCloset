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

import java.util.ArrayList;
import java.util.List;

import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;

public interface IConexion {

    BeanRespuesta inicioSesionCliente(BeanUsuario beanUsuario ) throws ConexionHostExcepcion;

    BeanUsuario obtenerDatosDeUsuario(BeanUsuario beanUsuario) throws ConexionHostExcepcion;

    BeanRespuesta enrolamientoCliente(BeanUsuario beanUsuario) throws ConexionHostExcepcion;

    BeanRespuesta recuperarCuenta(BeanUsuario beanUsuario) throws ConexionHostExcepcion;

    List<BeanTipoTransaccion> obtenerTiposTransacion() throws ConexionHostExcepcion;

    List<BeanCategoria> obtenerTodasLasCategorias() throws ConexionHostExcepcion;

    List<RentasSendToMovil> obtenerTodasLasRentas() throws ConexionHostExcepcion;

    List<VentasSendToMovil> obtenerTodasLasVentas() throws ConexionHostExcepcion;

    List<BeanCompras> obtenerTodasLasCompras(Integer idUsuario) throws ConexionHostExcepcion;

    List<BeanArriendos> obtenerTodosLosArriendos(Integer idUsuario) throws ConexionHostExcepcion;

    ArrayList<BeanFotos> obtenerFotosDelProducto(Integer idProducto) throws ConexionHostExcepcion;

    BeanUsuario obtenerUsuarioVendedor(Integer idProducto) throws  ConexionHostExcepcion;

    BeanUsuario obtenerUsuarioRentador(Integer idProducto) throws  ConexionHostExcepcion;

    BeanRespuesta comprar(BeanCompras beanCompras) throws ConexionHostExcepcion;

    BeanRespuesta arrendar(BeanArriendos beanArriendos) throws ConexionHostExcepcion;

    BeanRespuesta subirVenta(VentasSendToServer beanVentas) throws ConexionHostExcepcion;

    BeanRespuesta subirRenta(RentasSendToServer beanRentas) throws ConexionHostExcepcion;

    BeanItemsProducto obtenerItemsDelProducto() throws  ConexionHostExcepcion;

    BeanRespuesta completaRegistroFaltante(BeanUsuario beanUsuario) throws ConexionHostExcepcion;

}
