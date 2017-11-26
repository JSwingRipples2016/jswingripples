package org.incha.ui.stats;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.classview.ClassTreeView;
import org.incha.ui.texteditor.TextEditor;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class HierarchicalView extends ClassTreeView {
    private static final long serialVersionUID = -725916023414871313L;

    /**
     * Default constructor.
     */
    public HierarchicalView(final JavaProject project, final List<JSwingRipplesEIGNode> nodes) {
        super(project);
        setData(nodes);
    	
        addMouseListener(new MouseAdapter() {

            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(e.getX(), e.getY());
                } else if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    JSwingRipplesEIGNode selectedItem = getSelectedItem(e.getX(), e.getY());
                    IMember selectedNodeIMember = selectedItem.getNodeIMember();
                    ISourceRange selectedNodeSourceRange;
                    try {
                        selectedNodeSourceRange = selectedNodeIMember.getSourceRange();
                    } catch (JavaModelException e1) {
                        throw new RuntimeException(e1);
                    }
                    final ICompilationUnit unit = selectedNodeIMember.getCompilationUnit();
                	String fileToOpen = unit.getPath().toString();
                	try {
						TextEditor textEditor = TextEditor.getInstance();
						textEditor.bringToFront();
                        textEditor.openFile(fileToOpen, selectedNodeSourceRange);
                    } catch (Exception e1) {
						e1.printStackTrace();
					}
                }
                
            }
            
        });
    }

    /**
     * @param x x mouse coordinate.
     * @param y y mouse coordinate.
     */
    protected void showPopupMenu(final int x, final int y) {
        final JSwingRipplesEIGNode node = getSelectedItem(x, y);
        if (node != null) {
            ICActionsManager.getInstance().showMenuForNode(node, x, y, this);
        }
    }
}
