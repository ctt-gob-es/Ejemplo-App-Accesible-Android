package es.uah.cc.todomanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/** A dialog to show error messages on editing tasks.
 * Created by Fjest on 18/09/2018.
 */

public class ErrorTaskDialog extends DialogFragment {

    public ErrorTaskDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(R.string.error_task_message)
                .setTitle(R.string.error_task_title)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }

}

