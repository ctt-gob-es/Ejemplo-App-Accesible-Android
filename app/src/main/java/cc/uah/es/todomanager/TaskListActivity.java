package cc.uah.es.todomanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import cc.uah.es.todomanager.domain.TaskList;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Tasks. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TaskDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TaskListActivity extends AppCompatActivity implements CompleteTaskDialog.CompleteDialogListener, CancelTaskDialog.CancelDialogListener, OnTaskChangedListener {

    public static  final String ARG_TASK = "cc.uah.es.todomanager.task";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Bundle fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        TaskList.fillSampleData(TaskList.getInstance());
        fragments = new Bundle();

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
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(TaskList.getInstance().getTasks()));
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
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, task);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "CompleteTask");
    }

    protected void cancelTask (TaskList.Task task, int position) {
        CancelTaskDialog dialog = new CancelTaskDialog(task, position, this);
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, task);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "CancelDialog");
    }

    protected void viewTask(TaskList.Task task, int position, View v) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARG_TASK, task);
            arguments.putInt(TaskDetailFragment.ARG_ITEM_POS, position);
            TaskDetailFragment fragment = TaskDetailFragment.newInstance(this, new OnListEditButtonListener());
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(TaskDetailFragment.TAG)
                    .replace(R.id.task_detail_container, fragment)
                    .commit();
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra(ARG_TASK, task);
            intent.putExtra(TaskDetailFragment.ARG_ITEM_POS, position);

            startActivityForResult(intent, TaskDetailActivity.ACTIVITY_CODE);
        }
    }

    protected void newTask(View v) {
        if (mTwoPane) {
            EditTask1Fragment fragment = EditTask1Fragment.newInstance(new OnNewTaskListener(), new TaskList.Task());
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(EditTask1Fragment.TAG)
                    .replace(R.id.task_detail_container, fragment)
                    .commit();
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, NewTaskActivity.class);
                    startActivityForResult(intent, NewTaskActivity.ACTIVITY_CODE);
        }
    }

    protected void notifyTaskChanged(int position) {
        RecyclerView list = (RecyclerView) findViewById(R.id.task_list);
        list.getAdapter().notifyItemChanged(position);
    }

    protected void notifyTaskListChanged() {
        RecyclerView list = (RecyclerView) findViewById(R.id.task_list);
        list.getAdapter().notifyDataSetChanged();
    }

    protected void notifyItemInserted() {
        RecyclerView list = (RecyclerView) findViewById(R.id.task_list);
        list.getAdapter().notifyItemInserted(TaskList.getInstance().getTasks().size());
    }

    protected void addTask() {
        notifyItemInserted();
        Toast toast = Toast.makeText(getApplicationContext(), R.string.task_added, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCancel(TaskList.Task task, int position) {
        TaskList.getInstance().setTask(task);
        notifyTaskChanged(position);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.task_canceled, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onComplete(TaskList.Task task, int position) {
        TaskList.getInstance().setTask(task);
        notifyTaskChanged(position);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.task_completed, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onTaskChanged(TaskList.Task task, int position) {
TaskList.getInstance().setTask(task);
        notifyTaskChanged(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TaskDetailActivity.ACTIVITY_CODE:
                if (resultCode == TaskDetailActivity.CHANGED) {
                    TaskList.Task t = data.getParcelableExtra(ARG_TASK);
                    TaskList.getInstance().setTask(t);
                    notifyTaskChanged(data.getExtras().getInt(TaskDetailFragment.ARG_ITEM_POS));
                }
                break;
            case NewTaskActivity.ACTIVITY_CODE:
                if (resultCode == EditTask1Fragment.TASK_CREATION_COMPLETED) {
                    TaskList.Task t = data.getParcelableExtra(TaskListActivity.ARG_TASK);
                    TaskList.getInstance().addTask(t);
                    addTask();
                }
                    break;
        }
    }

    protected  class  OnNewTaskListener implements OnEditTaskListener {

        @Override
        public void onNextStep(TaskList.Task task) {
Fragment fragment = EditTask2Fragment.newInstance(new OnNewTaskListener(), task);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(EditTask2Fragment.TAG)
                    .replace(R.id.task_detail_container, fragment)
                    .commit();
        }

        @Override
        public void onPreviousStep(TaskList.Task task) {
getSupportFragmentManager().popBackStack(EditTask2Fragment.TAG, 0);
        }

        @Override
        public void onCancel(TaskList.Task task) {
            getSupportFragmentManager().popBackStack(EditTask1Fragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void onFinish(TaskList.Task task) {
TaskList.getInstance().addTask(task);
            addTask();
            getSupportFragmentManager().popBackStack(EditTask1Fragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            viewTask(task, TaskList.getInstance().getTasks().size(), null);
        }

    }

    protected class  OnUpdateTaskListener implements cc.uah.es.todomanager.OnEditTaskListener {

        @Override
        public void onNextStep(TaskList.Task task) {
            Fragment fragment = EditTask2Fragment.newInstance(new OnUpdateTaskListener(), task);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(EditTask2Fragment.TAG)
                    .replace(R.id.task_detail_container, fragment)
                    .commit();
        }

        @Override
        public void onPreviousStep(TaskList.Task task) {
            getSupportFragmentManager().popBackStack(EditTask2Fragment.TAG, 0);
        }

        @Override
        public void onCancel(TaskList.Task task) {
            getSupportFragmentManager().popBackStack(EditTask1Fragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void onFinish(TaskList.Task task) {
TaskList.getInstance().setTask(task);
            getSupportFragmentManager().popBackStack(EditTask1Fragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            TaskDetailFragment fragment = (TaskDetailFragment) getSupportFragmentManager().getFragment(fragments, TaskDetailFragment.TAG);
            fragment.updateTask(task);
        }
    }

protected class OnListEditButtonListener implements  OnEditButtonListener {
    @Override
    public void init(TaskList.Task task) {
        EditTask1Fragment fragment = EditTask1Fragment.newInstance(new OnUpdateTaskListener(), task.clone());
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(EditTask1Fragment.TAG)
                .replace(R.id.task_detail_container, fragment)
.commit();
    }
}
}

