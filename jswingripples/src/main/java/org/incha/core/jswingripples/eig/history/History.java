package org.incha.core.jswingripples.eig.history;

import java.util.LinkedList;


public class History {
    private final int MAX_SIZE = 20;
    private final LinkedList<UndoAction> undos = new LinkedList<UndoAction>();
    private final LinkedList<UndoAction> redos = new LinkedList<UndoAction>();

    /**
     * Adds action to history.
     * @param action action.
     */
    public synchronized void add(final UndoAction action) {
        undos.add(action);
        redos.clear();
        if (undos.size() > MAX_SIZE) {
            undos.removeFirst();
        }
    }
    /**
     * Runs undo operation.
     */
    public void undo() {
        UndoAction undo = null;
        synchronized (this) {
            if (undos.size() > 0) {
                undo = undos.removeLast();
            }
        }

        if (undo != null) {
            final UndoAction redo = undo.undo();
            if (redo != null) {
                synchronized (this) {
                    redos.add(redo);
                }
            }
        }
    }
    /**
     * Runs undo operation.
     */
    public void redo() {
        UndoAction redo = null;
        synchronized (this) {
            if (redos.size() > 0) {
                redo = redos.removeLast();
            }
        }

        if (redo != null) {
            final UndoAction undo = redo.undo();
            if (undo != null) {
                synchronized (this) {
                    undos.add(undo);
                }
            }
        }
    }
    public synchronized boolean canUndo() {
        return undos.size() > 0;
    }
    public synchronized boolean canRedo() {
        return redos.size() > 0;
    }
    /**
     * Clears the history.
     */
    public synchronized void clear() {
        undos.clear();
        redos.clear();
    }
}
