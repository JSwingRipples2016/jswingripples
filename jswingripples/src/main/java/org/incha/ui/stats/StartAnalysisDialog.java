package org.incha.ui.stats;

import org.incha.core.*;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.parser.InteractiveTask;
import org.incha.core.telemetry.utils.Phase;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.ui.jripples.JRipplesDefaultModulesConstants;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StartAnalysisDialog extends JDialog {
    private static final long serialVersionUID = 6788138046337076311L;
    private final JTextField classNameTextField = new JTextField(30);
    private final JButton startConceptLocationButton = new JButton("Start Concept Location");
    private final StartAnalysisAction startAnalysisCallback;
    private File mainClassFile;
    private JavaProject project;
    final Window ownerWindow;
    final JComboBox<String> projects;
    private static Phase currentPhase;

    public static Phase getCurrentPhase() {
        return currentPhase;
    }

    JComboBox<String> dependencyGraph = new JComboBox<String>(new DefaultComboBoxModel<String>(
        new String[]{
            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER,
            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC
        }
    ));
    
    public StartAnalysisDialog(final Window owner, final StartAnalysisAction callback) {
        super(owner);
        ownerWindow = owner;
        startAnalysisCallback = callback;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 5));

        //create projects combobox.
        final List<JavaProject> prg = JavaProjectsModel.getInstance().getProjects();
        final String[] prgArray = new String[prg.size()];
        for (int i = 0; i < prgArray.length; i++) {
            prgArray[i] = prg.get(i).getName();
        }
        projects = new JComboBox<>(new DefaultComboBoxModel<>(prgArray));
        projects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                projectChanged();
            }
        });

        final JPanel center = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final JPanel projectAndType = createCenterPanel();
        center.add(projectAndType);

        getContentPane().add(center, BorderLayout.CENTER);

        //south pane
        startConceptLocationButton.setEnabled(false);
        classNameTextField.setText("");
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startConceptLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doOk();
            }
        });
        south.add(startConceptLocationButton);

        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doCancel();
            }
        });
        south.add(cancel);
        getContentPane().add(south, BorderLayout.SOUTH);

        // select proper project
        if (startAnalysisCallback.getProjectSelected() != null){        	
        	projects.setSelectedItem(startAnalysisCallback.getProjectSelected());        	
        }
        //set up default values
        projectChanged();
    }

    protected void projectChanged() {
        project = JavaProjectsModel.getInstance().getProject((String) projects.getSelectedItem());
        
        if (project != null) {
            //set current module configuration

            //dependency graph module
            final ModuleConfiguration cfg = project.getModuleConfiguration();
            final Statistics stats = project.getCurrentStatistics();
            classNameTextField.setText(stats != null ? stats.getEIG().getMainClass() : null);

            switch (cfg.getDependencyGraphModule()) {
                case ModuleConfiguration.MODULE_DEPENDENCY_BUILDER:
                    dependencyGraph.setSelectedItem(JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER);
                break;
                default://MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC
                    dependencyGraph.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC);
            }
        }
    }
    
    private void verifyMainClassFileExtension(){ 
        final Integer sizeExtension = 5;
        String classname = classNameTextField.getText();
            if (classname.length()>sizeExtension && 
                    classname.substring(classname.length()-sizeExtension, 
                            classname.length()).toUpperCase().equals(".JAVA")){
                startConceptLocationButton.setEnabled(true);
            }
            else {
                startConceptLocationButton.setEnabled(false);
            }
    }

    private JPanel createCenterPanel() {
        final JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED),
                new EmptyBorder(3, 3, 3, 3)));
        panel.add(new JLabel("Java project:"));
        panel.add(projects);
        panel.add(new JLabel("Class name:"));
        panel.add(createMainClassFinderPanel());
        panel.add(new JLabel("Dependency Graph"));
        panel.add(dependencyGraph);
        dependencyGraph.setEditable(false);
        projects.setEditable(false);
        addClassNameTextFieldListener();
        return panel;
    }

    protected void doCancel() {
        dispose();
    }

    protected void doOk() {
        if(JSwingRipplesApplication.getInstance().isAnotherProjectOpen()){
            String[] options = new String[]{"Yes","Cancel"};
            int result = JOptionPane.showOptionDialog(this,
                    new String[]{"There is another analysis in progress.\n " +
                            "Are you sure you want to begin a start a new one? " +
                            "All progress from the last analysis will be lost."},
                    "Another analysis in progress",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]);
            if(result == JOptionPane.YES_OPTION){
                beginConceptLocation();
            }
        } else {
            beginConceptLocation();
        }

    }

    private void beginConceptLocation(){
        dispose();
        currentPhase = Phase.CL; // Starting Concept Location phase.
        JSwingRipplesApplication.getInstance().enableProceedButton(true);
        JSwingRipplesApplication.getInstance().setProceedButtonText("Proceed to Impact Analysis");
        JSwingRipplesApplication.getLogger().log(currentPhase, "Loading classes...", "N/A");
        startAnalysisCallback.startAnalysis(
            createConceptLocationData(), new StartAnalysisAction.SuccessfulAnalysisAction() {
                @Override
                public void execute(ModuleConfiguration config, final JSwingRipplesEIG eig) {
                    JSwingRipplesApplication.getInstance().showProceedButton();
                    StatisticsManager.getInstance().addStatistics(config, eig);
                    JSwingRipplesApplication.getLogger().log(currentPhase, "All classes loaded", "N/A");
                    JSwingRipplesApplication.getInstance().setProceedButtonListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            currentPhase = Phase.IA;
                            JSwingRipplesApplication.getInstance().enableProceedButton(false);
                            JSwingRipplesApplication.getLogger().log(currentPhase, "Starting analysis...", "N/A");
                            startAnalysisCallback.startAnalysis(createImpactAnalysisData(eig), createImpactAnalysisCallback());
                        }
                });
             }
        });
    }
    
    protected void setClassName(final String classNameParam, String fileName){
        mainClassFile = new File(fileName);
        classNameTextField.setText(classNameParam);
    }
    
    protected void enableButtonOk(){
        startConceptLocationButton.setEnabled(true);
    }

    private void addClassNameTextFieldListener() {
        classNameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                verifyMainClassFileExtension();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                verifyMainClassFileExtension();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verifyMainClassFileExtension();
            }
        });
    }

    private JPanel createMainClassFinderPanel() {
        JPanel mainClassFinderPanel = new JPanel();
        mainClassFinderPanel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        mainClassFinderPanel.add(classNameTextField);
        mainClassFinderPanel.add(createBrowseButton());
        mainClassFinderPanel.add(createAutomaticButton());

        return mainClassFinderPanel;
    }

    private JButton createAutomaticButton() {
        final JButton automaticButton = new JButton("Auto");
        automaticButton.setEnabled(projects.getItemCount() != 0);
        automaticButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new InteractiveTask(new InteractiveTask.TaskListener() {
                    @Override
                    public void taskSuccessful() {
                        automaticButton.setEnabled(true);
                    }

                    @Override
                    public void taskFailure() {
                        Logger.getLogger(StartAnalysisDialog.class.getName())
                              .log(Level.SEVERE, "Could not find main classes");
                    }
                }) {
                    @Override
                    public void run() {
                        try {
                            automaticButton.setEnabled(false);
                            // dialog creation is a heavy operation, since it looks for main classes
                            // in the entire project
                            MainClassSearchDialog dialog = new MainClassSearchDialog(StartAnalysisDialog.this, project);
                            dialog.pack();
                            dialog.setLocationRelativeTo(ownerWindow);
                            dialog.setTitle("Select entry point");
                            dialog.setVisible(true);
                            listener.taskSuccessful();

                        } catch (IOException ex) {
                            listener.taskFailure();
                        }
                    }
                }.start();
            }
        });
        return automaticButton;
    }

    private JButton createBrowseButton() {
        JButton browseButton = new JButton("Browse");
        browseButton.setEnabled(projects.getItemCount() != 0);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser chooser = new JFileChooser(project.getBuildPath().getFirstPath());
                chooser.setMultiSelectionEnabled(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final File selectedFile = chooser.getSelectedFile();
                    if (selectedFile != null) {
                        mainClassFile = selectedFile;
                        classNameTextField.setText(selectedFile.getName());
                    }
                }
            }
        });
        return browseButton;
    }
    private AnalysisData createConceptLocationData() {
        return new AnalysisData(
                (String) projects.getSelectedItem(),
                mainClassFile,
                (String) dependencyGraph.getSelectedItem(),
                ModuleConfiguration.AnalysisModule.MODULE_CONCEPT_LOCATION,
                new JSwingRipplesEIG(JavaProjectsModel.getInstance().getProject((String) projects.getSelectedItem())));
    }

    private AnalysisData createImpactAnalysisData(JSwingRipplesEIG postConceptLocationEIG) {
        return new AnalysisData(
                (String) projects.getSelectedItem(),
                mainClassFile,
                (String) dependencyGraph.getSelectedItem(),
                ModuleConfiguration.AnalysisModule.MODULE_IMPACT_ANALYSIS,
                postConceptLocationEIG);
    }

    private AnalysisData createChangePropagationData(JSwingRipplesEIG postImpactAnalysisEIG) {
        return new AnalysisData(
                (String) projects.getSelectedItem(),
                mainClassFile,
                (String) dependencyGraph.getSelectedItem(),
                ModuleConfiguration.AnalysisModule.MODULE_CHANGE_PROPAGATION,
                postImpactAnalysisEIG);
    }

    private StartAnalysisAction.SuccessfulAnalysisAction createChangePropagationCallback(){
        return new StartAnalysisAction.SuccessfulAnalysisAction() {
            @Override
            public void execute(ModuleConfiguration config, JSwingRipplesEIG eig) {
                JSwingRipplesApplication.getInstance().enableProceedButton(true);
                JSwingRipplesApplication.getLogger().log(currentPhase, "Loaded", "N/A");
                JSwingRipplesApplication.getInstance().hideProceedButton();
                JSwingRipplesApplication.getInstance().resetProceedButton();
                JSwingRipplesApplication.getInstance().refreshViewArea();
            }
        };
    }

    private StartAnalysisAction.SuccessfulAnalysisAction createImpactAnalysisCallback(){
        return new StartAnalysisAction.SuccessfulAnalysisAction() {
            @Override
            public void execute(ModuleConfiguration config,final JSwingRipplesEIG eig) {
                JSwingRipplesApplication.getInstance().enableProceedButton(true);
                JSwingRipplesApplication.getLogger().log(currentPhase, "Analysis successful", "N/A");
                JSwingRipplesApplication.getInstance().refreshViewArea();
                JSwingRipplesApplication.getInstance().setProceedButtonText("Proceed To Change Propagation");
                JSwingRipplesApplication.getInstance().setProceedButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JSwingRipplesApplication.getInstance().enableProceedButton(false);
                        currentPhase = Phase.CP;
                        JSwingRipplesApplication.getLogger().log(currentPhase, "Loading propagation...", "N/A");
                        startAnalysisCallback.startAnalysis(
                                createChangePropagationData(eig), createChangePropagationCallback());
                    }
                });
            }
        };
    }
}
