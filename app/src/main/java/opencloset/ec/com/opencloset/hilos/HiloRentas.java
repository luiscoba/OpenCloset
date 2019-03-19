package opencloset.ec.com.opencloset.hilos;

import opencloset.ec.com.opencloset.utilidad.MetodoPara;

public class HiloRentas extends Thread {

    public HiloRentas() {

    }

    @Override
    public void run() {

        MetodoPara.cargarListaRentas();

    }

}
