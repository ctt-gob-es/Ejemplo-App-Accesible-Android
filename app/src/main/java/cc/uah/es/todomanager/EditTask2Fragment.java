package cc.uah.es.todomanager;


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

import cc.uah.es.todomanager.domain.TaskList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTask2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTask2Fragment extends Fragment {

    public static final String TAG = "es.uah.cc.todomanager.EditTask2Fragment";
    private TaskList.Task task;
    private OnEditTaskListener listener;

    public EditTask2Fragment() {
        // Required empty public constructor
    }

    public OnEditTaskListener getOnEditTaskListener() {
        return listener;
    }

    public void setOnEditTaskListener(OnEditTaskListener listener) {
        this.listener = listener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTask2Fragment.
     */
    public static EditTask2Fragment newInstance(OnEditTaskListener listener, TaskList.Task task) {
        EditTask2Fragment fragment = new EditTask2Fragment();
        Bundle args = new Bundle();
        args.putParcelable(NewTaskActivity.ARG_NEW_TASK2, task);
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

        if (savedInstanceState == null) {
            task = getArguments().getParcelable(TaskListActivity.ARG_TASK);
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

    protected void setup(View view) {
        final View v = view;
        DatePicker picker = (DatePicker) v.findViewById(R.id.task_deadline);
        picker.setMinDate(Calendar.getInstance().getTimeInMillis());
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

protected void onFinishPressed(View v) {
    listener.onFinish(task);
}

protected void onCancelPressed(View v) {
    listener.onCancel(task);
}

protected  void  onPreviousPressed(View v) {
listener.onPreviousStep(task);
}

}
