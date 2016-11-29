package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class NodeRemovedAction implements UndoAction {
    private JSwingRipplesEIGNode node;

    /**
     * @param source source node.
     */
    public NodeRemovedAction(final JSwingRipplesEIGNode source) {
        super();
        this.node = source;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.UndoAction#undo()
     */
    @Override
    public UndoAction undo() {
        node.getEig().addNode(node.getNodeIMember());
        return new NodeAddedAction(node);
    }
}
