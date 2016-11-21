package org.incha.ui.dependency;

import java.util.Collection;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.classview.ClassTreeDataModel;
import org.incha.ui.classview.ClassTreeRenderer;
import org.incha.ui.classview.ClassTreeView;
import org.incha.ui.classview.HeaderRenderer;
import org.incha.ui.jripples.JRipplesViewsConstants;

@SuppressWarnings("serial")
public class ClassDependencyView extends ClassTreeView {
    private JSwingRipplesEIGNode node;
    private final int callingDirection;
    /**
     * @param project
     */
    public ClassDependencyView(final JavaProject project, final int callingDirection) {
        super(project);
        this.callingDirection = callingDirection;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.classview.ClassTreeView#createCellRenderer()
     */
    @Override
    protected ClassTreeRenderer createCellRenderer() {
        return new ClassTreeRenderer() {
            /* (non-Javadoc)
             * @see org.incha.ui.classview.ClassTreeRenderer#renderOtherColumn(javax.swing.JLabel, org.incha.core.jswingripples.eig.JSwingRipplesEIGNode, int)
             */
            @Override
            protected void renderOtherColumn(final JLabel label,
                    final JSwingRipplesEIGNode neighborNode, final int column) {
                if (column == 2) {
                    label.setIcon(null);

                    final JSwingRipplesEIG eig = node.getEig();
                    JSwingRipplesEIGEdge edge=null;
                    JSwingRipplesEIGEdge edge1=null;

                    if (callingDirection==1) edge=eig.getEdge(node, neighborNode);
                    else if (callingDirection==0) edge=eig.getEdge(neighborNode,node);
                    else if (callingDirection==2) {
                        edge=eig.getEdge(neighborNode,node);
                        edge1=eig.getEdge(node,neighborNode);
                    }
                    if (edge==null && edge1==null) {
                        label.setText("Transitive dependency");
                    } else if (edge!=null && "Custom".equals(edge.getMark())) {
                            label.setText("Custom dependency");
                    } else if (edge1!=null && "Custom".equals(edge1.getMark())) {
                        label.setText("Custom dependency");
                    }
                } else {
                    super.renderOtherColumn(label, node, column);
                };
            }
        };
    }
    /* (non-Javadoc)
     * @see org.incha.ui.classview.ClassTreeView#createModel()
     */
    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel(
                JRipplesViewsConstants.SHORT_NAME_COLUMN_TITLE,
                JRipplesViewsConstants.MARK_COLUMN_TITLE,
                "Dependency notes",
                JRipplesViewsConstants.FULL_NAME_COLUMN_TITLE
            );
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createHeaderRenderer()
     */
    @Override
    protected TableCellRenderer createHeaderRenderer() {
        return new HeaderRenderer(this);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#setData(java.util.Collection)
     */
    @Override
    public void setData(final Collection<JSwingRipplesEIGNode> members) {
        throw new IllegalStateException("Illegal method access, please use setDependencies() instead");
    }
    /**
     * @param node
     * @param set
     */
    public void setDependencies(final JSwingRipplesEIGNode node,
            final Set<JSwingRipplesEIGNode> set) {
        this.node = node;
        super.setData(set);
    }
}
