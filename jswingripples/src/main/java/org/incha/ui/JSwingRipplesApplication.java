
package org.incha.ui;

import org.apache.commons.logging.LogFactory;
import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.StatisticsManager;
import org.incha.ui.stats.StartAnalysisAction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JSwingRipplesApplication extends JFrame {
    private static final long serialVersionUID = 6142679404175274529L;
    private JTabbedPane viewArea;
    private JButton proceedButton;
    private JSplitPane projectViewAndViewAreaSplit;
    private final ProjectsView projectsView;
    private final MainMenuBar mainMenuBar;
    private static JSwingRipplesApplication instance;
    private TaskProgressMonitor progressMonitor;

    private JSwingRipplesApplication(final JTabbedPane viewArea, TaskProgressMonitor progressMonitor) {
        super("JSwingRipples");
        this.viewArea = viewArea;
        this.progressMonitor = progressMonitor;
        setContentPane(createMainContentPane());
        mainMenuBar = new MainMenuBar();
        projectsView = createProjectsView();
        projectViewAndViewAreaSplit = createProjectViewAndViewAreaSplit();
        getContentPane().add(projectViewAndViewAreaSplit, BorderLayout.CENTER);
        getContentPane().add(progressMonitor, BorderLayout.SOUTH);
        setJMenuBar(mainMenuBar.getJBar());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addJTabbedPaneMouseListener(viewArea);
        StatisticsManager.getInstance().addStatisticsChangeListener(new DefaultController());
        new ModelSaver(JavaProjectsModel.getInstance(), JavaProjectsModel.getModelFile());
    }

    /**
     * @param e
     */
    protected void handleProjectsViewMouseEvent(final ProjectsViewMouseEvent e) {
        if (e.getType() != ProjectsViewMouseEvent.LEFT_MOUSE_PRESSED) {
            return;
        }

        final Object[] path = e.getPath();
        if (path[path.length -1] instanceof JavaProject) {
            final JavaProject project = (JavaProject) path[path.length -1];
            final JPopupMenu menu = new JPopupMenu();
            
            //delete project menu item
            final JMenuItem delete = new JMenuItem("Delete Project");
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    JavaProjectsModel.getInstance().deleteProject(project);
                }
            });
            menu.add(delete);

            //project preferences menu item
            final JMenuItem prefs = new JMenuItem("Settings");
            prefs.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    showProjectSettingsEditor(project);
                }
            });
            menu.add(prefs);

            //start analysis
            final JMenuItem startAnalysis = new JMenuItem("Start analysis");
            startAnalysis.addActionListener(new StartAnalysisAction(project.getName()));
            menu.add(startAnalysis);
            
            //project preferences menu item
            final JMenuItem showIssues = new JMenuItem("Show Issues");
            showIssues.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    IssuesView issuesWindow = new IssuesView();
                    IssuesReader issuesReader = new IssuesReader(JSwingRipplesApplication.getHome()+ File.separator+project.getName()+".xml");
                    issuesReader.load();
                    issuesWindow.addTableView(issuesReader.loadData(), issuesReader.loadColumnNames());
                    addComponentAsTab(issuesWindow,"Issues from Project: "+ project.getName());
                }
            });
            menu.add(showIssues);
            menu.show(projectsView, e.getX(), e.getY());
        }
    }
    
    /**
     * @param project
     */
    protected void showProjectSettingsEditor(final JavaProject project) {
        final JFrame f = new JFrame("Project Settings");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout(0, 5));

        final ProjectSettingsEditor view = new ProjectSettingsEditor(project);
        f.getContentPane().add(view, BorderLayout.CENTER);

        //add ok button
        final JPanel south = new JPanel(new FlowLayout());
        final JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                f.dispose();
            }
        });
        south.add(ok);
        f.getContentPane().add(south, BorderLayout.SOUTH);

        //set frame location
        final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        f.setSize(size.width / 2, size.height / 2);
        f.setLocationRelativeTo(this);

        //show frame
        f.setVisible(true);
    }

    /**
     * Import a project from a path.
     */
    protected void importProject(){
        final JavaProject project = NewProjectWizard.showDialog(this);
        if (project != null) {
            if(JavaProjectsModel.getInstance().addProject(project)) {
                new ImportSource(project);
            }
        }
    }

    /**
     * Import project from a url in github
     */
    protected void importProjectGithub(){
        final JavaProject project = NewProjectWizard.showDialog(this);
        if (project != null) {
            if(JavaProjectsModel.getInstance().addProject(project)) {
                new GitSettings(project);
            }
        }
    }

    /**
     * @return the application home folder.
     */
    public static File getHome() {
        return new File(System.getProperty("user.home") + File.separator + ".jswingripples");
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
                //init logging
                getHome().mkdirs();
                //Properties
                Properties prop = new Properties();
                try
                {
                    InputStream in = JSwingRipplesApplication.class.getClassLoader().getResourceAsStream("project.properties");
                    prop.load(in);
                } catch (IOException e) {
                    LogFactory.getLog(JSwingRipplesApplication.class).error("Missing properties file!");
                    System.exit(1);
                }
                final JFrame f = JSwingRipplesApplication.getInstance();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //set frame location
                final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
                f.setSize(size.width / 2, size.height / 2);
                f.setLocationByPlatform(true);
                //LogFactory.getLog(JSwingRipplesApplication.class).debug("Prueba uno");
                String info = prop.getProperty("project_name") + " version " + prop.getProperty("project_version");
                LogFactory.getLog(JSwingRipplesApplication.class).info(info);

                f.setVisible(true);
            }
        });
    }

    /**
     * Get function using singleton.
     * @return shared application window.
     */
    public static JSwingRipplesApplication getInstance() {
    	if(instance==null){
    		instance = new JSwingRipplesApplication(new JTabbedPane(), new ProgressMonitorImpl());
    	}
        return instance;
    }

    /**
     * @return progress monitor.
     */
    public TaskProgressMonitor getProgressMonitor() {
        return this.progressMonitor;
    }

    public void addComponentAsTab(JComponent component, String tabTitle) {
        viewArea.addTab(tabTitle, component);
    }

    public void removeAllTabs(){
        for(int i = 0 ; i < viewArea.getTabCount(); i++){
            viewArea.removeTabAt(i);
        }
    }

    public void enableSearchMenuButtons() {
        mainMenuBar.getSearchMenu().getSearchButton().setEnabled(true);
        mainMenuBar.getSearchMenu().getClearButton().setEnabled(true);
    }


    public void showProceedButton() {
        JSplitPane rightSide = (JSplitPane) projectViewAndViewAreaSplit.getRightComponent();
        rightSide.setRightComponent(proceedButton); // make the button show up by setting it as the bottom part
        rightSide.revalidate();
    }

    public void setProceedButtonListener(ActionListener al){
        resetProceedButton();
        proceedButton.addActionListener(al);
    }

    public void setProceedButtonText(String labelText){
        proceedButton.setText(labelText);
    }

    public void hideProceedButton() {
       JSplitPane rightSide = (JSplitPane) projectViewAndViewAreaSplit.getRightComponent();
       rightSide.setRightComponent(null); // make the button disappear by changing the bottom part reference
       rightSide.revalidate();
    }

    public void resetProceedButton() {
        for(ActionListener al : proceedButton.getActionListeners()){
            proceedButton.removeActionListener(al);
        }
    }

    public void enableProceedButton(boolean enable){
        proceedButton.setEnabled(enable);
    }

    public void refreshViewArea() {
        viewArea.repaint();
    }

    public boolean isAnotherProjectOpen(){
        return viewArea.getTabCount() > 0;
    }

    private void addJTabbedPaneMouseListener(JTabbedPane pane){
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if((SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e))
                        && viewArea.indexAtLocation(e.getX(),e.getY()) != -1){
                    final JPopupMenu menu = new JPopupMenu();
                    final JMenuItem close = new JMenuItem("Close");
                    close.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            viewArea.removeTabAt(viewArea.getSelectedIndex());
                            if(viewArea.getTabCount() == 0){
                                mainMenuBar.getSearchMenu().getClearButton().setEnabled(false);
                                mainMenuBar.getSearchMenu().getSearchButton().setEnabled(false);
                                hideProceedButton();
                                resetProceedButton();
                            }
                        }
                    });
                    menu.add(close);
                    menu.show(viewArea, e.getX(), e.getY());
                }
                super.mouseClicked(e);
            }
        });
    }

    private ProjectsView createProjectsView() {
        ProjectsView projectsView = new ProjectsView(JavaProjectsModel.getInstance());
        projectsView.addProjectsViewMouseListener(new ProjectsViewMouseListener() {
            @Override
            public void handle(final ProjectsViewMouseEvent e) {
                handleProjectsViewMouseEvent(e);
            }
        });
        return projectsView;
    }

    private JSplitPane createProjectViewAndViewAreaSplit() {
        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectsView, createViewAreaAndProceedButtonSplit());
    }

    private JSplitPane createViewAreaAndProceedButtonSplit() {
        proceedButton = new JButton("Proceed to Impact Analysis");
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, viewArea, null);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.95);
        splitPane.setEnabled(false);
        return splitPane;
    }

    private JPanel createMainContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout(0, 5));
        contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        return contentPane;
    }
}