package org.incha.ui;

import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.ui.jripples.JRipplesResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProjectSettingsEditor extends JTabbedPane {
    
    private static final long serialVersionUID = 6521933918476824719L;
    
    /**
     * The java project.
     */
    private final JavaProject project;

    /**
     * Default constructor.
     */
    public ProjectSettingsEditor(final JavaProject project) {
        super(LEFT);
        setTabLayoutPolicy(WRAP_TAB_LAYOUT);
        this.project = project;
        addTab("Project", createProjectSettingsTab());
        addTab("Build Path", createBuildPathTab());
        addTab("Github", createGithubTab());
    }

    /**
     * @return project settings tab, like as 'name', 'location', etc.
     */
    private JPanel createProjectSettingsTab() {
        final JPanel root = new JPanel(new FlowLayout(FlowLayout.LEADING));
        FlowLayout layout = new FlowLayout();
        final JPanel panel = new JPanel(layout);
        panel.add(new JLabel("Name: "));
        panel.add(new JLabel(project.getName()));
        ImageIcon icon = new ImageIcon(JRipplesResources.getImage("icons/edit.gif"));
        JLabel editLabel = new JLabel(icon);
        panel.add(editLabel);
        root.add(panel);

        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newProjectName = JOptionPane.showInputDialog(
                        "New project name", project.getName());
                if (newProjectName != null) {
                    JavaProjectsModel.getInstance()
                            .renameProject(project.getName(), newProjectName);
                    panel.remove(1);
                    panel.add(new JLabel(project.getName()), 1);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });
        return root;
    }
    
    /**
     * @return
     */
    private JTabbedPane createGithubTab() {
        final JTabbedPane tabPane = new JTabbedPane(TOP, WRAP_TAB_LAYOUT);
        tabPane.addTab("GitHub Settings", new GitHubSettings(project));
        return tabPane;
    }

    /**
     * @return
     */
    private JTabbedPane createBuildPathTab() {
        final JTabbedPane tabPane = new JTabbedPane(TOP, WRAP_TAB_LAYOUT);
        tabPane.addTab("Sources", new SourcesEditor(project));
        return tabPane;
    }
}