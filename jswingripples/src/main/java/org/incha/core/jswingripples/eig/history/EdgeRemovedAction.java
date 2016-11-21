package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class EdgeRemovedAction implements UndoAction {
    /**
     * The edge.
     */
    private JSwingRipplesEIGEdge edge;

    /**
     * @param source
     */
    public EdgeRemovedAction(final JSwingRipplesEIGEdge source) {
        super();
        this.edge = source;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.UndoAction#undo()
     */
    @Override
    public UndoAction undo() {
        final JSwingRipplesEIG eig = edge.getEig();

        final JSwingRipplesEIGNode fromNode = eig.getNode(edge.getFromNode().getNodeIMember());
        final JSwingRipplesEIGNode toNode = eig.getNode(edge.getToNode().getNodeIMember());

        final JSwingRipplesEIGEdge addedEdge = eig.addEdge(fromNode, toNode);
        return new EdgeAddedAction(addedEdge);
    }
}
