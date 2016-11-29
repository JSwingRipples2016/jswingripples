package org.incha.ui.stats;

import org.apache.commons.logging.LogFactory;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

/**
 * Created by Manuel Olguín (molguin@dcc.uchile.cl) on 6/6/16.
 * Part of org.incha.ui.stats.
 */
public abstract class AbstractGraphViewerListener implements ViewerListener{
    private ViewerPipe pipe;
    private boolean loop;
    private Thread pumpt;

    AbstractGraphViewerListener(ViewerPipe pipe)
    {
        this.pipe = pipe;
        this.loop = true;

        pumpt = new Thread(new PumpRunnable());
        pumpt.start();
    }

    public void stopPumpThread()
    {
        this.loop = false;
        try {
            pumpt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewClosed(String viewName) {
    }

    @Override
    public void buttonPushed(String id) {
        LogFactory.getLog(this.getClass()).info("Click: " + id);

    }

    @Override
    public void buttonReleased(String id) {

    }

    private class PumpRunnable implements Runnable
    {

        @Override
        public void run() {
            while(loop)
            {
                pipe.pump();
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
