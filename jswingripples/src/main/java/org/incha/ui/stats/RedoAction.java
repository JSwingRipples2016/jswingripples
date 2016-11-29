package org.incha.ui.stats;

import org.incha.core.jswingripples.eig.history.History;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {
    private static final long serialVersionUID = 1416226650246264539L;
    private History undoable;
    private final Component context;

    /**
     * @param undoable
     * @param name
     */
    public RedoAction(final History undoable, final String name, final Component context) {
        super(name);
        this.undoable = undoable;
        setEnabled(undoable.canRedo());
        this.context = context;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        undoable.redo();
        context.repaint();
    }
}
