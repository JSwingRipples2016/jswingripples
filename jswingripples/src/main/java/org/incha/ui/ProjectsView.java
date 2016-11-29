package org.incha.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.incha.core.BuildPath;
import org.incha.core.JavaProjectsModel;
import org.incha.core.JavaProject;
import org.incha.utils.CollectionUtils;

public class ProjectsView extends JPanel {
    private static final long serialVersionUID = -7945996533262500375L;

    /**
     * The sources node.
     */
    public static final String SOURCES = "sources";

    /**
     * List of actions for right mouse clicking on project
     */
    private List<ProjectsViewMouseListener> mouseListeners = new LinkedList<ProjectsViewMouseListener>();

    /**
     * The model.
     */
    private final JavaProjectsModel model;
    /**
     * Project tree.
     */
    private final JTree tree = new JTree(new DefaultTreeModel(
            new DefaultMutableTreeNode(null)));

    /**
     * The build path listener.
     */
    private final PropertyChangeListener buildPathListener = new PropertyChangeListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            buildPathChanged((JavaProject) evt.getSource(), evt.getPropertyName(),
                    (List<File>) evt.getOldValue(),
                    (List<File>) evt.getNewValue());
        }
    };

    /**
     * @param model the model.
     */
    public ProjectsView(final JavaProjectsModel model) {
        super(new BorderLayout());
        this.model = model;

        //add listeners to model
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                modelChanged(evt);
            }
        });

        //add projects tree
        ((DefaultMutableTreeNode) tree.getModel().getRoot()).setUserObject(model);
        tree.setRootVisible(false);
        tree.setEditable(false);
        tree.setCellRenderer(new ProjectsTreeCellRenderer());
        add(new JScrollPane(tree), BorderLayout.CENTER);
        tree.addMouseListener(new MouseAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
             */
            @Override
            public void mousePressed(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    e.consume();
                    mousePressedOnTree(e.getX(), e.getY(), ProjectsViewMouseEvent.LEFT_MOUSE_PRESSED);
                }
            }
            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    mousePressedOnTree(e.getX(), e.getY(), ProjectsViewMouseEvent.MOUSE_CLICKED);
                }
            }
        });

        synchronizeModel();
    }

    /**
     * Synchronizes model by view.
     */
    private void synchronizeModel() {
        for (final JavaProject p : model.getProjects()) {
            handleProjectAdded(p);
        }
    }

    /**
     * @param evt property change event.
     */
    @SuppressWarnings("unchecked")
    protected void modelChanged(final PropertyChangeEvent evt) {
        if (JavaProjectsModel.PROJECTS.equals(evt.getPropertyName())) {
            CollectionUtils.synchronize((List<JavaProject>) evt.getOldValue(),
                (List<JavaProject>) evt.getNewValue(),
                new CollectionUtils.SynchHandler<JavaProject>() {
                    @Override
                    public void itemAdded(final JavaProject item) {
                        handleProjectAdded(item);
                    };
                    @Override
                    public void itemDeleted(final JavaProject item) {
                        handleProjectDeleted(item);
                    };
                });
        }
    }

    /**
     * @param project the project.
     * @param propertyName property name.
     * @param oldValue old build path value.
     * @param newValue new build path value
     */
    protected void buildPathChanged(final JavaProject project, final String propertyName,
            final List<File> oldValue, final List<File> newValue) {
        final DefaultMutableTreeNode projectNode = getProjectNode(project);
        final DefaultMutableTreeNode root = getBuildPathNode(projectNode, propertyName);

        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

        final List<File> newFiles = new LinkedList<File>(newValue);
        final List<File> oldFiles = new LinkedList<File>(oldValue);
        //process added files

        //handle added files
        Iterator<File> iter = newFiles.iterator();
        while (iter.hasNext()) {
            final File f = iter.next();
            if (!oldFiles.remove(f)) {
                model.insertNodeInto(new DefaultMutableTreeNode(f),
                        root, model.getChildCount(root));
            }

            iter.remove();
        }

        //handle deleted files
        iter = oldFiles.iterator();
        while(iter.hasNext()) {
            final DefaultMutableTreeNode fileNode = getFileNode(root, iter.next());
            model.removeNodeFromParent(fileNode);
        }
    }
    /**
     * @param project deleted project.
     */
    protected void handleProjectDeleted(final JavaProject project) {
        //remove property change listener from project
        project.getBuildPath().removePropertyChangeListener(buildPathListener);

        //remove project node from view
        final DefaultMutableTreeNode node = getProjectNode(project);
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.removeNodeFromParent(node);
    }
    /**
     * @param project the project.
     * @return the node containing given project.
     */
    private DefaultMutableTreeNode getProjectNode(final JavaProject project) {
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        final int count = model.getChildCount(model.getRoot());

        for (int i = 0; i < count; i++) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) model.getChild(
                    model.getRoot(), i);
            if (project.equals(child.getUserObject())) {
                return child;
            }
        }

        return null;
    }

    /**
     * @param project added project.
     */
    protected void handleProjectAdded(final JavaProject project) {
        //add change listener to project
        project.getBuildPath().addPropertyChangeListener(buildPathListener);

        //create project node.
        final DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project);

        //create class path source node.
        final DefaultMutableTreeNode src = new DefaultMutableTreeNode(SOURCES);
        projectNode.add(src);
        //add source items to source node
        for (final File f : project.getBuildPath().getSources()) {
            src.add(new DefaultMutableTreeNode(f));
        }

        //add project node to view
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.insertNodeInto(projectNode, (DefaultMutableTreeNode) model.getRoot(),
                model.getChildCount(model.getRoot()));

        //expand root if required
        final TreePath path = new TreePath(tree.getModel().getRoot());
        if (!tree.isExpanded(path)) {
            tree.expandPath(path);
        }
    }

    /**
     * @param root the files container node (sources/claspath).
     * @param file
     * @return the node for given file.
     */
    private DefaultMutableTreeNode getFileNode(final DefaultMutableTreeNode root,
            final File file) {
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        final int count = model.getChildCount(root);

        for (int i = 0; i < count; i++) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) model.getChild(
                    root, i);
            if (file.equals(child.getUserObject())) {
                return child;
            }
        }

        return null;
    }

    /**
     * @param projectNode the project node.
     * @param propertyName the property node.
     * @return build path node for given property (class path node or source node)
     */
    private DefaultMutableTreeNode getBuildPathNode(
            final DefaultMutableTreeNode projectNode, final String propertyName) {
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        final int count = model.getChildCount(projectNode);
        final String label = SOURCES;
        for (int i = 0; i < count; i++) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) model.getChild(
                    projectNode, i);
            if (label.equals(child.getUserObject())) {
                return child;
            }
        }
        return null;
    }
    /**
     * @param x x mouse coordinate.
     * @param y y mouse coordinate.
     * @param actionId the action ID.
     */
    protected void mousePressedOnTree(final int x, final int y, final int actionId) {
        final TreePath path = tree.getPathForLocation(x, y);
        if (path != null) {
            //create array of user objects
            final Object[] userObjects = new Object[path.getPathCount()];
            for (int i = 0; i < userObjects.length; i++) {
                userObjects[i] = ((DefaultMutableTreeNode) path.getPathComponent(i)).getUserObject();
            }

            final ProjectsViewMouseEvent e = new ProjectsViewMouseEvent(userObjects, actionId, x, y);
            for (final ProjectsViewMouseListener l : this.mouseListeners) {
                l.handle(e);
            }
        }
    }
    /**
     * @param l action listener.
     */
    public void addProjectsViewMouseListener(final ProjectsViewMouseListener l) {
        mouseListeners.add(l);
    }
    /**
     * @param l action listener.
     */
    public void removeProjectsViewMouseListener(final ProjectsViewMouseListener l) {
        mouseListeners.remove(l);
    }
}
