package org.incha.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.JavaProject;
import org.incha.core.Statistics;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.StatisticsChangeListener;
import org.incha.ui.stats.HierarchicalView;
import org.incha.ui.util.ModalContext;
import org.incha.ui.util.RunnableWithProgress;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class DefaultController implements StatisticsChangeListener {
    private static final Log log = LogFactory.getLog(DefaultController.class);
    /**
     * Default constructor.
     */
    public DefaultController() {
        super();
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.StatisticsChangeListener#statisticsAdded(java.lang.String, org.incha.core.Statistics)
     */
    @Override
    public void statisticsAdded(final String id, final Statistics stats) {
        openAnalysisView(stats);
    }

    protected void openAnalysisView(final Statistics stats) {
        final JavaProject project = stats.getEIG().getJavaProject();
        if (project != null) {
            project.setCurrentStatistics(stats);
        }
        openClassViewer(stats);
    }

    /**
     * @param stats statistics.
     */
    protected void openClassViewer(final Statistics stats) {
        try {
            ModalContext.run(new RunnableWithProgress() {
                @Override
                public void run(final TaskProgressMonitor monitor) {
                    openView(stats, monitor);

                }
            }, false, JSwingRipplesApplication.getInstance()
                    .getProgressMonitor());
        } catch (final Exception e) {
            log.error(e);
        }
    }

    protected void openView(final Statistics stats, final TaskProgressMonitor monitor) {
        final JavaProject project = stats.getEIG().getJavaProject();
        final List<JSwingRipplesEIGNode> members = getMembers(stats.getEIG());

        monitor.beginTask("Create statistics view", 1);
        final HierarchicalView view;
        try {
            view = new HierarchicalView(project, members);
        } finally {
            monitor.done();
        }

        final JScrollPane comp = new JScrollPane(view);
        comp.getViewport().setBackground(Color.WHITE);
        JSwingRipplesApplication.getInstance().addComponentAsTab(comp, "Project: " + project.getName());
    }

    /**
     * @param eig
     * @return
     */
    private List<JSwingRipplesEIGNode> getMembers(final JSwingRipplesEIG eig) {
        final List<JSwingRipplesEIGNode> list = new LinkedList<JSwingRipplesEIGNode>();
        for (final JSwingRipplesEIGNode node : eig.getAllNodes()) {
            list.add(node);
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.StatisticsChangeListener#statisticsReload(java.lang.String, org.incha.core.Statistics, org.incha.core.Statistics)
     */
    @Override
    public void statisticsReload(final String id, final Statistics oldStats,
            final Statistics newStats) {
        openAnalysisView(newStats);
    }
    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.StatisticsChangeListener#statisticsRemoved(java.lang.String, org.incha.core.Statistics)
     */
    @Override
    public void statisticsRemoved(final String id, final Statistics stats) {
    }
}
