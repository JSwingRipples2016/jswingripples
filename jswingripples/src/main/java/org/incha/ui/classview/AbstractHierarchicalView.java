package org.incha.ui.classview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public abstract class AbstractHierarchicalView extends JTable {
    private static final long serialVersionUID = -725916023414871313L;
    /**
     * The member hierarchy support.
     */
    private MemberHierarchySupport support = new MemberHierarchySupport(
            new LinkedList<JSwingRipplesEIGNode>());
    private Map<JSwingRipplesEIGNode, Boolean> expandedStates = new HashMap<JSwingRipplesEIGNode, Boolean>();
    private final JavaProject project;

    /**
     * Default constructor.
     */
    public AbstractHierarchicalView(final JavaProject project) {
        super();
        this.project = project;
        setModel(createModel());

        setAutoCreateColumnsFromModel(true);
        setColumnSelectionAllowed(false);
        setShowHorizontalLines(false);
        setDragEnabled(false);
        setRowHeight(20);

        final TableCellRenderer cellRenderer = createCellRenderer();
        final TableCellRenderer headerRenderer = createHeaderRenderer();

        //set renderer to all columns
        final int count = getColumnCount();
        for (int i = 0; i < count; i++) {
            final TableColumn column = getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer);
            if (headerRenderer != null) {
                column.setHeaderRenderer(headerRenderer);
            }
        }

        addMouseListener(new MouseAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    expandOrCollapse(e.getX(), e.getY());
                }
            }
        });
    }
    /**
     * @return
     */
    protected TableCellRenderer createHeaderRenderer() {
        return new HeaderRenderer(this);
    }
    public void setData(final Collection<JSwingRipplesEIGNode> members) {
        getClassHierarchyModel().clear();
        support = new MemberHierarchySupport(members);

        //add data from provider
        final JSwingRipplesEIGNode[] types = support.getRootTypes();
        Arrays.sort(types, new MemberComparator());

        getClassHierarchyModel().addAll(types, 0);
    }

    /**
     * @return
     */
    protected abstract ClassTreeDataModel createModel();
    /**
     * @return table cell renderer
     */
    protected abstract AbstractMemberRenderer createCellRenderer();

    public boolean isExpanded(final JSwingRipplesEIGNode member) {
        return Boolean.TRUE == expandedStates.get(member);
    }
    /**
     * @return
     */
    public ClassTreeDataModel getClassHierarchyModel() {
        return (ClassTreeDataModel) getModel();
    }
    /**
     * @param x mouse x coordinate.
     * @param y mouse y coordinate.
     */
    protected void expandOrCollapse(final int x, final int y) {
        final ClassTreeDataModel model = getClassHierarchyModel();
        final JSwingRipplesEIGNode m = getSelectedItem(x, y);

        if(m != null && hasChildren(m)) {
            int row = rowAtPoint(new Point(x, y));
            final Rectangle rect = getCellRect(row, 0, true);
            if (!rect.contains(x, y)
                    || x < getHierarchyOffset(m)
                    || x > getHierarchyOffset(m) + 16) {
                return;
            }

            if (isExpanded(m)) {
                expandedStates.put(m, Boolean.FALSE);
                final int depth = getHierarchyDepth(m);
                //collapse node
                row++;
                while (row < getRowCount() && depth < getHierarchyDepth(model.getValueAt(row, 0))) {
                    model.removeRow(row);
                }
            } else {
                expandedStates.put(m, Boolean.TRUE);
                //expand
                final JSwingRipplesEIGNode[] members = support.getChildren(m);
                Arrays.sort(members, new MemberComparator());
                model.addAll(members, row + 1);
            }

            repaint();
        }
    }
    /**
     * @param x
     * @param y
     * @return
     */
    protected JSwingRipplesEIGNode getSelectedItem(final int x, final int y) {
        final int row = rowAtPoint(new Point(x, y));
        if (row < 0) {
            return null;
        }
        final ClassTreeDataModel model = getClassHierarchyModel();
        return model.getValueAt(row, 0);
    }
    /**
     * @param member
     * @return
     */
    public boolean hasChildren(final JSwingRipplesEIGNode member) {
        return support.hasChildren(member);
    }
    /**
     * @param member
     * @return
     */
    public int getHierarchyDepth(final JSwingRipplesEIGNode member) {
        return support.getHierarchyDepth(member);
    }
    /**
     * @param member
     * @return
     */
    public int getHierarchyOffset(final JSwingRipplesEIGNode member) {
        return 20 * getHierarchyDepth(member);
    }
    /**
     * @return the project
     */
    public JavaProject getProject() {
        return project;
    }
}
