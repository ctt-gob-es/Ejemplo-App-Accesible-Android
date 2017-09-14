package cc.uah.es.todomanager.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
        import java.util.Calendar;

public class TaskList {

    private Map<Long, Task> tasks;
    private long idSerial   ;
    private static TaskList instance;
    private static boolean isInitialized = false;

    private TaskList() {
        tasks = new HashMap<Long, Task>(10);
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

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Task getTask(long id) {
        return tasks.get(id);
    }

    public void addTask(String name, String details, String priority, Date deadline, boolean complex) {
tasks.put(idSerial, new Task(idSerial++, name, details, priority, deadline, complex));
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
    public static class Task {

        private long id;
        private String name;
        private String details;
        private String priority;
        private Date deadline;
        private boolean complex;
        private int completed;
        private TaskStatus status;

        public static final String LOW_PRIORITY = "low_priority";
        public static final String MEDIUM_PRIORITY = "medium_priority";
        public static final String HIGH_PRIORITY = "high_priority";

        public Task() {
            name = "";
priority = MEDIUM_PRIORITY;
            status = new PendingTask();
            details = "";
            complex = false;
            deadline = null;
            completed = 0;
        }

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
        public String toString() {
            return name;
        }
    }

    /**
     * Defining a state pattern for the tasks.
     */
    public static interface TaskStatus {
        void complete(TaskList.Task task);

        void cancel(TaskList.Task task);

        String getStatusDescription();
    }

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
