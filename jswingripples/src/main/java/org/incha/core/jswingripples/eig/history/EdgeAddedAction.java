package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;

public class EdgeAddedAction implements UndoAction {
    /**
     * Source edge.
     */
    private JSwingRipplesEIGEdge edge;

    /**
     * @param source
     */
    public EdgeAddedAction(final JSwingRipplesEIGEdge source) {
        super();
        this.edge = source;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.UndoAction#undo()
     */
    @Override
    public UndoAction undo() {
        edge.getEig().removeEdge(edge);
        return new EdgeRemovedAction(edge);
    }
}
