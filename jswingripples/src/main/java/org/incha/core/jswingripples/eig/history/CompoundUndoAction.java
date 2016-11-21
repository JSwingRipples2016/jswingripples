package org.incha.core.jswingripples.eig.history;

import java.util.LinkedList;


public class CompoundUndoAction implements UndoAction {
    /**
     * List of sub actions.
     */
    private final LinkedList<UndoAction> actions = new LinkedList<UndoAction>();

    /**
     * Default constructor.
     */
    public CompoundUndoAction() {
        super();
    }
    /**
     * @param a add action to batch.
     */
    public void add(final UndoAction a) {
        actions.add(a);
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.BackAction#undo()
     */
    @Override
    public CompoundUndoAction undo() {
        final CompoundUndoAction action = new CompoundUndoAction();
        for (final UndoAction undo : actions) {
            final UndoAction redo = undo.undo();
            if (redo != null) {
                action.actions.addFirst(redo);
            }
        }
        return action;
    }
}
