package cc.uah.es.todomanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.uah.es.todomanager.domain.TaskList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTask1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTask1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTask1Fragment extends Fragment {
    public static final int ACTIVITY_CODE = 2;
    public static final int TASK_CREATION_COMPLETED = 1;
    public static final int TASK_CREATION_CANCELED = 0;

    private OnNewTaskListener listener;
    private TaskList.Task task;

    public NewTask1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided listener.
     *
     * @param listener A listener for on new task next step event.
     * @return A new instance of fragment NewTask1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTask1Fragment newInstance(OnNewTaskListener listener) {
        NewTask1Fragment fragment = new NewTask1Fragment();
        fragment.setOnNewTaskListener(listener);
        return fragment;
    }

    public OnNewTaskListener getOnNewTaskListener() {
        return listener;
    }

    public void setOnNewTaskListener(OnNewTaskListener listener) {
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
        return inflater.inflate(R.layout.fragment_edit_task1, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            // listener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewTaskListener {
        void onNewTaskNextStep(TaskList.Task task);
        void onNewTaskPreviousStep(TaskList.Task task);
        void onNewTaskCancel();
        void onNewTaskFinish(TaskList.Task task);
    }

}

