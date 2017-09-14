package cc.uah.es.todomanager;

import android.app.Activity;
import android.icu.text.DateFormat;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import cc.uah.es.todomanager.domain.TaskList;

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a {@link TaskListActivity}
 * in two-pane mode (on tablets) or a {@link TaskDetailActivity}
 * on handsets.
 */
public class TaskDetailFragment extends Fragment implements CompleteTaskDialog.CompleteDialogListener, CancelTaskDialog.CancelDialogListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "cc.uah.es.todomanager.item_id";
    public static final String ARG_ITEM_POS = "cc.uah.es.todomanager.item_ps";

    /**
     * The dummy content this fragment is presenting.
     */
    private TaskList.Task mItem;
    private int position;
    private OnTaskChangedListener listener = null;
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the task.
            mItem = TaskList.getInstance().getTask(getArguments().getLong(ARG_ITEM_ID));
            position = getArguments().getInt(ARG_ITEM_POS);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getResources().getString(R.string.task_details_title));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_detail, container, false);
        this.rootView = rootView;

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.task_name)).append(" " + mItem.getName());
            int p = R.string.medium_priority;
            switch (mItem.getPriority()) {
                case TaskList.Task.HIGH_PRIORITY: p = R.string.high_priority; break;
                case TaskList.Task.MEDIUM_PRIORITY: p = R.string.medium_priority; break;
                case TaskList.Task.LOW_PRIORITY: p = R.string.low_priority; break;
            }
            ((TextView) rootView.findViewById(R.id.task_priority)).append(" " + getResources().getString(p));
            int s = R.string.pending_task;
            switch (mItem.getStatus().getStatusDescription()) {
                case TaskList.PendingTask.STATUS: s = R.string.pending_task; break;
                case TaskList.CompletedTask.STATUS: s = R.string.completed_task; break;
                case TaskList.CanceledTask.STATUS: s = R.string.canceled_task; break;
            }
            ((TextView) rootView.findViewById(R.id.task_status)).append(" " + getResources().getString(s));
            ((TextView) rootView.findViewById(R.id.task_description)).append("\n" + mItem.getDetails());
            if (mItem.getDeadline() != null) ((TextView) rootView.findViewById(R.id.task_deadline)).append(" " + DateFormat.getDateInstance(DateFormat.SHORT).format(mItem.getDeadline()));
            else ((View) rootView.findViewById(R.id.task_deadline)).setVisibility(View.INVISIBLE);
            if (mItem.isComplex()) {
                SeekBar bar = (SeekBar) rootView.findViewById(R.id.task_progress);
                bar.setProgress(mItem.getCompleted());
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mItem.setCompleted(progress);
                        if (progress == 100) completeTask();
                        listener.onTaskChanged(position);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
            else {
                ((View) rootView.findViewById(R.id.task_progress)).setVisibility(View.INVISIBLE);
                ((View) rootView.findViewById(R.id.task_completion)).setVisibility(View.INVISIBLE);
            }
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!(mItem.getStatus() instanceof TaskList.PendingTask)) {
            menu.removeItem(R.id.complete_button);
            menu.removeItem(R.id.cancel_button);
        }
    }

    @Override
    public void onComplete(int position) {
        ((TextView) rootView.findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(R.string.completed_task));
        listener.onTaskChanged(position);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCancel(int position) {
        ((TextView) rootView.findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(R.string.canceled_task));
        listener.onTaskChanged(position);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_task_option: editTask(); return true;
            case R.id.complete_button: completeTask();; return true;
            case R.id.cancel_button: cancelTask();; return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    protected void editTask() {

    }

    protected void completeTask() {
        CompleteTaskDialog dialog = new CompleteTaskDialog(mItem, position, this);
        dialog.show(getFragmentManager(), "CompleteTask");
    }

    protected void cancelTask() {
        CancelTaskDialog dialog = new CancelTaskDialog(mItem, -1, this);
        dialog.show(getFragmentManager(), "CancelDialog");
    }

    public OnTaskChangedListener getOnTaskChangedListener() {
        return listener;
    }

    public void setOnTaskChangedListener(OnTaskChangedListener listener) {
        this.listener = listener;
    }
}

