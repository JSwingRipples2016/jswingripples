package org.incha.ui;

import org.apache.commons.logging.LogFactory;
import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.StatisticsManager;
import org.incha.core.search.Searcher;
import org.incha.ui.search.SearchMenu;
import org.incha.ui.stats.GraphVisualizationAction;
import org.incha.ui.stats.ImpactGraphVisualizationAction;
import org.incha.ui.stats.ShowCurrentStateAction;
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
    private int tabbedAreaCount = 0;
    private SearchMenu searchMenu;
    private final ProjectsView projectsView;
    private static JSwingRipplesApplication instance;
    private TaskProgressMonitor progressMonitor;

    private JSwingRipplesApplication(final JTabbedPane viewArea, TaskProgressMonitor progressMonitor) {
        super("JSwingRipples");
        this.viewArea = viewArea;
        this.progressMonitor = progressMonitor;
        addJTabbedPaneMouseListener(viewArea);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JPanel contentPane = new JPanel(new BorderLayout(0, 5));
        setContentPane(contentPane);
        contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        setJMenuBar(createMenuBar());

        projectsView = new ProjectsView(JavaProjectsModel.getInstance());
        projectsView.addProjectsViewMouseListener(new ProjectsViewMouseListener() {
            @Override
            public void handle(final ProjectsViewMouseEvent e) {
                handleProjectsViewMouseEvent(e);
            }
        });
        
        final JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectsView, viewArea);
        getContentPane().add(pane, BorderLayout.CENTER);

        //add progress monitor.
        getContentPane().add(progressMonitor, BorderLayout.SOUTH);

        //init liteners
        final DefaultController controller = new DefaultController();
        StatisticsManager.getInstance().addStatisticsChangeListener(controller);

        //create model saver, this class will watch for model
        //and save it when model state changed
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
                    showProjectSettinsEditor(project);
                }
            });
            menu.add(prefs);

            //start analysis
            final JMenuItem startAnalysis = new JMenuItem("Start analysis");
            startAnalysis.addActionListener(new StartAnalysisAction(project.getName()));
            menu.add(startAnalysis);

            menu.show(projectsView, e.getX(), e.getY());
        }
    }
    
    /**
     * @param project
     */
    protected void showProjectSettinsEditor(final JavaProject project) {
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
     * Method to create Aplication Menu
     * 
     * @return 
     */
    private JMenuBar createMenuBar() {
        final JMenuBar bar = new JMenuBar();
        
        /* Start: File Menu */
        final JMenu file = new JMenu("File");
        bar.add(file);
       
        //File Menu ---> Submenu: Import Project
        final JMenuItem importProject = new JMenuItem("Import Project");
        importProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                importProject();
            }
        });
        file.add(importProject);
        /* End: File Menu */
        
        /* Start: JRipples Menu */
        final JMenu jRipples = new JMenu("JRipples");
        bar.add(jRipples);
        
        //JRipples Menu ---> Submenu: Start analysis
        final JMenuItem startAnalysis = new JMenuItem("Start analysis");
        startAnalysis.addActionListener(new StartAnalysisAction());
        jRipples.add(startAnalysis);
        
        jRipples.add(new JSeparator(JSeparator.HORIZONTAL));
        
        //JRipples Menu ---> Submenu: Current state - statistics        
        final JMenuItem currentState = new JMenuItem("Current state - statistics");
        currentState.addActionListener(new ShowCurrentStateAction());
        jRipples.add(currentState);
        
        //JRipples Menu ---> Submenu: Current Graph
        final JMenuItem currentGraph = new JMenuItem("Current Graph");        
        currentGraph.addActionListener(new GraphVisualizationAction());
        jRipples.add(currentGraph);
        
        //JRipples Menu ---> Submenu: Impact Set Graph
        final JMenuItem impactGraph = new JMenuItem("Impact Set Graph");
        impactGraph.addActionListener(new ImpactGraphVisualizationAction());
        jRipples.add(impactGraph);
        /* End: JRipples Menu */
        
        /* Start: Graph Style Menu */
        final JMenu Graph_style = new JMenu("Graph Style");
        bar.add(Graph_style);
        
        //Graph Style Menu Menu ---> Submenu: Toggle - Node size by rank
        final JMenuItem nodesize = new JMenuItem("Toggle - Node size by rank");
        nodesize.addActionListener(new NodeSizeChangeAction());
        Graph_style.add(nodesize);
        nodesize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        
        //Graph Style Menu Menu ---> Submenu: Bigger Nodes
        final JMenuItem bigger_nodes = new JMenuItem("Bigger Nodes");
        bigger_nodes.addActionListener(new NodeChangeAction(0));
        Graph_style.add(bigger_nodes);
        bigger_nodes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        
        //Graph Style Menu Menu ---> Submenu: Smaller Nodes
        final JMenuItem smaller_nodes = new JMenuItem("Smaller Nodes");
        smaller_nodes.addActionListener(new NodeChangeAction(1));
        Graph_style.add(smaller_nodes);
        smaller_nodes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        
        //Graph Style Menu Menu ---> Submenu: Toggle - Zoomed text
        final JMenuItem zoomed_text = new JMenuItem("Toggle - Zoomed text");
        zoomed_text.addActionListener(new Zoomed_text_changer());
        Graph_style.add(zoomed_text);
        zoomed_text.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
        
        //Graph Style Menu Menu ---> Submenu: Bigger text
        final JMenuItem bigger_text = new JMenuItem("Bigger text");
        bigger_text.addActionListener(new Text_size_changer(0));
        Graph_style.add(bigger_text);
        bigger_text.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
        
        //Graph Style Menu Menu ---> Submenu: Smaller text
        final JMenuItem smaller_text = new JMenuItem("Smaller text");
        smaller_text.addActionListener(new Text_size_changer(1));
        Graph_style.add(smaller_text);
        smaller_text.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK));
        
        //Graph Style Menu Menu ---> Submenu: Colors
        JMenu submenu1 = new JMenu("Colors");
        
        //Graph Style Menu Menu ---> Submenu: Colors ---> Colors list
        JMenuItem c0 = new JMenuItem("White");
        c0.addActionListener(new NodeColorChangeAction("white"));
        JMenuItem c1 = new JMenuItem("SandyBrown");
        c1.addActionListener(new NodeColorChangeAction("sandybrown"));
        JMenuItem c2 = new JMenuItem("Sienna");
        c2.addActionListener(new NodeColorChangeAction("sienna"));
        JMenuItem c3 = new JMenuItem("Salmon");
        c3.addActionListener(new NodeColorChangeAction("salmon"));
        JMenuItem c4 = new JMenuItem("MediumOrchid");
        c4.addActionListener(new NodeColorChangeAction("mediumorchid"));
        JMenuItem c5 = new JMenuItem("NavyBlue");
        c5.addActionListener(new NodeColorChangeAction("navyblue"));
        JMenuItem c6 = new JMenuItem("SkyBlue");
        c6.addActionListener(new NodeColorChangeAction("skyblue"));
        JMenuItem c7 = new JMenuItem("Aquamarine");
        c7.addActionListener(new NodeColorChangeAction("aquamarine"));
        JMenuItem c8 = new JMenuItem("LimeGreen");
        c8.addActionListener(new NodeColorChangeAction("limegreen"));
        JMenuItem c9 = new JMenuItem("Black");
        c9.addActionListener(new NodeColorChangeAction("black"));
        
        Graph_style.add(submenu1);
        submenu1.add(c0);
        submenu1.add(c1);
        submenu1.add(c2);
        submenu1.add(c3);
        submenu1.add(c4);
        submenu1.add(c5);
        submenu1.add(c6);
        submenu1.add(c7);
        submenu1.add(c8);
        submenu1.add(c9);
        
        //Graph Style Menu Menu ---> Submenu: Themes
        JMenu submenu2 = new JMenu("Themes");
        
        //Graph Style Menu Menu ---> Submenu: Colors ---> Themes list
        JMenuItem t0 = new JMenuItem("Cappuccino");
        t0.addActionListener(new Theme_changer(0));
        
        JMenuItem t1 = new JMenuItem("Stars");
        t1.addActionListener(new Theme_changer(1));
       
        JMenuItem t2 = new JMenuItem("Blob");
        t2.addActionListener(new Theme_changer(2));
        
        JMenuItem t3 = new JMenuItem("Perimeter");
        t3.addActionListener(new Theme_changer(3));
        
        JMenuItem t4 = new JMenuItem("Glow");
        t4.addActionListener(new Theme_changer(4));
        
        JMenuItem t5 = new JMenuItem("Bond");
        t5.addActionListener(new Theme_changer(5));
        
        Graph_style.add(submenu2);
        submenu2.add(t0);
        submenu2.add(t1);
        submenu2.add(t2);
        submenu2.add(t3);
        submenu2.add(t4);
        submenu2.add(t5);
        /* End: Graph Style Menu */
