package es.uah.cc.todomanager.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
        import java.util.Calendar;

/**
 * A task list using singleton pattern.
 */
public class TaskList {

    private Map<Long, Task> tasks;
    private List<Task> taskList;
    private long idSerial   ;
    private static TaskList instance;
    private static boolean isInitialized = false;

    private TaskList() {
        tasks = new HashMap<Long, Task>(10);
        taskList = new ArrayList<Task>();
        idSerial = 0;
    }

    /**
     * Singleton pattern.
     *
     * @return A singleton instance.
     */
    public static TaskList getInstance() {
        if (instance == null) instance = new TaskList();
        return instance;
    }

    /**
     * Gets the task list.
     * @return A list.
     */
    public List<Task> getTasks() {
        return taskList;
    }

    /**
     * Gets the task identified by the introduced id.
     * @param id    The id of the task to get.
     * @return
     */
    public Task getTask(long id) {
        return tasks.get(id);
    }

    /**
     * Updates a task if it is in the list.
     * @param task    The task to update.
     * @return True if it was updated, false else.
     */
    public boolean setTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            taskList.set(taskList.indexOf(task), task);
            return true;
        }
        else return false;
    }

    /**
     * Adds a new task to the list.
     * @param name The title of the task.
     * @param details     The description of the task.
     * @param priority    The priority of the task.
     * @param deadline    The deadline date.
     * @param complex     Whether the task is complex or not.
     */
    public void addTask(String name, String details, String priority, Date deadline, boolean complex) {
        Task t = new Task(idSerial, name, details, priority, deadline, complex);
tasks.put(idSerial++, t);
        taskList.add(t);
    }

    /**
     * Adds a new task to the list.
     * @param task    The task to add.
     */
    public void addTask(Task task) {
        addTask(task.getName(), task.getDetails(), task.getPriority(), task.getDeadline(), task.isComplex());
    }

    /**
     * A method to fill the list with sample data.
     */
    public static void fillSampleData(TaskList tasks) {
        if (!isInitialized) {
            tasks.addTask("Pasear al perro", "Darle una vuelta de 15 minutos para que haga sus cositas", Task.MEDIUM_PRIORITY, null, false);
            tasks.addTask("Envolver el regalo de Ana", "Comprar el papel de regalo y ponerle un lacito bonito.", Task.LOW_PRIORITY, Calendar.getInstance().getTime(), false);
            tasks.addTask("Terminar la redacción de historia", "Investigar sobre los Reyes Católicos y resumir su reinado.", Task.HIGH_PRIORITY, Calendar.getInstance().getTime(), true);
            tasks.addTask("Ir a ver Star Wars", "Quedar con Pedro para ir a ver la nueva película de Star Wars al cine.", Task.HIGH_PRIORITY, null, false);
            tasks.getTask(1l).complete();
            tasks.getTask(3l).cancel();
            tasks.getTask(2l).setCompleted(40);
isInitialized = true;
        }
    }

    /**
     * /**
     * A task to achieve.
     */
    public static class Task implements Parcelable {

        private long id;
        private String name;
        private String details;
        private String priority;
        private Date deadline;
        private boolean complex;
        private int completed;
        private TaskStatus status;

        public static final Parcelable.Creator CREATOR = new TaskCreator();
        public static final String LOW_PRIORITY = "low_priority";
        public static final String MEDIUM_PRIORITY = "medium_priority";
        public static final String HIGH_PRIORITY = "high_priority";

        /**
         * Empty constructor.
         */
        public Task() {
            name = "";
priority = MEDIUM_PRIORITY;
            status = new PendingTask();
            details = "";
            complex = false;
            deadline = null;
            completed = 0;
        }

        /**
         * Constructor to fill a task.
         * @param id The task id
         * @param name The title of the task.
         * @param details The description of the task.
         * @param priority the priority of the task.
         * @param deadline The date of deadline.
         * @param complex Whether the task is complex or not.
         */
        public Task(long id, String name, String details, String priority, Date deadline, boolean complex) {
            this.id = id;
            this.name = name;
            this.details = details;
            this.priority = priority;
            this.complex = complex;
            this.deadline = deadline;
            completed = 0;
            status = new PendingTask();
        }

        /**
         * Constructor from a parcel.
         * @param in    the parcel.
         */
        public Task(Parcel in) {
            readFromParcel(in);
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public Date getDeadline() {
            return deadline;
        }

        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        public boolean isComplex() {
            return complex;
        }

        public void setComplex(boolean complex) {
            this.complex = complex;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public TaskStatus getStatus() {
            return status;
        }

        public void setStatus(TaskStatus status) {
            this.status = status;
        }

        public void complete() {
            status.complete(this);
        }

        public void cancel() {
            status.cancel(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Task task = (Task) o;

            return id == task.id;

        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
dest.writeLong(id);
            dest.writeString(name);
            dest.writeString(details);
            dest.writeString(priority);
            boolean[] temp = {complex};
            dest.writeBooleanArray(temp);
            dest.writeInt(completed);
            dest.writeString(status.getStatusDescription());
            long[] timestamp = new long[2];
            if (deadline != null) {
                timestamp[0] = 1;
                timestamp[1] = deadline.getTime();
            } else  {
timestamp[0] = 0;
                timestamp[1] = 0;
            }
            dest.writeLongArray(timestamp);
        }

        protected void readFromParcel(Parcel in) {
            id = in.readLong();
            name = in.readString();
            details = in.readString();
            priority = in.readString();
            boolean[] temp = new boolean[1];
            in.readBooleanArray(temp);
            complex = temp[0];
            completed = in.readInt();
            String s = in.readString();
            switch (s) {
                case PendingTask.STATUS: status = new PendingTask(); break;
                case CompletedTask.STATUS: status = new CompletedTask(); break;
                case CanceledTask.STATUS: status = new CanceledTask();
            }
                long[] timestamp = new long[2];
            in.readLongArray(timestamp);
                if (timestamp[0] == 1) deadline = new Date(timestamp[1]);
            else deadline = null;
        }

        @Override
        public String toString() {
            return name;
        }

    public static class  TaskCreator implements  Parcelable.Creator<Task> {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    }
    }

    /**
     * Defining a state pattern for the tasks.
     */
    public static interface TaskStatus {
        /**
         * Completes the task.
         * @param task    The task.
         */
        void complete(TaskList.Task task);

        /**
         * Cancels the task.
         * @param task    The task.
         */
        void cancel(TaskList.Task task);

        /**
         * A string representing the status of the task.
         * @return
         */
        String getStatusDescription();
    }

    /**
     * A pending task status.
     */
    public static class PendingTask implements TaskStatus {
        public static final String STATUS = "pending_task";
        @Override
        public void complete(TaskList.Task task) {
            task.setStatus(new CompletedTask());
            task.setCompleted(100);
        }

        @Override
        public void cancel(TaskList.Task task) {
            task.setStatus(new CanceledTask());
        }

        @Override
        public String getStatusDescription() {
            return STATUS;
        }
    }

    /**
     * A completed task status.
     */
    public static class CompletedTask implements TaskStatus {
        public static final String STATUS = "completed_task";

        @Override
        public void complete(TaskList.Task task) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void cancel(TaskList.Task task) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStatusDescription() {
            return STATUS;
        }
    }

    /**
     * A cancelled task status.
     */
    public static class CanceledTask implements TaskStatus {
        public static final String STATUS = "canceled_task";

        @Override
    public void complete(TaskList.Task task) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void cancel(TaskList.Task task) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStatusDescription() {
            return STATUS;
        }
    }

    }
