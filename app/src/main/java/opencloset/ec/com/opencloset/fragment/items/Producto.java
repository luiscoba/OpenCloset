package opencloset.ec.com.opencloset.fragment.items;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.deployits.ventaDeRopa.bean.BeanUsuario;
import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.coordinar.acuerdaElPago;
import opencloset.ec.com.opencloset.conexion.Conexion;
import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;
import opencloset.ec.com.opencloset.fragment.dialogs.DeseaIniciarSesion;
import opencloset.ec.com.opencloset.fragment.items.detalles.MultipleItems;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;

public class Producto extends Fragment {

    private TextView finaliza_en, nombre_producto, precio, descripcion;

    private Button comprar, arrendar;
    private Spinner cantidad_comprada, cantidad_rentada;

    private VentasSendToMovil venta;
    private RentasSendToMovil renta;
    private MensajeSistema mensaje;

    private String strImagenProducto;
    private int cantidad_elegida;

    private EditText input;

    private ArrayAdapter <String> adapter;
    ArrayList <String> spinnerArray;

    private boolean validaInformacion() {
        boolean validaInformacion = true;
        input.setError(null);

        if (cantidad_elegida < 7) {
            input.setError(getString(R.string.errorCantidadMenorAsiete));
            input.requestFocus();
            validaInformacion = false;
        }
        return validaInformacion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detalle_producto, container, false);

        MultipleItems activity = (MultipleItems) getActivity();
        Bundle bundle = activity.getDatos();

        mensaje = new MensajeSistema(getContext());

        spinnerArray = new ArrayList <>();
        finaliza_en = (TextView) view.findViewById(R.id.txtFinalizaEn);
        nombre_producto = (TextView) view.findViewById(R.id.txtNomProd);
        precio = (TextView) view.findViewById(R.id.etxtPrecio);
        descripcion = (TextView) view.findViewById(R.id.txtDescripcion);

        cantidad_comprada = (Spinner) view.findViewById(R.id.spnCantidad);
        cantidad_rentada = (Spinner) view.findViewById(R.id.spnCantidad);

        strImagenProducto = bundle.getString("strImagenProducto");

        final int idTipoTransaccion = bundle.getInt("idTipoTransaccion");

