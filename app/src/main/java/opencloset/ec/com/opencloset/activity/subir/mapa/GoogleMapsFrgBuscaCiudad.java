package opencloset.ec.com.opencloset.activity.subir.mapa;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.subir.fotos.Agrega_fotos;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class GoogleMapsFrgBuscaCiudad extends Fragment {
    // Manejador del fragmento
    private SupportMapFragment mapFragment;
    // Objeto de MapaDireccion
    private GoogleMap mMap;
    // Objeto de progreso
    private ProgressDialog progreso;
    // Arreglo con los tipos de mapas
    private static final CharSequence[] MAP_TYPE_ITEM;

    private Marker myMarker;

    private LatLng direccionInicial;
    // Objeto para manejar el apuntador a la clase padre
    private MapaDireccion mapaDireccion;

    static {
        MAP_TYPE_ITEM = new CharSequence[]{
                "Road Map", "Satellite", "Terrain", "Hybrid"
        };
    }

    private IobtieneDireccion mCallback;

    public interface IobtieneDireccion {
        void onDireccion(String direccion, String provincia, Double latitud, Double longitud);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (IobtieneDireccion) activity;
        } catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the IobtieneDireccion interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MetodoPara.permisoDeEjecucion();

        direccionInicial = new LatLng(-0.209083, -78.486857);

        Globales.direccion = "Esperando por ubicación";

        progreso = new ProgressDialog(getActivity());
        progreso.setTitle(getResources().getString(R.string.cargando_mapa));
        progreso.setMessage(getResources().getString(R.string.espere));
        progreso.setCancelable(false);
        progreso.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_mapa_direccion, container, false);
        /*
        Si el fragmento no esta creado, se invoca al manejador de fragmentos del MapaDireccion, y se
        lanza en el FrameLayout del layout que ya estaba definido para la clase MainActivity, no es
        necesario hacer otro layout adicional para esto.
         */
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                /**
                 * Este metodo se dispara despues que el MapaDireccion ya ha sido cargado
                 * @param googleMap
                 */
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            progreso.dismiss();
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    //este if te permite desplazar el marker a una nueva posicion, con un click
                                    if (myMarker != null) {
                                        myMarker.remove();
                                    }
                                    myMarker = mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .draggable(true)
                                            .title(latLng.toString()));

                                    obtieneDireccionDelMapa();
                                }
                            });
                        }
                    });
                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {
                        }

                        @Override
                        public void onMarkerDrag(Marker arg0) {
                        }

                        @Override
                        public void onMarkerDragEnd(Marker arg0) {
                            obtieneDireccionDelMapa();
                        }
                    });
                    // Se mueve la camara del MapaDireccion hacia la ubicacion, y se configura el zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(direccionInicial, 8));
                    // Se establece el tipo de visualizacion
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            });
        }
        // Para lanzar un fragmento dentro de otro
        getFragmentManager().beginTransaction().add(R.id.frmMap, mapFragment).commit();
        setHasOptionsMenu(true);

        return rootView;
    }

    private void obtieneDireccionDelMapa() {
        try {
            Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List <Address> addresses = geo.getFromLocation(myMarker.getPosition().latitude, myMarker.getPosition().longitude, 5);
            if (addresses.isEmpty()) {
                mCallback.onDireccion("Esperando por ubicación", "", 0.0, 0.0);
            } else {
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    //String city = addresses.get(0).getLocality(); // es la ciudad
                    String state = addresses.get(0).getAdminArea();  // es la provincia
                    String country = addresses.get(0).getCountryName(); // es el pais
                    if (country.equals("Ecuador"))
                        mCallback.onDireccion(address, state, myMarker.getPosition().latitude, myMarker.getPosition().longitude);
                    else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("OpenCloset")
                                .setMessage("Solo funciona a nivel de Ecuador")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Prompt the user once explanation has been shown

                                        // Create a LatLngBounds that includes the city of Adelaide in Australia.
                                        LatLngBounds ECUADOR = new LatLngBounds(
                                                new LatLng(-5.075317, -80.694020), new LatLng(1.509654, -75.058033));
                                        // Constrain the camera target to the Adelaide bounds.
                                        mMap.setLatLngBoundsForCameraTarget(ECUADOR);
                                    }
                                })
                                .create()
                                .show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }

    /**
     * Creacion del menu superior, esta usada la modalidad dinamica
     * pero los titulos estan manejados en los recursos
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(R.string.cambiar_tipo_mapa)
                .setIcon(R.mipmap.ic_show_layers)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    /**
     * Manejo de la accion del menu superior
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == null) {
            getActivity().onBackPressed();

        } else if (item.getTitle().equals(getResources().getString(R.string.cambiar_tipo_mapa))) {
            cambiarTipoMapa();

        }
        return true;
    }

    /**
     * Vinculacion del apuntador a la actividad padre
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mapaDireccion = (MapaDireccion) getActivity();
    }

    /**
     * Desvinculacion del apuntador a la actividad padre
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mapFragment = null;
        mapaDireccion = null;
    }

    /**
     * Cambia el tipo de visualizacion del MapaDireccion, escogiendo el mismo desde un
     * dialogo de seleccion simple
     */
    private void cambiarTipoMapa() {
        //System.out.println("activity.getComponentName():"+activity.getComponentName());
        final String fDialogTitle = getString(R.string.seleccione_tipo_mapa);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(fDialogTitle);
        int checkItem = mMap.getMapType() - 1;
        builder.setSingleChoiceItems(
                MAP_TYPE_ITEM,
                checkItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;
                            case 1:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                        }
                        dialog.dismiss();
                    }
                }
        );
        android.support.v7.app.AlertDialog fMapTypeDialog = builder.create();
        fMapTypeDialog.setCanceledOnTouchOutside(true);
        fMapTypeDialog.show();
    }

}