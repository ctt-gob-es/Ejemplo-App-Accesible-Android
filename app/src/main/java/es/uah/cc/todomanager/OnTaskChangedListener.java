package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Created by Fjest on 13/09/2017.
 */

public interface OnTaskChangedListener {
    void onTaskChanged(TaskList.Task task, int position);
}
