package cc.uah.es.todomanager;

import cc.uah.es.todomanager.domain.TaskList;

/**
 * This interface must be implemented by activities that contain edit task
 * fragment to allow an interaction in these fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnEditTaskListener {
    void onNextStep(TaskList.Task task);
    void onPreviousStep(TaskList.Task task);
    void onCancel(TaskList.Task task);
    void onFinish(TaskList.Task task);
}
