package opencloset.ec.com.opencloset.fragment.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import opencloset.ec.com.opencloset.R;

public class DeseaIniciarSesion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Aviso");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Es necesario iniciar sesión. ¿Desea iniciar sesión?");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                DialogFragment LoginDialogFragment = new LoginDialog();
                LoginDialogFragment.setCancelable(false);
                LoginDialogFragment.show(getActivity().getSupportFragmentManager(), "dialogoParaLoguearse");

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

}