package org.incha.ui.classview;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HeaderRenderer implements TableCellRenderer {
    protected final AbstractHierarchicalView view;
    private final TableCellRenderer renderer;

    /**
     *
     */
    public HeaderRenderer(final AbstractHierarchicalView view) {
        super();
        this.view = view;
        this.renderer = view.getTableHeader().getDefaultRenderer();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        return renderer.getTableCellRendererComponent(table,
                convertColumnHeader(value, column), isSelected, hasFocus, row, column);
    }

    /**
     * @param value
     * @return prepared value to render.
     */
    protected Object convertColumnHeader(final Object value, final int column) {
        return value;
    }
}
