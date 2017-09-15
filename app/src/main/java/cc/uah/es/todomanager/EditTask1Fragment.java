package cc.uah.es.todomanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import cc.uah.es.todomanager.domain.TaskList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditTask1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditTask1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTask1Fragment extends Fragment {
    public static final int ACTIVITY_CODE = 2;
    public static final int TASK_CREATION_COMPLETED = 1;
    public static final int TASK_CREATION_CANCELED = 0;

    private OnEditTaskListener listener;
    private TaskList.Task task;

    public EditTask1Fragment() {
        // Required empty public constructor
    }

    public TaskList.Task getTask() {
        return task;
    }

    public void setTask(TaskList.Task task) {
        this.task = task;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided listener.
     *
     * @param listener A listener for on new task next step event.
     *                 @param task A task for working on it.
     * @return A new instance of fragment EditTask1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTask1Fragment newInstance(OnEditTaskListener listener, TaskList.Task task) {
        EditTask1Fragment fragment = new EditTask1Fragment();
        fragment.setOnEditTaskListener(listener);
        fragment.setTask(task);
        return fragment;
    }

    public OnEditTaskListener getOnEditTaskListener() {
        return listener;
    }

    public void setOnEditTaskListener(OnEditTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_task1, container, false);
        loadTaskData(rootView);
        return  rootView;
    }

    protected void loadTaskData(View a) {
        // View a = getView();
        ((EditText) a.findViewById(R.id.task_name)).setText(task.getName());
        /* Spinner prioritySp = ((Spinner) a.findViewById(R.id.task_priority));
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
        if (hasDeadline) deadlineChk.setChecked(false);
        else deadlineChk.setChecked(true);
        */
    }

    public void onNextPressed(View view) {
        listener.onNextStep(task);
    }

    public void onCancelPressed(View view) {
        listener.onCancel();
    }

    public void onFinishPressed(View view) {
listener.onFinish(task);
    }

}

