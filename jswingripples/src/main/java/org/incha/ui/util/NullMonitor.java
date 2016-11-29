package org.incha.ui.util;

import org.incha.ui.TaskProgressMonitor;

public class NullMonitor extends TaskProgressMonitor {

    /**
     * Default constructor.
     */
    public NullMonitor() {
        super();
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setTaskName(java.lang.String)
     */
    @Override
    public void setTaskName(final String name) {
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#beginTask(java.lang.String, int)
     */
    @Override
    public void beginTask(final String taskName, final int max) {
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#done()
     */
    @Override
    public void done() {
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#isCanceled()
     */
    @Override
    public boolean isCanceled() {
        return false;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setCanceled(boolean)
     */
    @Override
    public void setCanceled(final boolean value) {
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#worked(int)
     */
    @Override
    public void worked(final int value) {
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getMaximum()
     */
    @Override
    public int getMaximum() {
        return 0;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getProgress()
     */
    @Override
    public int getProgress() {
        return 0;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getTaskName()
     */
    @Override
    public String getTaskName() {
        return "NullMonitor";
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setMaximum(int)
     */
    @Override
    public void setMaximum(final int max) {
    }
}
