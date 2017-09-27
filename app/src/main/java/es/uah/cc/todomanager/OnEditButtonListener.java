package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Created by Fjest on 20/09/2017.
 */

/**
 * A listener for edition starting event.
 */
public interface OnEditButtonListener {
    /**
     * Starts the edition procedure.
     * @param task
     */
    void init(TaskList.Task task);
}
