package org.incha.ui;

import org.incha.ui.jripples.JRipplesResources;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;


public class ProgressMonitorImpl extends TaskProgressMonitor {
    private static final long serialVersionUID = -3979008622445178343L;

    private int maximum;
    private final JLabel text = new JLabel();
    private final JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    private final JButton cancel = new JButton();
    private String taskName;
    private int progress;
    private AtomicBoolean isCanceled = new AtomicBoolean();

    /**
     * Default constructor.
     */
    public ProgressMonitorImpl() {
        super(new BorderLayout(3, 0));

        cancel.setIcon(new ImageIcon(JRipplesResources.getImage("icons/progress_stop.gif")));

        //status panel
        final JPanel left = new JPanel(new BorderLayout());
        left.add(text, BorderLayout.CENTER);
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(left, BorderLayout.WEST);

        ///update status label size
        text.setHorizontalTextPosition(JLabel.LEFT);
        text.setText("Abra Kadabra prvbbb alklklkjsAbra Kadabra prvbbb alklklkjs");
        text.setPreferredSize(text.getPreferredSize());
        text.setMinimumSize(text.getMinimumSize());
        text.setText("Done");

        //progres bar
        progressBar.setMinimum(0);
        add(progressBar, BorderLayout.CENTER);

        //cancel button
        add(cancel, BorderLayout.EAST);
        cancel.setEnabled(false);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doCancel();
            }
        });
    }

    /**
     *
     */
    protected void doCancel() {
        super.doCancel(); // interrupts the task thread!
        isCanceled.set(true);
        cancel.setEnabled(false);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setTaskName(java.lang.String)
     */
    @Override
    public void setTaskName(final String name) {
        if (!isCanceled()) {
            this.taskName = name;
            text.setText(taskName);
        }
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#getTaskName()
     */
    @Override
    public String getTaskName() {
        return taskName;
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#beginTask(java.lang.String, int)
     */
    @Override
    public void beginTask(final String taskName, final int max) {
        setTaskName(taskName);
        setMaximum(max);
        setTaskName(taskName);
        isCanceled.set(false);
        cancel.setEnabled(true);
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
     * @see org.incha.ui.core.TaskProgressMonitor#done()
     */
    @Override
    public void done() {
        progressBar.setValue(getMaximum());
        text.setText("Done");
        progressBar.setValue(0);
        cancel.setEnabled(false);
        repaint();
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#isCanceled()
     */
    @Override
    public boolean isCanceled() {
        return isCanceled.get();
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setCanceled(boolean)
     */
    @Override
    public void setCanceled(final boolean value) {
        if (value) {
            doCancel();
        }
    }
    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#worked(int)
     */
    @Override
    public void worked(final int value) {
        if (!isCanceled()) {
            progressBar.setValue(value);
        }
    }

    /* (non-Javadoc)
     * @see org.incha.ui.core.TaskProgressMonitor#setMaximum(int)
     */
    @Override
    public void setMaximum(final int max) {
        this.maximum = max;
        progressBar.setMaximum(max);
    }
}
