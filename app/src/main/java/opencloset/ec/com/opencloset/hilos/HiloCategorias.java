package opencloset.ec.com.opencloset.hilos;

import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class HiloCategorias extends Thread {

    @Override
    public void run() {

        MetodoPara.cargarListaCategorias();

    }

}
