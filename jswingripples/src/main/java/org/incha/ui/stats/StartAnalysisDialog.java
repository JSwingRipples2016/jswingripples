package org.incha.ui.stats;

import java.io.File; 
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser; 
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.ModuleConfiguration;
import org.incha.core.Statistics;
import org.incha.ui.jripples.JRipplesDefaultModulesConstants;


public class StartAnalysisDialog extends JDialog {
    private static final long serialVersionUID = 6788138046337076311L;
    private File mainClassFile;
    private StartAnalysisAction startAnalysisCallback;
    final JComboBox<String> projects;
    final JTextField className = new JTextField(30);
    final JButton ok = new JButton("Ok"); 
    
    private JavaProject project;
    
    final JComboBox<String> incrementalChange = new JComboBox<String>(new DefaultComboBoxModel<String>(
        new String[]{
            JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_TITLE,
            JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_RELAXED_TITLE,
            JRipplesDefaultModulesConstants.MODULE_CHANGE_PROPAGATION_RELAXED_TITLE,
            JRipplesDefaultModulesConstants.MODULE_CHANGE_PROPAGATION_TITLE,
            JRipplesDefaultModulesConstants.MODULE_CONCEPT_LOCATION_TITLE,
            JRipplesDefaultModulesConstants.MODULE_CONCEPT_LOCATION_RELAXED_TITLE
        }
    ));
    JComboBox<String> analysis = new JComboBox<String>(new DefaultComboBoxModel<String>(
        new String[]{
            JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_TITLE
        }
    ));
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
        projects = new JComboBox<String>(new DefaultComboBoxModel<String>(prgArray));
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
        ok.setEnabled(false);
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ok.addActionListener(new ActionListener() {
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
        south.add(ok);

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
            className.setText(stats != null ? stats.getEIG().getMainClass() : null);

            switch (cfg.getDependencyGraphModule()) {
                case ModuleConfiguration.MODULE_DEPENDENCY_BUILDER:
                    dependencyGraph.setSelectedItem(JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER);
                break;
                default://MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC
                    dependencyGraph.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC);
            }

            //Incremental change
            switch (cfg.getIncrementalChange()) {
                case ModuleConfiguration.MODULE_IMPACT_ANALYSIS:
                    incrementalChange.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_TITLE);
                break;
                case ModuleConfiguration.MODULE_IMPACT_ANALYSIS_RELAXED:
                    incrementalChange.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_RELAXED_TITLE);
                break;
                case ModuleConfiguration.MODULE_CHANGE_PROPAGATION_RELAXED:
                    incrementalChange.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_CHANGE_PROPAGATION_RELAXED_TITLE);
                break;
                case ModuleConfiguration.MODULE_CHANGE_PROPAGATION:
                    incrementalChange.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_CHANGE_PROPAGATION_TITLE);
                break;
                case ModuleConfiguration.MODULE_CONCEPT_LOCATION:
                    incrementalChange.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_CONCEPT_LOCATION_TITLE);
                break;
                case ModuleConfiguration.MODULE_CONCEPT_LOCATION_RELAXED:
                    incrementalChange.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_CONCEPT_LOCATION_RELAXED_TITLE);
                break;
            }

            switch(cfg.getAnalysis()) {
                case ModuleConfiguration.MODULE_IMPACT_ANALYSIS:
                    analysis.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_TITLE);
                    break;
            }
        }
    }
    
    private void verifyMainClassFileExtension(){ 
        final Integer sizeExtension = 5;
        String classname = className.getText();
            if (classname.length()>sizeExtension && 
                    classname.substring(classname.length()-sizeExtension, 
                            classname.length()).toUpperCase().equals(".JAVA")){
                ok.setEnabled(true);
            }
            else {
                ok.setEnabled(false);
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
        projects.setEditable(false);
        panel.add(projects);

        panel.add(new JLabel("Class name:"));
        
        JPanel panelclassname = new JPanel();
        panelclassname.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        panelclassname.add(className);
        
        className.getDocument().addDocumentListener(new DocumentListener() {
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
        
        JButton btnsearch = new JButton("Browse");
        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
               
                final JFileChooser chooser = new JFileChooser(project.getBuildPath().getFirstPath());
                //chooser.addChoosableFileFilter(jpegFilter);
                chooser.setMultiSelectionEnabled(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final File selectedFile = chooser.getSelectedFile();
                    if (selectedFile != null) {
                        mainClassFile = selectedFile;
                        className.setText(selectedFile.getName());
                    }
                }
            }
        });
        panelclassname.add(btnsearch);
        panel.add(panelclassname);
       

        //Incremental change combobox
        panel.add(new JLabel("Incremental Change"));
        incrementalChange.setEditable(false);
        panel.add(incrementalChange);
//
//        panel.add(new JLabel("Presentation"));
//        panel.add(presentation);

        panel.add(new JLabel("Analysis"));
        analysis.setEditable(false);;
        panel.add(analysis);

        panel.add(new JLabel("Dependency Graph"));
        dependencyGraph.setEditable(false);
        panel.add(dependencyGraph);
        return panel;
    }

    /**
     *
     */
    protected void doCancel() {
        dispose();
    }
    /**
     *
     */
    protected void doOk() throws StartAnalysisAction.AnalysisFailedException {
        dispose();
        startAnalysisCallback.startAnalysis(this);
    }

    /**
     * @return the className
     */
    public File getMainClass() {
        return mainClassFile;
    }
}
