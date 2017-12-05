package es.uah.cc.todomanager;

import android.app.Activity;
import java.text.DateFormat;
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

import es.uah.cc.todomanager.R;
import es.uah.cc.todomanager.domain.TaskList;

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a {@link TaskListActivity}
 * in two-pane mode (on tablets) or a {@link TaskDetailActivity}
 * on handsets.
 */
public class TaskDetailFragment extends Fragment implements CompleteTaskDialog.CompleteDialogListener, CancelTaskDialog.CancelDialogListener {
    public  static  final  String TAG = "es.uah.cc.todomanager.TaskDetailFragment";
    /**
     * The key for the item position on the list view included in the input arguments.
     */
    public static final String ARG_ITEM_POS = "es.uah.cc.todomanager.item_ps";

    /**
     * The task this fragment is presenting.
     */
    private TaskList.Task mItem;
    /**
     * The position of the task on the list view.
     */
    private int position;
    /**
     * The listener for the changes suffered by the task.
     */
    private OnTaskChangedListener listener = null;
    /**
     * The listener for edit button interactions.
     */
    private OnEditButtonListener editButtonListener;

    public TaskDetailFragment() {
    }

    /**
     * A factory method.
     * @param taskChangedListener The listener for changes on the task.
     * @param editButtonListener  The listener for edit button interactions.
     * @param task                The task to be presented.
     * @param position            The position of the task on the list view.
     * @return A fragment instance.
     */
    public static TaskDetailFragment newInstance(OnTaskChangedListener taskChangedListener, OnEditButtonListener editButtonListener, TaskList.Task task, int position) {
TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setOnTaskChangedListener(taskChangedListener);
        fragment.setEditButtonListener(editButtonListener);
        Bundle args = new Bundle();
        args.putParcelable(TaskListActivity.ARG_TASK, task);
        args.putInt(ARG_ITEM_POS, position);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Getter for EditButtonListener
     * @return The listener.
     */
    public OnEditButtonListener getEditButtonListener() {
        return editButtonListener;
    }

    /**
     * The setter for EditButtonListener.
     * @param editButtonListener    The listener.
     */
    public void setEditButtonListener(OnEditButtonListener editButtonListener) {
        this.editButtonListener = editButtonListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(TaskListActivity.ARG_TASK)) {
            // Load the task.
            mItem = (TaskList.Task) getArguments().getParcelable(TaskListActivity.ARG_TASK);
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

        // Shows the task details.
        if (mItem != null) {
fillData(rootView);
showProgressIfComplex(rootView);
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    /**
     * Fills the text views with the data from the task.
     * @param rootView    The root view.
     */
    protected  void fillData(View rootView) {
    ((TextView) rootView.findViewById(R.id.task_name)).setText(getResources().getString(R.string.task_name) + " " + mItem.getName());
    int p = R.string.medium_priority;
    switch (mItem.getPriority()) {
        case TaskList.Task.HIGH_PRIORITY: p = R.string.high_priority; break;
        case TaskList.Task.MEDIUM_PRIORITY: p = R.string.medium_priority; break;
        case TaskList.Task.LOW_PRIORITY: p = R.string.low_priority; break;
    }
    ((TextView) rootView.findViewById(R.id.task_priority)).setText(getResources().getString(R.string.task_priority) + " " + getResources().getString(p));
    int s = R.string.pending_task;
    switch (mItem.getStatus().getStatusDescription()) {
        case TaskList.PendingTask.STATUS: s = R.string.pending_task; break;
        case TaskList.CompletedTask.STATUS: s = R.string.completed_task; break;
        case TaskList.CanceledTask.STATUS: s = R.string.canceled_task; break;
    }
    ((TextView) rootView.findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(s));
    ((TextView) rootView.findViewById(R.id.task_description)).setText(getResources().getString(R.string.task_description) + "\n" + mItem.getDetails());
        // If the task has a deadline, it shows it.
    if (mItem.getDeadline() != null) ((TextView) rootView.findViewById(R.id.task_deadline)).setText(getResources().getString(R.string.task_deadline) + " " + DateFormat.getDateInstance(DateFormat.SHORT).format(mItem.getDeadline()));
    else ((View) rootView.findViewById(R.id.task_deadline)).setVisibility(View.INVISIBLE);
}

    /**
     * Shows a progress bar if the task is complex.
     * @param rootView    The root view.
     */
    protected  void showProgressIfComplex(View rootView) {
        if (mItem.isComplex()) {
            SeekBar bar = (SeekBar) rootView.findViewById(R.id.task_progress);
            bar.setProgress(mItem.getCompleted());
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // the task progress is updated each time the progress bar is changed.
                    mItem.setCompleted(progress);
                    // If progress reaches 100%, task is automatically marked as completed.
                    if (progress == 100) completeTask();
                    listener.onTaskChanged(mItem, position);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
//Nothing to do.
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
// Nothing to do.
                }
            });

        }
        else {
            ((View) rootView.findViewById(R.id.task_progress)).setVisibility(View.INVISIBLE);
            ((View) rootView.findViewById(R.id.task_completion)).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // If task is not pending, the user will not be able to edit, complete or cancel it.
        if (!(mItem.getStatus() instanceof TaskList.PendingTask)) {
            menu.removeItem(R.id.complete_button);
            menu.removeItem(R.id.cancel_button);
            menu.removeItem(R.id.edit_task_option);
        }
    }

    @Override
    public void onComplete(TaskList.Task task, int position) {
        ((TextView) getView().findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(R.string.completed_task));
        listener.onTaskChanged(task, position);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCancel(TaskList.Task task, int position) {
        ((TextView) getView().findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(R.string.canceled_task));
        listener.onTaskChanged(task, position);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_task_option: editTask(); return true;
            case R.id.complete_button: completeTask(); return true;
            case R.id.cancel_button: cancelTask(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts an EditTaskActivity.
     */
    protected void editTask() {
editButtonListener.init(mItem);
    }

    /**
     * Marks a task as completed.
     */
    protected void completeTask() {
        CompleteTaskDialog dialog = new CompleteTaskDialog(mItem, position, this);
        Bundle args = new Bundle();
        args.putParcelable(TaskListActivity.ARG_TASK, mItem);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "CompleteTask");
    }

    /**
     * Marks a task as cancelled.
     */
    protected void cancelTask() {
        CancelTaskDialog dialog = new CancelTaskDialog(mItem, -1, this);
        Bundle args = new Bundle();
        args.putParcelable(TaskListActivity.ARG_TASK, mItem);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "CancelDialog");
    }

    /**
     * Getter for OnTaskChangedListener.
     * @return The listener.
     */
    public OnTaskChangedListener getOnTaskChangedListener() {
        return listener;
    }

    /**
     * Setter for OnTaskChangedListener.
     * @param listener    The listener.
     */
    public void setOnTaskChangedListener(OnTaskChangedListener listener) {
        this.listener = listener;
    }
}

