package cc.uah.es.todomanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import cc.uah.es.todomanager.domain.TaskList;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An activity representing a list of Tasks. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TaskDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TaskListActivity extends AppCompatActivity implements CompleteTaskDialog.CompleteDialogListener, CancelTaskDialog.CancelDialogListener, OnTaskChangedListener, NewTask1Fragment.OnNewTaskListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        TaskList.fillSampleData(TaskList.getInstance());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTask(view);
            }
        });

        View recyclerView = findViewById(R.id.task_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.task_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(new ArrayList<TaskList.Task>(TaskList.getInstance().getTasks())));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<TaskList.Task> mValues;

        public SimpleItemRecyclerViewAdapter(List<TaskList.Task> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            if (holder.mItem.getStatus() instanceof TaskList.PendingTask) {
                switch (holder.mItem.getPriority()) {
                    case TaskList.Task.HIGH_PRIORITY:
                        holder.mNameView.setTextColor(getResources().getColor(R.color.high_priority));
                        break;
                    case TaskList.Task.LOW_PRIORITY:
                        holder.mNameView.setTextColor(getResources().getColor(R.color.low_priority));
                        break;
                    default: holder.mNameView.setTextColor(getResources().getColor(R.color.medium_priority));
                }
            } else {
                if (holder.mItem.getStatus() instanceof TaskList.CompletedTask) holder.mNameView.setTextColor(getResources().getColor(R.color.completed));
                else if (holder.mItem.getStatus() instanceof TaskList.CanceledTask) holder.mNameView.setTextColor(getResources().getColor(R.color.canceled));
            }

            holder.mNameView.setText(holder.mItem.getName());
            if (holder.mItem.isComplex() & holder.mItem.getStatus() instanceof TaskList.PendingTask)
                holder.mNameView.append("\n" + String.format(getResources().getString(R.string.percentage_completed), holder.mItem.getCompleted()));
            if (holder.mItem.getDeadline() != null)
            holder.mDeadlineView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(holder.mItem.getDeadline()));
            if (holder.mItem.getStatus() instanceof TaskList.CompletedTask | holder.mItem.getStatus() instanceof  TaskList.CanceledTask) {
                holder.mCompleteButton.setVisibility(View.INVISIBLE);
                holder.mCancelButton.setVisibility(View.INVISIBLE);
            } else {
                holder.mCompleteButton.setVisibility(View.VISIBLE);
                holder.mCancelButton.setVisibility(View.VISIBLE);
            }

            holder.mNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewTask(holder.mItem, position, v);
                }
            });

            holder.mCompleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeTask(holder.mItem, position);
                }
            });
            holder.mCancelButton.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
cancelTask(holder.mItem, position);
                }
            }));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mDeadlineView;
            public final ImageButton mCancelButton;
            public final ImageButton mCompleteButton;
            public TaskList.Task mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mDeadlineView = (TextView) view.findViewById(R.id.deadline);
                mCancelButton = (ImageButton) view.findViewById(R.id.cancel_button);
                mCompleteButton = (ImageButton) view.findViewById(R.id.complete_button);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }

    protected void completeTask(TaskList.Task task, int position) {
        CompleteTaskDialog dialog = new CompleteTaskDialog(task, position, this);
        dialog.show(getSupportFragmentManager(), "CompleteTask");
    }

    protected void cancelTask (TaskList.Task task, int position) {
        CancelTaskDialog dialog = new CancelTaskDialog(task, position, this);
        dialog.show(getSupportFragmentManager(), "CancelDialog");
    }

    protected void viewTask(TaskList.Task task, int position, View v) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putLong(TaskDetailFragment.ARG_ITEM_ID, task.getId());
            arguments.putInt(TaskDetailFragment.ARG_ITEM_POS, position);
            TaskDetailFragment fragment = new TaskDetailFragment();
            fragment.setArguments(arguments);
            fragment.setOnTaskChangedListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.task_detail_container, fragment)
                    .commit();
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, task.getId());
            intent.putExtra(TaskDetailFragment.ARG_ITEM_POS, position);

            startActivityForResult(intent, TaskDetailActivity.ACTIVITY_CODE);
        }
    }

    protected void newTask(View v) {
        if (mTwoPane) {
            NewTask1Fragment fragment = NewTask1Fragment.newInstance(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.task_detail_container, fragment)
                    .commit();
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, NewTaskActivity.class);
            startActivityForResult(intent, NewTask1Fragment.ACTIVITY_CODE);
        }
    }

    @Override
    public void onCancel(int position) {
        onTaskChanged(position);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.task_canceled, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onComplete(int position) {
        onTaskChanged(position);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.task_completed, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onTaskChanged(int position) {
        RecyclerView list = (RecyclerView) findViewById(R.id.task_list);
        list.getAdapter().notifyItemChanged(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TaskDetailActivity.ACTIVITY_CODE:
                if (resultCode == TaskDetailActivity.CHANGED) onTaskChanged(data.getExtras().getInt(TaskDetailFragment.ARG_ITEM_POS));
                break;
        }
    }

    @Override
    public void onNewTaskNextStep(TaskList.Task task) {

    }

    @Override
    public void onNewTaskPreviousStep(TaskList.Task task) {

    }

    @Override
    public void onNewTaskCancel() {

    }

    @Override
    public void onNewTaskFinish(TaskList.Task task) {

    }
}

