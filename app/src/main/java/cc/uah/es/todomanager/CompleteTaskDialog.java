package cc.uah.es.todomanager;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import cc.uah.es.todomanager.domain.TaskList;

/**
 * Created by Fjest on 11/09/2017.
 */

public class CompleteTaskDialog extends DialogFragment {
    private static final String TAG = "CompleteTaskDialog";

    private TaskList.Task task;
    private int position;
    private CompleteDialogListener listener;

    public CompleteTaskDialog(TaskList.Task task, int position) {
        this.task = task;
        this.position = position;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
listener = (CompleteDialogListener) activity;
        } catch (ClassCastException cce) {
            Log.e(TAG, "The listener must be a CompleteDialogListener instance.");
            throw new ClassCastException(activity.toString());
        }
    }

    @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.setMessage(String.format(getResources().getString(R.string.complete_task_dialog_message), task.getName()))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
task.complete();
                            listener.onComplete(position);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nothing to do.
                        }
                    })
                    .create();
        }

        public static interface CompleteDialogListener {
void onComplete(int position);
        }
}

