package org.incha.ui.dependency;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.nio.file.FileSystem;

import javax.swing.AbstractAction;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.JSwingRipplesApplication;

public class OpenWithIDEAction extends AbstractAction {
    private static final long serialVersionUID = 6537463394107510663L;
    /**
     * Node.
     */
    private final JSwingRipplesEIGNode node;

    /**
     * @param node node.
     */
    public OpenWithIDEAction(final JSwingRipplesEIGNode node) {
        super("Open with IDE");
        this.node = node;
        this.enabled = false;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        
    }
}
