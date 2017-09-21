package cc.uah.es.todomanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cc.uah.es.todomanager.domain.TaskList;

public class NewTaskActivity extends AppCompatActivity   {
    public static final int ACTIVITY_CODE = 2;
    public static final String ARG_NEW_TASK1 = "cc.uah.es.todomanager.newtask1";
    public static final String ARG_NEW_TASK2 = "cc.uah.es.todomanager.newtas2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            // Create the new task 1 fragment and add it to the activity
            // using a fragment transaction.
            EditTask1Fragment fragment = EditTask1Fragment.newInstance(new OnNewTaskListener(), new TaskList.Task());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.new_task_form_container, fragment)
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

    protected class OnNewTaskListener implements cc.uah.es.todomanager.OnEditTaskListener {

        @Override
        public void onNextStep(TaskList.Task task) {
                EditTask2Fragment fragment = EditTask2Fragment.newInstance(new OnNewTaskListener(), task);
getSupportFragmentManager().beginTransaction()
        .replace(R.id.new_task_form_container, fragment)
        .commit();
        }

        @Override
        public void onPreviousStep(TaskList.Task task) {
            EditTask1Fragment fragment = EditTask1Fragment.newInstance(new OnNewTaskListener(), new TaskList.Task());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.new_task_form_container, fragment)
                    .commit();
        }

        @Override
        public void onCancel(TaskList.Task task) {
            setResult(EditTask1Fragment.TASK_CREATION_CANCELED);
            finish();
        }

        @Override
        public void onFinish(TaskList.Task task) {
            Intent intent = new Intent();
            intent.putExtra(TaskListActivity.ARG_TASK, task);
setResult(EditTask1Fragment.TASK_CREATION_COMPLETED, intent);
            finish();
        }
    }
}

