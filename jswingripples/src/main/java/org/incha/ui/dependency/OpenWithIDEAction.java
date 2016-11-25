package org.incha.ui.dependency;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.incha.core.JavaProject;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.utils.FileVisitor;
import org.incha.utils.IoUtils;
import java.util.regex.Matcher;

public class OpenWithIDEAction extends AbstractAction {
    private static final long serialVersionUID = 6537463394107510663L;
    private final JSwingRipplesEIGNode node;

    /**
     * @param node node.
     */
    public OpenWithIDEAction(final JSwingRipplesEIGNode node) {
        super("Open with Editor");
        this.node = node;
    }
    
    
    public void showErrorMessage(){
        JOptionPane.showMessageDialog(null, "It is not possible to open the file with the default editor.");
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        try{
            
            File file = new File(node.getAbsolutePath().replaceAll("/", Matcher.quoteReplacement(File.separator)));
            Desktop desktop = Desktop.getDesktop();
            if (!Desktop.isDesktopSupported()) {
                showErrorMessage();
            }
            
            if (desktop.isSupported(Desktop.Action.EDIT)) {
                desktop.edit(file);
            } else if (desktop.isSupported(Desktop.Action.OPEN)){
                desktop.edit(file);
            }
            
        } catch (Exception ex){
            showErrorMessage();
        }
    }
}
