package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class ProbabilitySetAction implements UndoAction {
    private final JSwingRipplesEIGNode node;
    private final String oldProbability;
    private final String newProbability;

    /**
     * @param node
     * @param oldProbability
     * @param newProbability
     */
    public ProbabilitySetAction(final JSwingRipplesEIGNode node, final String oldProbability,
            final String newProbability) {
        super();
        this.node = node;
        this.oldProbability = oldProbability;
        this.newProbability = newProbability;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.BackAction#undo()
     */
    @Override
    public ProbabilitySetAction undo() {
        node.setProbability(oldProbability);
        return new ProbabilitySetAction(node, newProbability, oldProbability);
    }
}
