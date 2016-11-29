package org.incha.ui.dependency;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.JSwingRipplesApplication;

public class ShowDependencyAction extends AbstractAction {
    private static final long serialVersionUID = 6537463394107510663L;
    /**
     * Node.
     */
    private final JSwingRipplesEIGNode node;

    /**
     * @param node node.
     */
    public ShowDependencyAction(final JSwingRipplesEIGNode node) {
        super("View dependencies...");
        this.node = node;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final DependencyViewer viewer = new DependencyViewer(node.getEig().getJavaProject());
        viewer.setNode(node);

        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screen.width * 2 / 3;
        final int height = screen.height * 2 / 3;

        viewer.setSize(width, height);

        final JSwingRipplesApplication app = JSwingRipplesApplication.getInstance();
        if (app == null) {
            viewer.setLocation((screen.width - width) / 2, (screen.height - height) / 2);
        } else {
            viewer.setLocationRelativeTo(app);
        }

        viewer.setAlwaysOnTop(true);
        viewer.setVisible(true);
    }
}
