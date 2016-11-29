package org.incha.ui.buildpath;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class FileTableModel implements TableModel {
    /**
     * The item list.
     */
    private final List<File> items = new ArrayList<File>();
    /**
     * The list of listeners.
     */
    private final List<TableModelListener> listeners = new LinkedList<TableModelListener>();

    /**
     * Default constructor.
     */
    public FileTableModel() {
        super();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return items.size();
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 1;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int columnIndex) {
        return columnIndex == 0 ? "file" : null;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return File.class;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        checkBounds(rowIndex, columnIndex);
        return items.get(rowIndex);
    }

    /**
     * @param rowIndex
     * @param columnIndex
     */
    public void checkBounds(final int rowIndex, final int columnIndex) {
        if (rowIndex >= items.size() || columnIndex > 0) {
            throw new RuntimeException("Out of data. Row: " + rowIndex
                    + ", column: " + columnIndex);
        }
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        checkBounds(rowIndex, columnIndex);
        items.set(rowIndex, (File) value);
        fireModelChanged(new TableModelEvent(this, rowIndex));
    }
    /**
     * @param value the value to add.
     */
    public void add(final File value) {
        items.add(value);
        final int row = items.size() - 1;
        fireModelChanged(new TableModelEvent(this, row, row,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }
    public void remove(final File value) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            final File item = items.get(i);
            if (item.equals(value)) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            removeRow(index);
        }
    }
    /**
     * @param index
     */
    public void removeRow(final int index) {
        checkBounds(index, 0);
        items.remove(index);
        fireModelChanged(new TableModelEvent(this, index, index,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }
    /**
     * @param e the model event.
     */
    private void fireModelChanged(final TableModelEvent e) {
        for (final TableModelListener l : listeners) {
            l.tableChanged(e);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    @Override
    public void addTableModelListener(final TableModelListener l) {
        listeners.add(l);
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    @Override
    public void removeTableModelListener(final TableModelListener l) {
        listeners.remove(l);
    }

    /**
     *
     */
    public void refresh() {
        final int size = items.size();
        if (size > 0) {
            fireModelChanged(new TableModelEvent(this, 0, size - 1,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
        }
    }
}
