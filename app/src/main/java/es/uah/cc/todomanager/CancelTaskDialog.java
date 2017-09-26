package es.uah.cc.todomanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import es.uah.cc.todomanager.R;
import es.uah.cc.todomanager.domain.TaskList;

/**
 * Created by Fjest on 11/09/2017.
 */

public class CancelTaskDialog extends android.support.v4.app.DialogFragment{
    private static final String TAG = "CancelTaskDialog";

    private TaskList.Task task;
    private int position;
    private CancelDialogListener listener;

    public CancelTaskDialog(TaskList.Task task, int position, CancelDialogListener listener) {
        this.task = task;
        this.position = position;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TaskList.Task task = getArguments().getParcelable(TaskListActivity.ARG_TASK);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(String.format(getResources().getString(R.string.cancel_task_dialog_message), task.getName()))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.cancel();
                        listener.onCancel(task, position);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing to do.
                    }
                })
                .create();
    }

    public static interface  CancelDialogListener {
        void onCancel(TaskList.Task task, int position);
    }
}

