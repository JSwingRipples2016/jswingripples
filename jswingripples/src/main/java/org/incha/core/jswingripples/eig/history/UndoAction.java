package org.incha.core.jswingripples.eig.history;


public interface UndoAction {
    /**
     * @return reverse action (redo). Can be null if no back action supported
     */
    UndoAction undo();
}
