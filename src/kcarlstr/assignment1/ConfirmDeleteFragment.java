package kcarlstr.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by kylecarlstrom on 15-01-24.
 * 
 * Fragment to host delete confirmation dialog, passes data back through interface onDialogPass
 * The activity then implements this framework in order to recieve the data.
 */
public class ConfirmDeleteFragment extends DialogFragment {

    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onDialogPass(boolean data);
    }

    @Override
    public void onAttach(Activity activity) {
        dataPasser = (OnDataPass) activity;
        super.onAttach(activity);
    }

    public void passData(boolean data) {
        dataPasser.onDialogPass(data);
    }

    /*
     * Creates a Dialog that asks the user to confirm that they wish to delete something
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Confirm deletion?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        passData(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        passData(false);
                    }
                });
        return builder.create();
    }
}
