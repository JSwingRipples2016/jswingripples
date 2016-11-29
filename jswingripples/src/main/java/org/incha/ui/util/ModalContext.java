package org.incha.ui.util;

import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.ui.TaskProgressMonitor;

public final class ModalContext {
    private static final Log log = LogFactory.getLog(ModalContext.class);
    /**
     * Default constructor.
     */
    private ModalContext() {
        super();
    }

    /**
     * @param runnable
     * @param fork
     * @param progressMonitor
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public static void run(final RunnableWithProgress runnable, final boolean fork,
            final TaskProgressMonitor progressMonitor)
                    throws InvocationTargetException, InterruptedException {
        if (!fork) {
            if (!SwingUtilities.isEventDispatchThread()) {
                runnable.run(progressMonitor);
            } else {
                startModalContextThread(runnable, progressMonitor);
            }
        } else {
            new Thread("Modal Context Tread") {
                @Override
                public void run() {
                    try {
                        runnable.run(progressMonitor);
                    } catch (final Exception e) {
                        log.error("Failed to run the runnable in modal mode", e);
                    }
                }
            }.start();
        }
    }

    /**
     * @param runnable the runnable to run.
     * @param progressMonitor progress monitor.
     * @param monitor the mutex for wait/notify operations.
     */
    private static void startModalContextThread(
            final RunnableWithProgress runnable,
            final TaskProgressMonitor progressMonitor) {
        final AtomicBoolean isExit = new AtomicBoolean(false);
        final SecondaryLoop loop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();

        new Thread("Modal Context Tread") {
            @Override
            public void run() {
                try {
                    runnable.run(progressMonitor);
                } catch (final Exception e) {
                    log.error("Failed to run the runnable in modal mode", e);
                } finally {
                    isExit.set(true);
                    loop.exit();
                }
            }
        }.start();

        //if not the modal thread i already finished, should itterupt it
        if (!isExit.get()) {
            loop.enter();
        }
    }
}
