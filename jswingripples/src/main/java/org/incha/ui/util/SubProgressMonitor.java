package org.incha.ui.util;

import org.incha.ui.TaskProgressMonitor;


public class SubProgressMonitor extends TaskProgressMonitor {
    private final TaskProgressMonitor monitor;
    private String taskName;
    private String savedName;
    private int savedMax;
    private int savedProgress;
    private int maximum;
    private int progress;

    /**
     * @param monitor progress monitor.
     */
    public SubProgressMonitor(final TaskProgressMonitor monitor) {
        super();
        this.monitor = monitor;
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setTaskName(java.lang.String)
     */
    @Override
    public void setTaskName(final String name) {
        taskName = name;
        monitor.setTaskName(name);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#beginTask(java.lang.String, int)
     */
    @Override
    public void beginTask(final String taskName, final int max) {
        setTaskName(taskName);
        setMaximum(max);

        savedName = monitor.getTaskName();
        savedMax = monitor.getMaximum();
        savedProgress = monitor.getProgress();

        //reset monitor
        monitor.setTaskName(getTaskName());
        monitor.beginTask(taskName, max);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#done()
     */
    @Override
    public void done() {
        monitor.setTaskName(savedName);
        monitor.worked(0);
        monitor.setMaximum(savedMax);
        monitor.worked(savedProgress);
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#isCanceled()
     */
    @Override
    public boolean isCanceled() {
        return monitor.isCanceled();
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setCanceled(boolean)
     */
    @Override
    public void setCanceled(final boolean value) {
        monitor.setCanceled(value);
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#worked(int)
     */
    @Override
    public void worked(final int value) {
        monitor.worked(value);
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getTaskName()
     */
    @Override
    public String getTaskName() {
        return taskName;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getMaximum()
     */
    @Override
    public int getMaximum() {
        return maximum;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getProgress()
     */
    @Override
    public int getProgress() {
        return progress;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setMaximum(int)
     */
    @Override
    public void setMaximum(final int max) {
        this.maximum = max;
    }
}
