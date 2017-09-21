package cc.uah.es.todomanager;

import cc.uah.es.todomanager.domain.TaskList;

/**
 * Created by Fjest on 13/09/2017.
 */

public interface OnTaskChangedListener {
    void onTaskChanged(TaskList.Task task, int position);
}
