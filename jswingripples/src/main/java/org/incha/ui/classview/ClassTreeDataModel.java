package org.incha.ui.classview;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.eclipse.jdt.core.IMember;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class ClassTreeDataModel implements TableModel {
    /**
     * The item list.
     */
    private final List<JSwingRipplesEIGNode> items = new LinkedList<JSwingRipplesEIGNode>();

    /**
     * The list of listeners.
     */
    private final List<TableModelListener> listeners = new LinkedList<TableModelListener>();

    private final String[] columns;

    /**
     * @param eig the EIG.
     */
    public ClassTreeDataModel(final String... columns) {
        super();
        this.columns = columns;
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
        return columns.length;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int columnIndex) {
        return columns[columnIndex];
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return IMember.class;
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
    public JSwingRipplesEIGNode getValueAt(final int rowIndex, final int columnIndex) {
        checkBounds(rowIndex, columnIndex);
        return items.get(rowIndex);
    }
    /**
     * @param row the row index.
     * @return
     */
    public JSwingRipplesEIGNode getValueAt(final int row) {
        return getValueAt(row, 0);
    }
    /**
     * @param rowIndex
     * @param columnIndex
     */
    public void checkBounds(final int rowIndex, final int columnIndex) {
        if (rowIndex >= items.size() || columnIndex >= columns.length) {
            throw new RuntimeException("Out of data. Row: " + rowIndex
                    + ", column: " + columnIndex);
        }
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        throw new IllegalAccessError("Unimplemented. Use add instead");
    }
    /**
     * @param rember the value to add.
     * @param pos position to insert.
     */
    public void add(final JSwingRipplesEIGNode rember, final int pos) {
        if (pos >= items.size()) {
            items.add(rember);
            final int row = items.size() - 1;
            fireModelChanged(new TableModelEvent(this, row , row,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        } else {
            items.add(pos, rember);
            final int row = items.size() - 1;
            fireModelChanged(new TableModelEvent(this, pos, row,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        }
    }
    /**
     * @param members
     * @param pos
     */
    public void addAll(final JSwingRipplesEIGNode[] members, final int pos) {
        if (members.length == 0) {
            return;
        }

        final int oldSize = items.size();
        if (pos >= items.size()) {
            for (final JSwingRipplesEIGNode iType : members) {
                items.add(iType);
            }
            fireModelChanged(new TableModelEvent(this, oldSize , items.size() - 1,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        } else {
            for (int i = 0; i < members.length; i++) {
                items.add(i + pos, members[i]);
            }
            fireModelChanged(new TableModelEvent(this, pos, pos + members.length -1,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        }
    }
    /**
     * @param value the value to delete
     */
    public void remove(final JSwingRipplesEIGNode value) {
        final Iterator<JSwingRipplesEIGNode> iter = items.iterator();
        while (iter.hasNext()) {
            if (iter.next() == value) {
                iter.remove();
                break;
            }
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
    /**
     * @param type
     * return index of given type.
     */
    public int indexOf(final JSwingRipplesEIGNode type) {
        for (int i = 0; i < items.size(); i++) {
            final JSwingRipplesEIGNode item = items.get(i);
            if (item == type) {
                return i;
            }
        }

        return -1;
    }
    /**
     * @param index
     */
    public void clear() {
        final int size = items.size();
        if (size > 0) {
            items.clear();
            fireModelChanged(new TableModelEvent(this, 0, size,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        }
    }
}
