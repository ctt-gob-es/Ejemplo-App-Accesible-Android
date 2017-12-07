package es.uah.cc.todomanager;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import es.uah.cc.todomanager.R;
import es.uah.cc.todomanager.domain.TaskList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTask2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTask2Fragment extends Fragment {

    public static final String TAG = "es.uah.cc.todomanager.EditTask2Fragment";
    /**
     * The key for transactions and task data in the arguments.
     */
    public static final String EDIT_TASK_2 = "es.uah.cc.todomanager.edittask2";
    /**
     * The task which is being edited.
     */
    private TaskList.Task task;
    /**
     * The listener for events.
     */
    private OnEditTaskListener listener;

    public EditTask2Fragment() {
        // Required empty public constructor
    }

    /**
     * Getter for OnEditTaskListener.
     * @return The listener.
     */
    public OnEditTaskListener getOnEditTaskListener() {
        return listener;
    }

    /**
     * Setter for OnEditTaskListener.
     * @param listener
     */
    public void setOnEditTaskListener(OnEditTaskListener listener) {
        this.listener = listener;
    }

    /**
     * Factory method.
     * @param listener    The listener to use.
     * @param task        The task to be edited.
     * @return A new instance of the fragment.
     */
    public static EditTask2Fragment newInstance(OnEditTaskListener listener, TaskList.Task task) {
        EditTask2Fragment fragment = new EditTask2Fragment();
        Bundle args = new Bundle();
        args.putParcelable(EDIT_TASK_2, task);
        fragment.setArguments(args);
        fragment.setOnEditTaskListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getResources().getString(R.string.title_edit_task_2));
        }

        if (getArguments().containsKey(EDIT_TASK_2)) {
            task = getArguments().getParcelable(EDIT_TASK_2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_task2, container, false);
setup(rootView);
        return rootView;
    }

    /**
     * Shows the date picker and setups the rest of the layout.
     * @param view
     */
    protected void setup(View view) {
        final View v = view;
        DatePicker picker = (DatePicker) v.findViewById(R.id.task_deadline);
        picker.setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        final Date d = task.getDeadline();
        picker.init(d.getYear(), d.getMonth(), d.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                task.setDeadline(c.getTime());
            }
        });
        ((Button) v.findViewById(R.id.finish_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishPressed(v);
            }
        });
        ((Button) v.findViewById(R.id.previous_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousPressed(v);
            }
        });
        ((Button) v.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed(v);
            }
        });
    }

    /**
     * Finish the edition.
     * @param v    The view pressed.
     */
    protected void onFinishPressed(View v) {
    listener.onFinish(task);
}

    /**
     * Cancels the edition of the task.
     * @param v    The view pressed.
     */
    protected void onCancelPressed(View v) {
    listener.onCancel(task);
}

    /**
     * Goes to the previous screen.
     * @param v    The view pressed.
     */
    protected  void  onPreviousPressed(View v) {
listener.onPreviousStep(task);
}

}
