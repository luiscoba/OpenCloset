package opencloset.ec.com.opencloset.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ec.deployits.ventaDeRopa.bean.RentasSendToMovil;
import com.ec.deployits.ventaDeRopa.bean.VentasSendToMovil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.menu.ListaDeArriendos;
import opencloset.ec.com.opencloset.activity.menu.ListaDeCompras;
import opencloset.ec.com.opencloset.activity.menu.ListaDeVentas;
import opencloset.ec.com.opencloset.activity.notificaciones.SinConexionActivity;
import opencloset.ec.com.opencloset.activity.sesion.LoginActivity;
import opencloset.ec.com.opencloset.adaptador.RentasAdaptador;
import opencloset.ec.com.opencloset.adaptador.VentasAdaptador;
import opencloset.ec.com.opencloset.fragment.dialogs.dlgFragmentLista.DlgTransaccion;
import opencloset.ec.com.opencloset.fragment.tab.TodasLasRentas;
import opencloset.ec.com.opencloset.fragment.tab.TodasLasVentas;
import opencloset.ec.com.opencloset.global.Globales;
import opencloset.ec.com.opencloset.hilos.HiloCategorias;
import opencloset.ec.com.opencloset.hilos.HiloRentas;
import opencloset.ec.com.opencloset.hilos.HiloVentas;
import opencloset.ec.com.opencloset.utilidad.MensajeSistema;
import opencloset.ec.com.opencloset.utilidad.MetodoPara;
import opencloset.ec.com.opencloset.utilidad.TabPageAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private Menu menu;

    private boolean hayConexion;

    private VentasAdaptador ventasAdaptador;
    private RentasAdaptador rentasAdaptador;

    private HiloVentas hiloVentas;
    private HiloRentas hiloRentas;
    private HiloCategorias hCategorias;

    private TabPageAdapter mTabPageAdapter;

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MetodoPara.permisoDeEjecucion();

        Globales.beanUsuario = MetodoPara.traerPreferenciaBeanUsuario(this);

        hayConexion = MetodoPara.isConnected(this);
        if (!hayConexion) {
            Intent intent = new Intent(this, SinConexionActivity.class);
            startActivity(intent);
            System.out.println("-------------------------------------------------");
            System.out.println("-----   se va a SinConexionActivity   ------");
            System.out.println("-------------------------------------------------");

            finish();
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

        } else {
            hiloVentas = new HiloVentas();
            hiloRentas = new HiloRentas();
            hCategorias = new HiloCategorias();

            hiloVentas.start();
            hCategorias.start();
            hiloRentas.start();
            //unimos los hilos, para esperar a que terminen todos estos hilos y continuar
            try {
                hCategorias.join();
                hiloRentas.join();
                hiloVentas.join();
            } catch (InterruptedException e) {
                //System.out.println("Hilo principal interrumpido.");
            }
        }

        MetodoPara.ordenarCuponesComerciosPorCategorias();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Globales.fab = findViewById(R.id.fab);
        Globales.fab.setVisibility(View.VISIBLE);
        Globales.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Ahora puedes vender o rentar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                DlgTransaccion dlgTransaccion = new DlgTransaccion();
                dlgTransaccion.show(getSupportFragmentManager(), "dlgTransaccion");
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        String nombre = MetodoPara.leerValor(this, "nombre");

        if (Globales.beanUsuario.getId_usuario() == null || nombre.length() == 0) {
            menu.findItem(R.id.inicio_sesion).setVisible(true);
            menu.findItem(R.id.cerrar_sesion).setVisible(false);
        } else {
            menu.findItem(R.id.inicio_sesion).setVisible(false);
            menu.findItem(R.id.cerrar_sesion).setVisible(true);
        }
        TodasLasVentas tabTodasLasVentas = new TodasLasVentas();
        TodasLasRentas tabTodasLasRentas = new TodasLasRentas();
        //para cargar los fragments de los Tabs
        mTabPageAdapter = new TabPageAdapter(getSupportFragmentManager());
        mTabPageAdapter.addFragment(tabTodasLasVentas, getResources().getString(R.string.tab1));
        mTabPageAdapter.addFragment(tabTodasLasRentas, getResources().getString(R.string.tab2));

        ViewPager mViewPager = findViewById(R.id.containerViewPager);
        mViewPager.setAdapter(mTabPageAdapter);

        Globales.tabLayout = findViewById(R.id.tabs);
        Globales.tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(Globales.tabLayout));
        Globales.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        obtenerNombreDeUsuario();

        searchView = findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (ventasAdaptador != null) {
                    ventasAdaptador.getFilter().filter(newText);
                    ventasAdaptador.notifyDataSetChanged();
                }
                if (rentasAdaptador != null) {
                    rentasAdaptador.getFilter().filter(newText);
                    rentasAdaptador.notifyDataSetChanged();
                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        ArrayAdapter adapter = new ArrayAdapter <>(this, R.layout.spinner_item, Globales.lstCategoriasStrSpinner);
        Globales.spinner = findViewById(R.id.spinner);
        Globales.spinner.setAdapter(adapter);

        Globales.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapter, View vies, int position, long id) {

                String valorSpinner = Globales.spinner.getItemAtPosition(position).toString();//obtenemos el valor del item
                if (valorSpinner != null) {
                    if (!Globales.regresoMainActivityBtnFlotante) //controla el regreso con el boton flotante
                        Globales.regresoMainActivityBtnFlotante = false;

                    actualizarListas(valorSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapter) {
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList <String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actualizar) {

            Thread hVenta = new Thread(hiloVentas);
            Thread hCategoria = new Thread(hCategorias);
            Thread hRenta = new Thread(hiloRentas);

            hVenta.start();
            hCategoria.start();
            hRenta.start();
            try {
                hVenta.join();
                hCategoria.join();
                hRenta.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mTabPageAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        Fragment fragmento = null;
        MensajeSistema mensaje = new MensajeSistema(this);
        int id = item.getItemId();
        if (id == R.id.inicio_sesion) {
            Intent irAloginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(irAloginActivity);
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        } else if (id == R.id.compras) {
            if (Globales.beanUsuario.getCorreo() == null) {
                mensaje.mostrarMensajeToastActivity("Inicie sesión para ver sus compras");
            } else {
                Intent irAListaDeCompras = new Intent(getApplicationContext(), ListaDeCompras.class);
                startActivity(irAListaDeCompras);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        } else if (id == R.id.arriendos) {
            if (Globales.beanUsuario.getCorreo() == null) {
                mensaje.mostrarMensajeToastActivity("Inicie sesión para ver sus arriendos");
            } else {
                Intent irAListaDeArriendos = new Intent(getApplicationContext(), ListaDeArriendos.class);
                startActivity(irAListaDeArriendos);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        } else if (id == R.id.ventas) {
            if (Globales.beanUsuario.getCorreo() == null) {
                mensaje.mostrarMensajeToastActivity("Inicie sesión para ver sus ventas");
            } else {
                Intent irAListaDeVentas = new Intent(getApplicationContext(), ListaDeVentas.class);
                startActivity(irAListaDeVentas);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }

        } else if (id == R.id.cerrar_sesion) {
            //con esto anulamos el beanCliente
            MetodoPara.guardarValor(this, "idUsuario", "");
            Globales.beanUsuario = MetodoPara.traerPreferenciaBeanUsuario(this);
            MetodoPara.cargarValoresDelSistema();
            actualizarListas(Globales.item_Spinner_todas_las_categorias);
            obtenerNombreDeUsuario();
            //metodo para vaciar los fragments, es decir eliminar los fragments
            FragmentManager fm = this.getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            Globales.tabLayout.setVisibility(View.VISIBLE);
            mensaje.mostrarMensajeToastActivity("Ha finalizado sesión");
            menu.findItem(R.id.cerrar_sesion).setVisible(false);
            menu.findItem(R.id.inicio_sesion).setVisible(true);
/*        } else if (id == R.id.categorias) {

            Intent irAcategorias = new Intent(getApplicationContext(), Categorias.class);
            startActivity(irAcategorias);
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

      /*  } else if (id == R.id.MapaDireccion) {
            fragmento = GoogleMapsFrgBuscaCiudad.newInstance();
            // Metodo para mostrar los fragmentos
            showFragment(fragmento);
      */
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void obtenerNombreDeUsuario() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            View hView = navigationView.getHeaderView(0);
            TextView nav_user = hView.findViewById(R.id.nav_name);

            String nombre = MetodoPara.leerValor(this, "nombre");
            String bienvenido = getString(R.string.bienvenido);
            String hola = getString(R.string.hola);
            if (Globales.beanUsuario.getCorreo() == null || nombre.length() == 0) {
                nav_user.setText(bienvenido);
            } else {
                nav_user.setText("¡" + hola + " " + nombre + "!");
                //      ImageView imgvw = (ImageView) hView.findViewById(R.id.imageView);
// COLOCAR LA IMAGEN DE FOTO        imgvw.setImageResource();
            }
        }
    }

    public void actualizarListas(String valorSpinner) {

        System.out.println("______________________________________________________________________________________");
        System.out.println("-> ");
        System.out.println("-> ACTUALIZAR LISTA DE VENTAS Y RENTAS desde Spinner");
        System.out.println("______________________________________________________________________________________");

        if (Globales.lstVentasPorCategoriaAdaptador != null) {
            GridView grdvVentas = findViewById(R.id.grdvVentas);
            Globales.lstVentasPorCategoriaAdaptador.clear();
            //se carga de acuerdo al item seleccionado en el Spinner
            if (valorSpinner.equals(Globales.item_Spinner_todas_las_categorias)) {
                for (VentasSendToMovil venta : Globales.lstVentasSistema) {
                    Globales.lstVentasPorCategoriaAdaptador.add(venta);
                }
            } else {
                for (VentasSendToMovil venta : Globales.lstVentasSistema) {
                    if (venta.getCategoria().getNombre().equals(valorSpinner)) {
                        Globales.lstVentasPorCategoriaAdaptador.add(venta);
                    }
                }
            }
            ventasAdaptador = new VentasAdaptador(this, Globales.lstVentasPorCategoriaAdaptador);
            ventasAdaptador.setNotifyOnChange(true);
            grdvVentas.setAdapter(ventasAdaptador);
        }
        if (Globales.lstRentasPorCategoriaAdaptador != null) {
            GridView grdvRentas = findViewById(R.id.grdvRentas);
            Globales.lstRentasPorCategoriaAdaptador.clear();
            //se carga de acuerdo al item seleccionado en el Spinner
            if (valorSpinner.equals(Globales.item_Spinner_todas_las_categorias)) {
                for (RentasSendToMovil renta : Globales.lstRentasSistema) {
                    Globales.lstRentasPorCategoriaAdaptador.add(renta);
                }
            } else {
                for (RentasSendToMovil renta : Globales.lstRentasSistema) {
                    if (renta.getCategoria().equals(valorSpinner)) {
                        Globales.lstRentasPorCategoriaAdaptador.add(renta);
                    }
                }
            }
            rentasAdaptador = new RentasAdaptador(this, Globales.lstRentasPorCategoriaAdaptador);
            rentasAdaptador.setNotifyOnChange(true);
            grdvRentas.setAdapter(rentasAdaptador);
        }
    }

    @Override
    public void onBackPressed() {
        //poner el super.onBackPressed(); en primer lugar, sino no funciona
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();// elimina los fragmentos del contenedor
        }
        if (getSupportFragmentManager().findFragmentById(R.id.contenedor) == null) {
            Globales.tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        System.out.println("-------------------------------------------------");
        System.out.println("-------    ON RESTART - MainActivity    ---------");
        System.out.println("-------------------------------------------------");

        Globales.regresoMainActivityBtnFlotante = false;
        Globales.tabLayout.setVisibility(View.VISIBLE);

        if (Globales.beanUsuario.getId_usuario() == null) {
            menu.findItem(R.id.cerrar_sesion).setVisible(false);
            menu.findItem(R.id.inicio_sesion).setVisible(true);
        } else {
            menu.findItem(R.id.cerrar_sesion).setVisible(true);
            menu.findItem(R.id.inicio_sesion).setVisible(false);
        }

        obtenerNombreDeUsuario();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Globales.seActivoLogin) {
            Globales.seActivoLogin = false;
            actualizarListas(Globales.item_Spinner_todas_las_categorias);

            if (Globales.beanUsuario.getId_usuario() == null) {
                menu.findItem(R.id.cerrar_sesion).setVisible(false);
                menu.findItem(R.id.inicio_sesion).setVisible(true);
            } else {
                menu.findItem(R.id.cerrar_sesion).setVisible(true);
                menu.findItem(R.id.inicio_sesion).setVisible(false);
            }
            obtenerNombreDeUsuario();
        }
    }

}
