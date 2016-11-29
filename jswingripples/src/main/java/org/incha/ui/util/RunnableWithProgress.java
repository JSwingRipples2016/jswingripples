package org.incha.ui.util;

import org.incha.ui.TaskProgressMonitor;

public interface RunnableWithProgress {
    /**
     * @param monitor
     */
    void run(TaskProgressMonitor monitor);
}
