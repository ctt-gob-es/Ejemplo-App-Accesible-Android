package es.uah.cc.todomanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Iterator;
import java.util.List;

/** Dialog showed when the contact form is wrong.
 * Created by Fjest on 19/09/2018.
 */

public class ErrorContactMessageDialog extends DialogFragment {

    private List<String> messages;

    public ErrorContactMessageDialog(List<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "";
        Iterator it = messages.iterator();
        while (it.hasNext()) {
            message = message + it.next();
            if (it.hasNext()) message = message + "\n";
        }
                return builder.setMessage(message)
                        .setTitle(R.string.error_task_title)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }


}

