package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class MarkSetAction implements UndoAction {
    private final JSwingRipplesEIGNode node;
    private final String oldMark;
    private final String newMark;

    /**
     * @param node
     * @param oldMark
     * @param newMark
     */
    public MarkSetAction(final JSwingRipplesEIGNode node, final String oldMark,
            final String newMark) {
        super();
        this.node = node;
        this.oldMark = oldMark;
        this.newMark = newMark;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.BackAction#undo()
     */
    @Override
    public UndoAction undo() {
        node.setMark(oldMark);
        return new MarkSetAction(node, newMark, oldMark);
    }
}
