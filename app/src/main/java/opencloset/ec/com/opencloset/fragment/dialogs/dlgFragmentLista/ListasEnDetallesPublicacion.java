package opencloset.ec.com.opencloset.fragment.dialogs.dlgFragmentLista;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import opencloset.ec.com.opencloset.R;

public class ListasEnDetallesPublicacion extends DialogFragment {

    private int mBotonAcolocar;
    private boolean[] multi;

    private static String FILE_NAME_BTN_KEY;

    private static final String BTN_KEY = "btn";
    private static final String TITLE_KEY = "title";
    private static final String DATO_KEY_MAP = "mapDeDatos";
    private String mTitle;

    private Map mapInicial, mapResultado;

    private ISelectedData mCallback;

    public interface ISelectedData {
        void onSelectedData(Map <String, Integer> lista, int colocarTexto, String title_key);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ISelectedData) activity;
        } catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapResultado = new HashMap<String,Integer >();

        if (savedInstanceState == null) {
            //not restart
            Bundle args = getArguments();
            if (args == null) {
                throw new IllegalArgumentException("Bundle args required - Bundle sin argumentos");
            }
            mBotonAcolocar = args.getInt(BTN_KEY);
            mTitle = args.getString(TITLE_KEY);
            mapInicial = (Map) args.getSerializable(DATO_KEY_MAP);
        } else {
            //restart
            mBotonAcolocar = savedInstanceState.getInt(BTN_KEY);
            mTitle = savedInstanceState.getString(TITLE_KEY);
            mapInicial = (Map) savedInstanceState.getSerializable(DATO_KEY_MAP);
        }
        // se crea un archivo temporal para almacenar las opciones
        FILE_NAME_BTN_KEY = "multi_" + mBotonAcolocar + ".txt";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BTN_KEY, TITLE_KEY);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogo_deseas_publicar);

//        mOpciones = (String[]) hashMap.keySet().toArray(new String[hashMap.size()]);
//        dialogoMultiChoice(builder, mOpciones);

        dialogoMultiChoice(builder, mapInicial);

        return builder.create();
    }

    private void dialogoMultiChoice(android.app.AlertDialog.Builder builder, final Map <String, Integer> mapEntrada) {
        multi = new boolean[mapEntrada.size()];
        loadItems(multi);

        String[] mOpciones = (String[]) mapEntrada.keySet().toArray(new String[mapEntrada.size()]);

        builder.setTitle(mTitle);
        builder.setMultiChoiceItems(mOpciones, multi, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //  mensaje.mostrarMensajeToastContext("ud ha elegido " + transacciones[which]);
            }
        });

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                saveItems(multi, mapEntrada);
                mCallback.onSelectedData(mapResultado, mBotonAcolocar, mTitle);
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }

    private void saveItems(boolean[] multi, Map <String, Integer> options) {
        Collection <String> values = options.keySet();
        String[] targetArray = values.toArray(new String[values.size()]);

        PrintWriter writer = null;
        Set <String> lista = new HashSet();
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILE_NAME_BTN_KEY, Context.MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));

            for (int i = 0; i < multi.length; i++) {
                if (multi[i]) {
                    lista.add(targetArray[i]);
                    mapResultado.put(targetArray[i], mapInicial.get(targetArray[i]));
                }
                writer.println(Boolean.toString(multi[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                //((MenuPrincipal) getActivity()).setDatos(lista.toString());
                writer.close();
            }
        }
    }

    // para cargar los valores seleccionados
    private void loadItems(boolean[] multi) {
        BufferedReader reader = null;
        try {
            FileInputStream fis = getActivity().openFileInput(FILE_NAME_BTN_KEY);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            int i = 0;

            while ((title = reader.readLine()) != null) {
                if (title.equals("true")) {
                    multi[i] = true;
                } else if (title.equals("false")) {
                    multi[i] = false;
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}