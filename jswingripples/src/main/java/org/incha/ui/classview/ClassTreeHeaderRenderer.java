package org.incha.ui.classview;

import org.incha.core.ModuleConfiguration;
import org.incha.ui.jripples.JRipplesViewsConstants;

public class ClassTreeHeaderRenderer extends HeaderRenderer {
    /**
     * @param view the table.
     */
    public ClassTreeHeaderRenderer(final AbstractHierarchicalView view) {
        super(view);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.HeaderRenderer#prepareRenderedValue(java.lang.Object, int)
     */
    @Override
    protected Object convertColumnHeader(final Object value, final int column) {
        if (column == 2) {
            return getProbabilityTitle();
        }
        return super.convertColumnHeader(value, column);
    }
    /**
     * @return header of second column.
     */
    private String getProbabilityTitle() {
        final ModuleConfiguration cfg = view.getProject().getModuleConfiguration();
        if (cfg.isAnalysisDefaultImpactSetConnections()) {
            return "Coupling with Impact Set";
        } else {
            return JRipplesViewsConstants.PROBABILITY_COLUMN_TITLE;
        }
    }
}
