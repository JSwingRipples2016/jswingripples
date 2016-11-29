package org.incha.ui;

import org.incha.core.jswingripples.parser.InteractiveTask;

import javax.swing.JPanel;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Collection;

public abstract class TaskProgressMonitor extends JPanel {

    public Collection<InteractiveTask> threadedTasks = new ArrayList<>();

    public TaskProgressMonitor() {}

    public TaskProgressMonitor(LayoutManager layoutManager) {
        super(layoutManager);
    }

    protected void doCancel() {
        for (InteractiveTask threadedTask : threadedTasks) {
            if (threadedTask.isAlive() && !threadedTask.isInterrupted()) {
                // actually kills the thread
                threadedTask.stop();
            }
            threadedTask.getListener().taskFailure();
        }
    }

    public void addThreadedTask(InteractiveTask task) {
        threadedTasks.add(task);
    }

    /**
     * @param name task name.
     */
    public abstract void setTaskName(String name);
    /**
     * @return task name.
     */
    public abstract String getTaskName();
    /**
     * @param taskName task name.
     * @param max max value.
     */
    public abstract void beginTask(String taskName, int max);
    /**
     * @return maximum value.
     */
    public abstract int getMaximum();
    /**
     * @return progress value.
     */
    public abstract int getProgress();
    /**
     * Notify task finished.
     */
    public abstract void done();
    /**
     * @return check task canceled.
     */
    public abstract boolean isCanceled();
    /**
     * @param value sets the task finished.
     */
    public abstract void setCanceled(boolean value);
    /**
     * @param value new current value.
     */
    public abstract void worked(int value);

    public abstract void setMaximum(int max);
}
