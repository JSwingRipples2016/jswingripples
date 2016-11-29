package org.incha.ui.stats;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.eclipse.jdt.core.IMember;
import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.Statistics;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class StatsDialog extends JFrame {
    private static final long serialVersionUID = 4328259959960759078L;
    private static final String EMPTY_PROJECT = "         ";
    private final JComboBox<String> projectNames = new JComboBox<String>();

    private final JLabel mainClass = new JLabel();
    private final JLabel numberOfNodes = new JLabel();
    private final JLabel numberOfTopNodes = new JLabel();
    private final JLabel numberOfMembers = new JLabel();
    private final JLabel numberOfSubclasses = new JLabel();
    private final JLabel numberOfMethods = new JLabel();
    private final JLabel numberOfVariables = new JLabel();
    private final JLabel numberOfEdges = new JLabel();
    private final JLabel numberOfCustomEdges = new JLabel();

    /**
     * @param project java project.
     */
    public StatsDialog(final JavaProject[] projects) {
        super();
        initComponents(projects);
    }
    /**
     * @param projects
     */
    protected void initComponents(final JavaProject[] projects) {
        setTitle("JRipples stats");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //set content pane.
        final JPanel contentPane = new JPanel(new BorderLayout(0, 5));
        setContentPane(contentPane);

        //create projects combobox
        this.projectNames.setEditable(false);
        final String[] projectNames = new String[projects.length + 1];
        projectNames[0] = EMPTY_PROJECT;

        for (int i = 0; i < projects.length; i++) {
            projectNames[i + 1] = projects[i].getName();
        }
        final DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<String>(projectNames);
        this.projectNames.setModel(cbModel);
        this.projectNames.setSelectedIndex(0);

        final JPanel title = new JPanel(new BorderLayout(5, 0));
        title.setBorder(new EmptyBorder(2, 2, 2, 2));
        title.add(new JLabel("Project:"), BorderLayout.WEST);
        title.add(this.projectNames, BorderLayout.CENTER);

        contentPane.add(title, BorderLayout.NORTH);

        //create statistics view:
        final JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEADING));
        centerWrapper.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        contentPane.add(centerWrapper, BorderLayout.CENTER);

        final JPanel center = new JPanel(new GridLayout(9, 2, 5, 5));
        center.setBorder(new EmptyBorder(3, 3, 3, 3));
        centerWrapper.add(center);

//        Main class:
        center.add(new JLabel("Main class:"));
        center.add(mainClass);

//            All nodes: 289
        center.add(new JLabel("All nodes:"));
        center.add(numberOfNodes);

//            Top nodes (classes): 31
        center.add(new JLabel("Top nodes (classes):"));
        center.add(numberOfTopNodes);

//            Member nodes: 267
        center.add(new JLabel("Member nodes:"));
        center.add(numberOfMembers);

//               Subclasses: 10
        center.add(new JLabel("  Subclasses:"));
        center.add(numberOfSubclasses);


//               Methods: 177
        center.add(new JLabel("  Methods:"));
        center.add(numberOfMethods);

//               Variables: 80
        center.add(new JLabel("  Variables:"));
        center.add(numberOfVariables);

//            Edges: 438
        center.add(new JLabel("Edges:"));
        center.add(numberOfEdges);

//               Custom edges:
        center.add(new JLabel("  Custom edges:"));
        center.add(numberOfCustomEdges);

        //add listeners
        this.projectNames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                projectSelected();
            }
        });
    }

    /**
     *
     */
    protected void projectSelected() {
        JSwingRipplesEIG eig = null;
        final String projectName = (String) projectNames.getSelectedItem();
        if (!EMPTY_PROJECT.equals(projectName)) {
            final JavaProject project = JavaProjectsModel.getInstance().getProject(projectName);
            final Statistics stats = project.getCurrentStatistics();
            eig = stats == null ? null : stats.getEIG();
        }

        if (eig == null) {
            mainClass.setText("");
            numberOfNodes.setText("");
            numberOfTopNodes.setText("");
            numberOfMembers.setText("");
            numberOfSubclasses.setText("");
            numberOfMethods.setText("");
            numberOfVariables.setText("");
            numberOfEdges.setText("");
            numberOfCustomEdges.setText("");
        } else {
            if (eig.getMainClass() != null) {
                mainClass.setText(eig.getMainClass());
            }
            final JSwingRipplesEIGNode[] nodes= eig.getAllNodes();
            int methods=0;
            int classes=0;
            int variables=0;

            for (int i=0;i<nodes.length;i++) {
                if (nodes[i].isTop()) continue;
                switch (nodes[i].getNodeIMember().getElementType()) {
                    case IMember.FIELD:
                        variables++;
                        break;
                    case IMember.METHOD:
                        methods++;
                        break;
                    case IMember.TYPE:
                        classes++;
                        break;
                }
            }

            final JSwingRipplesEIGEdge edges[]= eig.getAllEdges();
            int edge=0;
            for (int i=0;i<edges.length;i++) {
                if (edges[i].getMark()!=null)
                    if (edges[i].getMark().compareTo("Custom")==0)
                        edge++;
            }

            numberOfNodes.setText("" + nodes.length);
            numberOfTopNodes.setText("" + eig.getTopNodes().length);

            numberOfSubclasses.setText("" + classes);
            numberOfMethods.setText("" + methods);
            numberOfVariables.setText("" + variables);

            numberOfEdges.setText("" + edges.length);
            numberOfCustomEdges.setText("" + edge);

        }
    }
}
