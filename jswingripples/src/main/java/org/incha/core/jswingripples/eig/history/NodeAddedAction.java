package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class NodeAddedAction implements UndoAction {

    private JSwingRipplesEIGNode node;

    /**
     * @param node the node.
     *
     */
    public NodeAddedAction(final JSwingRipplesEIGNode node) {
        super();
        this.node = node;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.UndoAction#undo()
     */
    @Override
    public UndoAction undo() {
        node.getEig().removeNode(node);
        return new NodeRemovedAction(node);
    }
}
