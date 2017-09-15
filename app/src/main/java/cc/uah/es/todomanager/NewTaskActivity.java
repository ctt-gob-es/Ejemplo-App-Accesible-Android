package cc.uah.es.todomanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cc.uah.es.todomanager.domain.TaskList;

public class NewTaskActivity extends AppCompatActivity   {

    private TaskList.Task task;
    private EditTask1Fragment formFragment;

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
            task = new TaskList.Task();
            // Create the new task 1 fragment and add it to the activity
            // using a fragment transaction.
            formFragment = EditTask1Fragment.newInstance(new OnNewTaskListener(), task);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.new_task_form_container, formFragment)
                    .commit();
        }
    }

    protected class OnNewTaskListener implements cc.uah.es.todomanager.OnEditTaskListener {

        @Override
        public void onNextStep(TaskList.Task task) {

        }

        @Override
        public void onPreviousStep(TaskList.Task task) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.new_task_form_container, formFragment)
                    .commit();
        }

        @Override
        public void onCancel() {
            setResult(EditTask1Fragment.TASK_CREATION_CANCELED);
            finish();
        }

        @Override
        public void onFinish(TaskList.Task task) {

        }
    }
}

