package cc.uah.es.todomanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import cc.uah.es.todomanager.domain.TaskList;

/**
 * An activity representing a single Task detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TaskListActivity}.
 */
public class TaskDetailActivity extends AppCompatActivity implements OnTaskChangedListener{
    private final static String TAG = "TaskDetailActivity";
    public static final int ACTIVITY_CODE = 1;
    public final static int CHANGED = 1;
    public final static int NOT_CHANGED = 0;
    private boolean changed = false;
    private int position;
    private TaskDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(TaskDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(TaskDetailFragment.ARG_ITEM_ID, -1));
            position = getIntent().getIntExtra(TaskDetailFragment.ARG_ITEM_POS, -1);
            arguments.putInt(TaskDetailFragment.ARG_ITEM_POS, position);
            fragment = TaskDetailFragment.newInstance(this, new OnDetailsEditButtonListener());
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, TaskListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskChanged(int position) {
        changed = true;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TaskDetailFragment.ARG_ITEM_POS, position);
        int result = changed ? CHANGED : NOT_CHANGED;
        setResult(result, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditTaskActivity.ACTIVITY_CODE) {
            if (resultCode == EditTask1Fragment.TASK_EDITION_COMPLETED) {
                TaskList.Task t = TaskList.getInstance().getTask(data.getLongExtra(TaskDetailFragment.ARG_ITEM_ID, -1));
fragment.updateTask(t);
            }
        }
    }

    protected  class OnDetailsEditButtonListener implements OnEditButtonListener {
        @Override
        public void init(TaskList.Task task) {
            Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
            intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, task.getId());
            startActivityForResult(intent, EditTaskActivity.ACTIVITY_CODE);
        }
    }
}

