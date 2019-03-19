package opencloset.ec.com.opencloset.utilidad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class LocalizacionDispositivo {
    private LocationManager locationManager;
    private Location localizacion;
    private Context context;
    private Activity activity;

    public LocalizacionDispositivo(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void calcularLocalizacion() {
        checkLocationPermission();
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        System.out.println("bestProvider: " + bestProvider);
        if (bestProvider == null) {

        } else {
            localizacion = locationManager.getLastKnownLocation(bestProvider);
            if (localizacion == null) {
                localizacion = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                System.out.println("localizacion is null");
            } else {
                System.out.println("localizacion NOT null");
            }
        }
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public Location getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Location localizacion) {
        this.localizacion = localizacion;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle("Permiso de localización")
                        .setMessage("¿Desea que avispa-t le muestre promociones cercanas?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
}
