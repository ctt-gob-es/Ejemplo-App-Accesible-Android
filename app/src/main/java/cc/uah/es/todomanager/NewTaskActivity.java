package cc.uah.es.todomanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cc.uah.es.todomanager.domain.TaskList;

public class NewTaskActivity extends AppCompatActivity implements NewTask1Fragment.OnNewTaskListener {

    private TaskList.Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
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
            NewTask1Fragment fragment = NewTask1Fragment.newInstance(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.new_task_form_container, fragment)
                    .commit();

            task = new TaskList.Task();
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
