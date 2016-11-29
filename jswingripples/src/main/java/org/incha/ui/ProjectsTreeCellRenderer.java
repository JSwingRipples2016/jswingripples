package org.incha.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.incha.core.JavaProject;
import org.incha.ui.jripples.JRipplesResources;

public class ProjectsTreeCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = -3335596024920805565L;
    private final ImageIcon folderIcon;
    private final Icon jarIcon;

    /**
     * Default constructor.
     */
    public ProjectsTreeCellRenderer() {
        super();
        this.folderIcon = new ImageIcon(JRipplesResources.getImage("icons/projects/folder.gif"));
        this.jarIcon = new ImageIcon(JRipplesResources.getImage("icons/projects/jar.gif"));
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value,
            final boolean sel, final boolean expanded, final boolean leaf, final int row,
            final boolean hasFocus) {
        final JLabel label = (JLabel) super.getTreeCellRendererComponent(
                tree, value, sel, expanded, leaf, row, hasFocus);
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.getUserObject() instanceof File) {
            final File f = (File) node.getUserObject();
            label.setIcon(f.isDirectory() ? folderIcon : jarIcon);
        } else if (node.getUserObject() instanceof JavaProject) {
            label.setText(((JavaProject) node.getUserObject()).getName());
        }
        return label;
    }
}
