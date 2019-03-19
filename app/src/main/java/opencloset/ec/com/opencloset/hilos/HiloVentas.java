package opencloset.ec.com.opencloset.hilos;

import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class HiloVentas extends Thread {

    public HiloVentas() {

    }

    @Override
    public void run() {

        MetodoPara.cargarListaVentas();

    }

}
