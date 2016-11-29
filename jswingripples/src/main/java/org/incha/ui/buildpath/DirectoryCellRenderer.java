package org.incha.ui.buildpath;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.incha.ui.jripples.JRipplesResources;

public class DirectoryCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    private final ImageIcon icon;

    public DirectoryCellRenderer() {
        this.icon = new ImageIcon(JRipplesResources.getImage("icons/projects/folder.gif"));
    }

    /* (non-Javadoc)
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        final JLabel comp = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        comp.setIcon(icon);
        return comp;
    }
}