        if (idTipoTransaccion == Globales.tipo_venta) {

            venta = (VentasSendToMovil) bundle.get("venta");
            spinnerArray = cargaSpinnerCantidad(venta.getProducto().getStock());
            adapter = new ArrayAdapter <String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setNotifyOnChange(true);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cantidad_comprada.setAdapter(adapter);

            cantidad_comprada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView <?> parentView, View selectedItemView, int position, long id) {
                    if (cantidad_comprada.getSelectedItem().toString().equals("Más de 6 unidades")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        // Set up the input
                        input = new EditText(getContext());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setGravity(Gravity.CENTER_HORIZONTAL);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        alert.setView(input);
                        alert.setTitle("Stop On The Go!");
                        alert.setMessage("Escribe la cantidad_elegida de producto");
                        alert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().trim().isEmpty())
                                            cantidad_comprada.setSelection(0);
                                        else {
                                            cantidad_elegida = Integer.parseInt(input.getText().toString());
                                            if (!validaInformacion()) {
                                                cantidad_comprada.setSelection(cantidad_elegida - 1);
                                            } else {
                                                if (cantidad_elegida < venta.getProducto().getStock()) {
                                                    if (spinnerArray.size() == 8)
                                                        spinnerArray.remove(7);

                                                    spinnerArray.add(input.getText().toString());
                                                    adapter.notifyDataSetChanged();
                                                    cantidad_comprada.setSelection(7);
                                                } else {
                                                    Toast.makeText(getContext(), "cantidad_elegida sobrepasa el stock", Toast.LENGTH_LONG).show();
                                                    cantidad_comprada.setSelection(0);
                                                }
                                            }
                                        }
                                    }
                                });
                        alert.setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cantidad_comprada.setSelection(0);
                                    }
                                });

                        alert.show();
                    } else {
                        cantidad_elegida = cantidad_comprada.getSelectedItemPosition() + 1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView <?> parentView) {
                    // your code here
                }

            });

            finaliza_en.setText("Finaliza en " + venta.getTransaccion().getFecha_fin());
            nombre_producto.setText(venta.getProducto().getNombre());
            precio.setText("U$S " + String.valueOf(venta.getProducto().getPrecio()));
            descripcion.setText(venta.getProducto().getDescripcion());

            comprar = view.findViewById(R.id.btnTransaccion);
            comprar.setText("COMPRAR");
            comprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Conexion conexion = new Conexion();

                    if (Globales.beanUsuario.getCorreo() == null) {//si su correo es null
                        DialogFragment deseaIniciarSesionDialogFragment = new DeseaIniciarSesion();
                        deseaIniciarSesionDialogFragment.setCancelable(false);
                        deseaIniciarSesionDialogFragment.show(getFragmentManager(), "dialogoParaPreguntarSiIniciaSesion");
                    } else {
                        try {
                            BeanUsuario usuarioVendedor = conexion.obtenerUsuarioVendedor(venta.getProducto().getId_producto());

                            if (usuarioVendedor.getId_usuario() == Globales.beanUsuario.getId_usuario())
                                mensaje.mostrarMensajeToastContext("Ud. es el vendedor este articulo");
                            else {
                                Intent intent = new Intent(getContext(), acuerdaElPago.class);
                                intent.putExtra("usuarioVendedor", usuarioVendedor);
                                intent.putExtra("idProducto", venta.getProducto().getId_producto());
                                intent.putExtra("idTipoTransaccion", idTipoTransaccion);
                                System.out.println("cantidad_comprada.getSelectedItem().toString() -> -> " + cantidad_comprada.getSelectedItem().toString());
                                intent.putExtra("cantidad_comprada", cantidad_elegida);
                                intent.putExtra("strImagenProducto", strImagenProducto);
                                startActivity(intent);
                            }

                        } catch (ConexionHostExcepcion conexionHostExcepcion) {
                            conexionHostExcepcion.printStackTrace();
                        }
                    }
                }
            });
        }
        // -- SECCIÓN RENTAR
        if (idTipoTransaccion == Globales.tipo_renta) {
            renta = (RentasSendToMovil) bundle.get("renta");
            spinnerArray = cargaSpinnerCantidad(renta.getProducto().getStock());
            adapter = new ArrayAdapter <String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setNotifyOnChange(true);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cantidad_rentada.setAdapter(adapter);

            cantidad_rentada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView <?> parentView, View selectedItemView, int position, long id) {
                    if (cantidad_rentada.getSelectedItem().toString().equals("Más de 6 unidades")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        // Set up the input
                        input = new EditText(getContext());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setGravity(Gravity.CENTER_HORIZONTAL);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        alert.setView(input);
                        alert.setTitle("Stop On The Go!");
                        alert.setMessage("Escribe la cantidad_elegida de producto");
                        alert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().trim().isEmpty())
                                            cantidad_rentada.setSelection(0);
                                        else {
                                            cantidad_elegida = Integer.parseInt(input.getText().toString());
                                            if (!validaInformacion()) {
                                                cantidad_rentada.setSelection(cantidad_elegida - 1);
                                            } else {
                                                if (cantidad_elegida < renta.getProducto().getStock()) {
                                                    if (spinnerArray.size() == 8)
                                                        spinnerArray.remove(7);

                                                    spinnerArray.add(input.getText().toString());
                                                    adapter.notifyDataSetChanged();
                                                    cantidad_rentada.setSelection(7);
                                                } else {
                                                    Toast.makeText(getContext(), "cantidad_elegida sobrepasa el stock", Toast.LENGTH_LONG).show();
                                                    cantidad_rentada.setSelection(0);
                                                }
                                            }
                                        }
                                    }
                                });
                        alert.setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cantidad_rentada.setSelection(0);
                                    }
                                });

                        alert.show();
                    } else {
                        cantidad_elegida = cantidad_rentada.getSelectedItemPosition() + 1;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView <?> parentView) {
                    // your code here
                }
            });

            finaliza_en.setText("Finaliza en " + renta.getTransaccion().getFecha_fin());
            nombre_producto.setText(renta.getProducto().getNombre());
            precio.setText("U$S " + String.valueOf(renta.getProducto().getPrecio()));
            descripcion.setText(renta.getProducto().getDescripcion());

            arrendar = view.findViewById(R.id.btnTransaccion);
            arrendar.setText("ARRENDAR");
            arrendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Conexion conexion = new Conexion();

                    if (Globales.beanUsuario.getCorreo() == null) {//si su correo es null
                        DialogFragment deseaIniciarSesionDialogFragment = new DeseaIniciarSesion();
                        deseaIniciarSesionDialogFragment.setCancelable(false);
                        deseaIniciarSesionDialogFragment.show(getFragmentManager(), "dialogoParaPreguntarSiIniciaSesion");
                    } else {
                        try {
                            BeanUsuario usuarioRentador = conexion.obtenerUsuarioRentador(renta.getProducto().getId_producto());

                            if (usuarioRentador.getId_usuario() == Globales.beanUsuario.getId_usuario())
                                mensaje.mostrarMensajeToastContext("Ud. es el rentador este articulo");
                            else {
                                Intent intent = new Intent(getContext(), acuerdaElPago.class);
                                intent.putExtra("usuarioRentador", usuarioRentador);
                                intent.putExtra("idProducto", renta.getProducto().getId_producto());
                                intent.putExtra("idTipoTransaccion", idTipoTransaccion);
                                intent.putExtra("cantidad_rentada", cantidad_elegida);
                                intent.putExtra("strImagenProducto", strImagenProducto);
                                startActivity(intent);
                            }

                        } catch (ConexionHostExcepcion conexionHostExcepcion) {
                            conexionHostExcepcion.printStackTrace();
                        }
                    }
                }
            });
        }

        return view;
    }

    public ArrayList cargaSpinnerCantidad(Integer stock) {
        ArrayList <String> arrayCantidad = new ArrayList <>();
        if (stock >= 1) {
            arrayCantidad.add("1 unidad");
            if (stock > 2) {
                for (int i = 1; i < stock; i++) {
                    arrayCantidad.add(i + 1 + " unidades");
                    if (i + 1 == 6)//se termina en 6
                        i = stock + 1;
                }
                if (stock >= 7) {
                    arrayCantidad.add("Más de 6 unidades");

                    return arrayCantidad;
                }
            }
        }
        return arrayCantidad;
    }
}
