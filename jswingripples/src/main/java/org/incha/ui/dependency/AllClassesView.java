package org.incha.ui.dependency;

import javax.swing.JLabel;

import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.classview.AbstractHierarchicalView;
import org.incha.ui.classview.AbstractMemberRenderer;
import org.incha.ui.classview.ClassTreeDataModel;

public class AllClassesView extends AbstractHierarchicalView {
    private static final long serialVersionUID = 8693122992017345093L;

    /**
     * @param project the project.
     * @param nodes the nodes.
     */
    public AllClassesView(final JavaProject project) {
        super(project);
        //hide table header
        setTableHeader(null);
    }
    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createCellRenderer()
     */
    @Override
    protected AbstractMemberRenderer createCellRenderer() {
        return new AbstractMemberRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void renderOtherColumn(final JLabel label, final JSwingRipplesEIGNode member,
                    final int column) {
                //not any other columns is ther
            }
        };
    }
    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createModel()
     */
    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel("");
    }
}
