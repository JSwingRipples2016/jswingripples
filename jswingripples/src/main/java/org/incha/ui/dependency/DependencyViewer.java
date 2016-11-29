package org.incha.ui.dependency;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.jdt.core.IInitializer;
import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class DependencyViewer extends JFrame {
    private static final String CAN_NOT_ADD_DEPENDENCY_TEXT = "Can not add a dependency to the current tab";
    private static final String ADD_DEPENDENCY_TEXT = "^ Add a dependency to the [Calling - direct] tab ^";

    private static final long serialVersionUID = 113666502784036828L;
    private static final int CALLING_DIRECT = 0;
    private static final int CALLED_DIRECT = 1;

    private final JLabel topLevelInfo = new JLabel();
    private final JButton addDependency = new JButton(ADD_DEPENDENCY_TEXT);
    private final ClassDependencyView callingDirectView;
    private final ClassDependencyView calledDirectView;
    private final ClassDependencyView callingIncludeTransitive;
    private final ClassDependencyView calledIncludeTransitive;
    private final ClassDependencyView allDependencies;

    private final AllClassesView allClassesView;
    private JSwingRipplesEIGNode node;

    /**
     * Default constructor.
     */
    public DependencyViewer(final JavaProject project) {
        super("Dependency manager");
        callingDirectView = new ClassDependencyView(project, 0);
        calledDirectView = new ClassDependencyView(project, 1);
        callingIncludeTransitive = new ClassDependencyView(project, 0);
        calledIncludeTransitive = new ClassDependencyView(project, 1);
        allDependencies = new ClassDependencyView(project, 2);

        allClassesView = new AllClassesView(project);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final JPanel contentPane = new JPanel(new BorderLayout(0, 5));
        setContentPane(contentPane);

        topLevelInfo.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(topLevelInfo, BorderLayout.NORTH);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        contentPane.add(splitPane, BorderLayout.CENTER);

        //top panel
        final JTabbedPane top = new JTabbedPane(JTabbedPane.TOP,
                JTabbedPane.WRAP_TAB_LAYOUT);
        top.add("Calling - direct",
                new JScrollPane(callingDirectView));
        top.add("Called - direct",
                new JScrollPane(calledDirectView));
        top.add("Calling, including transitive through members",
                new JScrollPane(callingIncludeTransitive));
        top.add("Called, including transitive through members",
                new JScrollPane(calledIncludeTransitive));
        top.add("All dependencies",
                new JScrollPane(allDependencies));

        splitPane.setLeftComponent(top);

        //bottom panel
        final JPanel bottom = new JPanel(new BorderLayout(0, 5));
        bottom.add(addDependency, BorderLayout.NORTH);
        bottom.add(new JScrollPane(allClassesView), BorderLayout.CENTER);
        splitPane.setRightComponent(bottom);

        //add listeners
        top.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                final int index = top.getSelectedIndex();
                final boolean allowAddDependency = index == CALLING_DIRECT || index == CALLED_DIRECT;
                addDependency.setEnabled(allowAddDependency);
                addDependency.setText(allowAddDependency
                        ? ADD_DEPENDENCY_TEXT : CAN_NOT_ADD_DEPENDENCY_TEXT);
            }
        });
        addComponentListener(new ComponentAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
             */
            @Override
            public void componentShown(final ComponentEvent e) {
                removeComponentListener(this);
                splitPane.setDividerLocation(0.5);
            }
        });
        addDependency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addDependency(top.getSelectedIndex());
            }
        });
        //remove dependency listener
        final MouseListener dependencyListener = new MouseAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
             */
            @Override
            public void mousePressed(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    removeDependency((ClassDependencyView) e.getSource(),
                            top.getSelectedIndex(), e.getX(), e.getY());
                }
            }
        };
        callingDirectView.addMouseListener(dependencyListener);
        calledDirectView.addMouseListener(dependencyListener);
    }

    /**
     *
     */
    protected void removeDependency(final ClassDependencyView view,
            final int selectedTab, final int x, final int y) {
        final int row = view.getSelectedRow();
        final JSwingRipplesEIGNode s = row < 0 ? null : view.getClassHierarchyModel().getValueAt(row);

        if (s != null) {
            final JSwingRipplesEIGNode node1;
            final JSwingRipplesEIGNode node2;

            if (selectedTab == CALLING_DIRECT) {
                node1 = s;
                node2 = node;
            } else {
                node1 = node;
                node2 = s;
            }

            final JSwingRipplesEIGEdge edge = node.getEig().getEdge(node1, node2);
            if (edge != null) {
                final JPopupMenu popup = new JPopupMenu();
                popup.add(new AbstractAction("Remove the dependency") {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        node.getEig().removeEdge(edge);
                        //refresh all views
                        setNode(node);
                    }
                });

                popup.show(view, x, y);
            }
        }
    }
    /**
     * @param selectedTab
     */
    protected void addDependency(final int selectedTab) {
        final int row = allClassesView.getSelectedRow();
        final JSwingRipplesEIGNode s = row < 0 ? null : allClassesView.getClassHierarchyModel().getValueAt(row);

        if (s != null && !(s.getNodeIMember() instanceof IInitializer)) {
            final JSwingRipplesEIGNode node1;
            final JSwingRipplesEIGNode node2;

            if (selectedTab == CALLING_DIRECT) {
                node1 = s;
                node2 = node;
            } else {
                node1 = node;
                node2 = s;
            }

            final JSwingRipplesEIGEdge edge = node.getEig().addEdge(node1, node2);
            if (edge != null) {
                edge.setMark("Custom");
                //refresh all views
                setNode(node);
            }
        }
    }
    /**
     * @param node the node.
     */
    public void setNode(final JSwingRipplesEIGNode node) {
        this.node = node;
        allClassesView.setData(getAllNodesExcludeGiven(node));
        topLevelInfo.setText("Dependencies of the [" + this.node.getFullName() + "] node: ");
        callingDirectView.setDependencies(node, getCallingDirectDependencies(this.node));
        calledDirectView.setDependencies(node, getCalledDirectDependencies(this.node));
        callingIncludeTransitive.setDependencies(node, getCallingIncludeTransitiveDependencies(this.node));
        calledIncludeTransitive.setDependencies(node, getCalledIncludeTransitiveDependencies(this.node));
        allDependencies.setDependencies(node, getAllDependencies(node));
    }

    /**
     * @param node2
     * @return
     */
    private List<JSwingRipplesEIGNode> getAllNodesExcludeGiven(
            final JSwingRipplesEIGNode node) {
        final List<JSwingRipplesEIGNode> nodes = new LinkedList<JSwingRipplesEIGNode>();
        for (final JSwingRipplesEIGNode n : node.getEig().getAllNodes()) {
            if (!n.equals(node)) {
                nodes.add(n);
            }
        }
        return nodes;
    }

    /**
     * @param node
     * @return
     */
    private Set<JSwingRipplesEIGNode> getAllDependencies(
            final JSwingRipplesEIGNode node) {
        final JSwingRipplesEIG eig = node.getEig();
        final HashSet<JSwingRipplesEIGNode> nodes=new HashSet<JSwingRipplesEIGNode>();
        nodes.add(node);
        return  eig.edgesToNeigbors(
                nodes,
                JSwingRipplesEIG.DIRECTION_CONSIDERED_BOTH_CALLING_AND_CALLED,
                JSwingRipplesEIG.NESTING_CONSIDERED_BOTH_TOP_AND_MEMBER_NODES);
    }
    /**
     * @param node
     * @return
     */
    private Set<JSwingRipplesEIGNode> getCalledIncludeTransitiveDependencies(
            final JSwingRipplesEIGNode node) {
        final JSwingRipplesEIG eig = node.getEig();
        final HashSet<JSwingRipplesEIGNode> nodes=new HashSet<JSwingRipplesEIGNode>();
        nodes.add(node);
        return  eig.edgesToNeigbors(
                nodes,
                JSwingRipplesEIG.DIRECTION_CONSIDERED_CALLED_NODES_ONLY,
                JSwingRipplesEIG.NESTING_CONSIDERED_BOTH_TOP_AND_MEMBER_NODES);
    }
    /**
     * @param node
     * @return
     */
    private Set<JSwingRipplesEIGNode> getCallingIncludeTransitiveDependencies(
            final JSwingRipplesEIGNode node) {
        final JSwingRipplesEIG eig = node.getEig();
        final HashSet<JSwingRipplesEIGNode> nodes=new HashSet<JSwingRipplesEIGNode>();
        nodes.add(node);
        return  eig.edgesToNeigbors(
                nodes,
                JSwingRipplesEIG.DIRECTION_CONSIDERED_CALLING_NODES_ONLY,
                JSwingRipplesEIG.NESTING_CONSIDERED_BOTH_TOP_AND_MEMBER_NODES);
    }
    /**
     * @param node
     * @return
     */
    private Set<JSwingRipplesEIGNode> getCalledDirectDependencies(
            final JSwingRipplesEIGNode node) {
        final JSwingRipplesEIG eig = node.getEig();
        final HashSet<JSwingRipplesEIGNode> nodes=new HashSet<JSwingRipplesEIGNode>();
        nodes.add(node);
        return  eig.edgesToNeigbors(
                nodes,
                JSwingRipplesEIG.DIRECTION_CONSIDERED_CALLED_NODES_ONLY,
                JSwingRipplesEIG.NESTING_CONSIDERED_BOTH_TOP_AND_MEMBER_NODES);
    }
    /**
     * @param node
     * @return
     */
    private Set<JSwingRipplesEIGNode> getCallingDirectDependencies(
            final JSwingRipplesEIGNode node) {
        final JSwingRipplesEIG eig = node.getEig();
        final HashSet<JSwingRipplesEIGNode> nodes=new HashSet<JSwingRipplesEIGNode>();
        nodes.add(node);
        return  eig.edgesToNeigbors(
                nodes,
                JSwingRipplesEIG.DIRECTION_CONSIDERED_CALLING_NODES_ONLY,
                JSwingRipplesEIG.NESTING_CONSIDERED_BOTH_TOP_AND_MEMBER_NODES);
    }
}
