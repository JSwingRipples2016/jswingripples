package org.incha.ui.classview;

import javax.swing.table.TableCellRenderer;

import org.incha.core.JavaProject;
import org.incha.core.search.Searcher;
import org.incha.ui.jripples.JRipplesViewsConstants;

public class ClassTreeView extends AbstractHierarchicalView {
    private static final long serialVersionUID = -725916023414871313L;

    /**
     * Default constructor.
     */
    public ClassTreeView(final JavaProject project) {
        super(project);
        Searcher.getInstance().setClassTreeView(this);
    }
    /* (non-Javadoc)
     * @see org.incha.ui.AbstractHierarchicalView#createModel()
     */
    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel(
                JRipplesViewsConstants.SHORT_NAME_COLUMN_TITLE,
                JRipplesViewsConstants.MARK_COLUMN_TITLE,
                JRipplesViewsConstants.PROBABILITY_COLUMN_TITLE,
                JRipplesViewsConstants.FULL_NAME_COLUMN_TITLE,
                JRipplesViewsConstants.SEARCH_COLUMN_TITLE
                );
    }
    /**
     * @return table cell renderer
     */
    @Override
    protected ClassTreeRenderer createCellRenderer() {
        return new ClassTreeRenderer();
    }
    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createHeaderRenderer()
     */
    @Override
    protected TableCellRenderer createHeaderRenderer() {
        return new ClassTreeHeaderRenderer(this);
    }
}
