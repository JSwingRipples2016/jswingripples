package org.incha.ui.stats;

import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.ModuleConfiguration;
import org.incha.core.Statistics;
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

    JComboBox<String> dependencyGraph = new JComboBox<String>(new DefaultComboBoxModel<String>(
        new String[]{
            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER,
            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC
        }
    ));

    /**
     * Default constructor.
     */
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
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startConceptLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    doOk();
                } catch (StartAnalysisAction.AnalysisFailedException ex) {
                    // TODO: how to notify the user?
                    ex.printStackTrace();
                }
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

    /**
     *
     */
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
    /**
     * @return
     */
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
        addClassNameTextListener();
        dependencyGraph.setEditable(false);
        projects.setEditable(false);
        return panel;
    }

    protected void doCancel() {
        dispose();
    }

    protected void doOk() throws StartAnalysisAction.AnalysisFailedException {
        dispose();
        startAnalysisCallback.startAnalysis(this);
    }

    public File getMainClass() {
        return mainClassFile;
    }
    
    protected void setClassName(final String classNameParam, String fileName){
        mainClassFile = new File(fileName);
        classNameTextField.setText(classNameParam);
    }
    
    protected void enableOkButton(){
        startConceptLocationButton.setEnabled(true);
    }

    private JPanel createMainClassFinderPanel() {
        JPanel classNameFinderPanel = new JPanel();
        classNameFinderPanel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        classNameFinderPanel.add(classNameTextField);
        classNameFinderPanel.add(createBrowseButton());
        classNameFinderPanel.add(createAutomaticButton());
        return classNameFinderPanel;
    }

    private JButton createBrowseButton() {
        JButton browseButton = new JButton("Browse");
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

    private JButton createAutomaticButton() {
        JButton automaticButton = new JButton("Auto");
        automaticButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final MainClassSearchDialog dialog;
                    dialog = new MainClassSearchDialog(StartAnalysisDialog.this, project);
                    dialog.pack();
                    dialog.setLocationRelativeTo(ownerWindow);
                    dialog.setTitle("Select the enter point");
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(StartAnalysisDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return automaticButton;
    }

    private void addClassNameTextListener() {
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
}