//        final JMenuItem manageStates = new JMenuItem("Manage Statess");
//        jRipples.add(manageStates);
//        final JMenuItem saveState = new JMenuItem("Save State");
//        jRipples.add(saveState);
//        final JMenuItem loadState = new JMenuItem("Load State");
//        jRipples.add(loadState);
        
        
        /* Start: Help Menu */
        final JMenu Help = new JMenu("Help");
        bar.add(Help);
        
        //Help Menu ---> Submenu: Help
        final JMenuItem subHelp = new JMenuItem("Help");        
        Help.add(subHelp);
        
        Help.add(new JSeparator(JSeparator.HORIZONTAL));
        
        //Help Menu ---> Submenu: Help
        final JMenuItem about = new JMenuItem("About");        
        Help.add(about);
        /* End: Help Menu */
        
        /* Start: Search Menu */
        searchMenu = new SearchMenu();
        Searcher.getInstance().setSearchMenu(searchMenu);
        bar.add(searchMenu.getSearchPanel());
        /* End: Search Menu */
        
        return bar;
    }

    /**
     * Creates new project.
     */
    protected void createNewProject() {
        final JavaProject project = NewProjectWizard.showDialog(this);
        if (project != null) {
            JavaProjectsModel.getInstance().addProject(project);
        }
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

                // If called with the protocol args
                processArgs(args);
                f.setVisible(true);
            }
        });
    }
    
    private static void processArgs(final String[] args) {
    	if (args.length != 0) {
    		JavaProjectsModel javaProjectsModel = JavaProjectsModel.getInstance();
    		JavaProject project = javaProjectsModel.getProjectByName(args[0]);
    		if (project == null) {
    			project = new JavaProject(args[0]);
    			javaProjectsModel.addProject(project);
    		}
    		for (int argNumber = 1; argNumber < args.length; argNumber++) {
    			project.getBuildPath().addSource(new File(args[argNumber]));
    		}
    	}
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
        this.tabbedAreaCount++;
        viewArea.addTab(tabTitle, component);
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
                            tabbedAreaCount--;
                            if(tabbedAreaCount == 0){
                                JMenuBar mb = getJMenuBar();
                                searchMenu.getClearButton().setEnabled(false);
                                searchMenu.getSearchButton().setEnabled(false);
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

}