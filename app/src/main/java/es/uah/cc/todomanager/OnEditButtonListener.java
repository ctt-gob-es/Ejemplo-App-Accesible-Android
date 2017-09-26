package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Created by Fjest on 20/09/2017.
 */

public interface OnEditButtonListener {
    void init(TaskList.Task task);
}
