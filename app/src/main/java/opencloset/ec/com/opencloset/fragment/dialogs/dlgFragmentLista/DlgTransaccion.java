package opencloset.ec.com.opencloset.fragment.dialogs.dlgFragmentLista;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import opencloset.ec.com.opencloset.R;
import opencloset.ec.com.opencloset.activity.subir.Detalles_publicacion;
import opencloset.ec.com.opencloset.global.Globales;

public class DlgTransaccion extends DialogFragment {

    private static int single = 0;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogo_deseas_publicar);

        dialogoSingleChoice(builder, Globales.mapTransaccion.keySet().toArray(new String[Globales.mapTransaccion.size()]));

        return builder.create();
    }

    private void dialogoSingleChoice(AlertDialog.Builder builder, final String[] mOpciones) {
        builder.setSingleChoiceItems(mOpciones, single, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent irADetallesPublicacion = new Intent(getActivity(), Detalles_publicacion.class);
                irADetallesPublicacion.putExtra("mOpcion", mOpciones[single = which]);
                Globales.idTipoTransaccion = Globales.mapTransaccion.get(mOpciones[single = which]);

                startActivity(irADetallesPublicacion);

                Toast.makeText(getActivity(), mOpciones[single = which], Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

}