package org.incha.ui.buildpath;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.incha.ui.jripples.JRipplesResources;

public class ClassPathEntryRenderer extends DirectoryCellRenderer {
    private static final long serialVersionUID = -5441338261158548567L;
    private final Icon jarIcon;

    public ClassPathEntryRenderer() {
        this.jarIcon = new ImageIcon(JRipplesResources.getImage("icons/projects/jar.gif"));
    }

    /* (non-Javadoc)
     * @see org.incha.ui.DirectoryCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        final JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        final File f = (File) value;
        if (f != null && f.isFile()) {
            label.setIcon(jarIcon);
        }
        return label;
    }
}
