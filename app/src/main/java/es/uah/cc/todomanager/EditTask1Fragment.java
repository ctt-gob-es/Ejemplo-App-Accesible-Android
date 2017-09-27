package es.uah.cc.todomanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

import es.uah.cc.todomanager.R;
import es.uah.cc.todomanager.domain.TaskList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditTask1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditTask1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTask1Fragment extends Fragment {
    public static final String TAG = "es.uah.cc.todomanager.EditTask1Fragment";
    /**
     * A key for transactions and task arguments.
     */
    public static final String EDIT_TASK_1 = "es.uah.cc.todomanager.edittask1";
    /**
     * Result: Creation of the task was completed.
     */
    public static final int TASK_CREATION_COMPLETED = 1;
    /**
     * Result: Edition of the task was completed.
     */
    public static final int TASK_EDITION_COMPLETED = 2;
    /**
     * Result: Creation of the task was cancelled.
     */
    public static final int TASK_CREATION_CANCELED = 0;
    /**
     * Result: Edition of the task was cancelled.
     */
    public static  final  int TASK_EDITION_CANCELED = -1;
    /**
     * Listener for events.
     */
    private OnEditTaskListener listener;
    /**
     * The task which is being edited.
     */
    private TaskList.Task task;

    public EditTask1Fragment() {
        // Required empty public constructor
    }

    /**
     * A factory method.
     * @param listener    The listener.
     * @param task        The task to be edited.
     * @return A new instance of the fragment.
     */
    public static EditTask1Fragment newInstance(OnEditTaskListener listener, TaskList.Task task) {
        EditTask1Fragment fragment = new EditTask1Fragment();
        fragment.setOnEditTaskListener(listener);
        Bundle args = new Bundle();
args.putParcelable(EDIT_TASK_1, task);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Getter for the listener.
     * @return The listener.
     */
    public OnEditTaskListener getOnEditTaskListener() {
        return listener;
    }

    /**
     * Setter for the listener.
     * @param listener    The listener.
     */
    public void setOnEditTaskListener(OnEditTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getResources().getString(R.string.title_edit_task_1));
        }

        if (getArguments().containsKey(EDIT_TASK_1)) {
task = getArguments().getParcelable(EDIT_TASK_1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_task1, container, false);
        loadTaskData(rootView);
        return  rootView;
    }

    /**
     * Load the task's data into the form fields.
     * @param view    The root view.
     */
    protected void loadTaskData(View view) {
        final View a = view;
        ((EditText) a.findViewById(R.id.task_name)).setText(task.getName());
        Spinner prioritySp = ((Spinner) a.findViewById(R.id.task_priority));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySp.setAdapter(adapter);
        switch (task.getPriority()) {
            case TaskList.Task.HIGH_PRIORITY: prioritySp.setSelection(0); break;
            case TaskList.Task.MEDIUM_PRIORITY: prioritySp.setSelection(1); break;
            case TaskList.Task.LOW_PRIORITY: prioritySp.setSelection(2);
        }
        ((EditText) a.findViewById(R.id.task_description)).setText(task.getDetails());
        ((CheckBox) a.findViewById(R.id.task_is_complex)).setChecked(task.isComplex());
        boolean hasDeadline = task.getDeadline() != null;
        CheckBox deadlineChk = (CheckBox) a.findViewById(R.id.task_has_deadline);
        deadlineChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View finishButton = a.findViewById(R.id.finish_button);
                View nextButton = a.findViewById(R.id.next_button);
                if (isChecked) {
finishButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                } else {
finishButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        if (hasDeadline) {
            deadlineChk.setChecked(true);
            ((View) a.findViewById(R.id.finish_button)).setVisibility(View.INVISIBLE);
        } else {
            deadlineChk.setChecked(false);
            ((View) a.findViewById(R.id.next_button)).setVisibility(View.INVISIBLE);
        }
        ((Button) a.findViewById(R.id.finish_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishPressed(v);
            }
        });
        ((Button) a.findViewById(R.id.next_button)).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextPressed(v);
            }
        }));
        ((Button) a.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed(v);
            }
        });
    }

    /**
     * Goes to the date picker screen.
     * @param view    The view pressed.
     */
    public void onNextPressed(View view) {
        updateTask(task);
        listener.onNextStep(task);
    }

    /**
     * Cancels the edition.
     * @param view    The view pressed.
     */
    public void onCancelPressed(View view) {
        listener.onCancel(task);
    }

    /**
     * Finish the edition.
     * @param view    The view pressed.
     */
    public void onFinishPressed(View view) {
updateTask(task);
        listener.onFinish(task);
    }

    /**
     * Updates the data of the task with the form entries.
     * @param task    The task to be updated.
     */
    protected void updateTask(TaskList.Task task) {
        View v = getView();
        task.setName(((EditText) v.findViewById(R.id.task_name)).getText().toString());
        task.setDetails(((EditText) v.findViewById(R.id.task_description)).getText().toString());
task.setComplex(((CheckBox) v.findViewById(R.id.task_is_complex)).isChecked());
        if (((CheckBox) v.findViewById(R.id.task_has_deadline)).isChecked())
            task.setDeadline(Calendar.getInstance().getTime());
        else task.setDeadline(null);
        switch (((Spinner) v.findViewById(R.id.task_priority)).getSelectedItemPosition()) {
            case 0: task.setPriority(TaskList.Task.HIGH_PRIORITY); break;
            case 1: task.setPriority(TaskList.Task.MEDIUM_PRIORITY); break;
            case 2: task.setPriority(TaskList.Task.LOW_PRIORITY);
        }
    }

}

