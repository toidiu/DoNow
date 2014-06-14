package toidiu.com.donow.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import toidiu.com.donow.R;
import toidiu.com.donow.interfaces.AddDiagFragListener;


public class DiagFrag extends DialogFragment {

    private static final String TITLE = "title";

    private AddDiagFragListener listener;

    public static DiagFrag newInstance(AddDiagFragListener listener, String title) {
        DiagFrag df = new DiagFrag();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        df.listener = listener;
        df.setArguments(args);

        return df;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(TITLE);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog, null);
        final EditText input = (EditText) view.findViewById(R.id.add_item_text);

        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setView(view)
                .setMessage("Add new item to the list")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.doPositiveClick(input);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.doNegativeClick();
                        dialog.cancel();
                    }
                })
                .create();
    }

}
