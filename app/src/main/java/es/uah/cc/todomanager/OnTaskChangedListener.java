package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Created by Fjest on 13/09/2017.
 * A listener to do some actions when a task changes.
 */
public interface OnTaskChangedListener {
    void onTaskChanged(TaskList.Task task, int position);
    void onSave(TaskList.Task task, int pos);
}
