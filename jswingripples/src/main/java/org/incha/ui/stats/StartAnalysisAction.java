package org.incha.ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.FSDirectory;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaModel;
import org.incha.compiler.dom.JavaDomBuilder;
import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.ModuleConfiguration;
import org.incha.core.StatisticsManager;
import org.incha.core.jswingripples.GraphBuilder;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.NodeSearchBuilder;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.search.Indexer;
import org.incha.core.search.LuceneConstants;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.ui.jripples.JRipplesDefaultModulesConstants;

public class StartAnalysisAction implements ActionListener {
    public class AnalysisFailedException extends Exception {
        public AnalysisFailedException(String message) { super(message); }
    }
	private String projectSelected;
	
    /**
     * Default constructor.
     */
	
    public StartAnalysisAction() {
        super();
        setProjectSelected(null);
    }
    
    /**
     * Constructor made to launch start analysis dialog with project withProject selected
     * @param withProject
     */
    public StartAnalysisAction(String withProject){
    	super();
    	setProjectSelected(withProject);    	
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JSwingRipplesApplication app = JSwingRipplesApplication.getInstance();
        final StartAnalysisDialog dialog = new StartAnalysisDialog(app, this);
        dialog.pack();
        dialog.setLocationRelativeTo(app);
        dialog.setVisible(true);
    }

    public void startAnalysis(StartAnalysisDialog dialog) throws AnalysisFailedException {
        final String projectName = (String) dialog.projects.getSelectedItem();
        if (projectName == null) {
            return;
        }
        final JavaProject project = JavaProjectsModel.getInstance().getProject(projectName);
        final JSwingRipplesEIG eig = new JSwingRipplesEIG(project);

        String packageName;
        try {
            if ((packageName = getPackage(dialog.getMainClass())) != null) {
                eig.setMainClass(packageName + "." + dialog.getMainClass().getName().replace(".java", ""));
            }
        } catch (JavaModelException e) {
            throw new AnalysisFailedException("Could not retrieve main class package");
        }
        final ModuleConfiguration config = new ModuleConfiguration();
        //module dependency builder
        String module = (String) dialog.dependencyGraph.getSelectedItem();
        if (JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER.equals(module)) {
            config.setDependencyGraphModule(ModuleConfiguration.MODULE_DEPENDENCY_BUILDER);
        } else {
            config.setDependencyGraphModule(ModuleConfiguration.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC);
        }

        //Incremental change
        module = (String) dialog.incrementalChange.getSelectedItem();
        if (JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_TITLE.equals(module)) {
            config.setIncrementalChange(ModuleConfiguration.MODULE_IMPACT_ANALYSIS);
        } else if (JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_RELAXED_TITLE.equals(module)) {
            config.setIncrementalChange(ModuleConfiguration.MODULE_IMPACT_ANALYSIS_RELAXED);
        } else if (JRipplesDefaultModulesConstants.MODULE_CHANGE_PROPAGATION_RELAXED_TITLE.equals(module)) {
            config.setIncrementalChange(ModuleConfiguration.MODULE_CHANGE_PROPAGATION_RELAXED);
        } else if (JRipplesDefaultModulesConstants.MODULE_CHANGE_PROPAGATION_TITLE.equals(module)) {
            config.setIncrementalChange(ModuleConfiguration.MODULE_CHANGE_PROPAGATION);
        } else if (JRipplesDefaultModulesConstants.MODULE_CONCEPT_LOCATION_TITLE.equals(module)) {
            config.setIncrementalChange(ModuleConfiguration.MODULE_CONCEPT_LOCATION);
        } else if (JRipplesDefaultModulesConstants.MODULE_CONCEPT_LOCATION_RELAXED_TITLE.equals(module)) {
            config.setIncrementalChange(ModuleConfiguration.MODULE_CONCEPT_LOCATION_RELAXED);
        }

        module = (String) dialog.analysis.getSelectedItem();
        if (JRipplesDefaultModulesConstants.MODULE_IMPACT_ANALYSIS_TITLE.equals(module)) {
            config.setAnalysis(ModuleConfiguration.MODULE_IMPACT_ANALYSIS);
        }

        project.setModuleConfiguration(config);

        new JRipplesModuleRunner(new JRipplesModuleRunner.ModuleRunnerListener() {
            @Override
            public void runSuccessful() {
                try {
                    Indexer.getInstance().indexEIG(eig);
                    JSwingRipplesApplication.getInstance().enableSearchMenuButtons();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                StatisticsManager.getInstance().addStatistics(config, eig);
            }

            @Override
            public void runFailure() {

            }
        }).runModulesWithPriority(config.buildModules(eig));
    }
    
    /**
     * projectSelected getter
     * @return projectSelected
     */
    protected String getProjectSelected() {
		return projectSelected;
	}

    /**
     * projectSelected setter
     * @param projectSelected
     */
	protected void setProjectSelected(String projectSelected) {
		this.projectSelected = projectSelected;
	}

    private String getPackage(File file) throws JavaModelException {
        JavaDomBuilder builder = new JavaDomBuilder(file.getAbsolutePath());
        for (IPackageDeclaration declaration : builder.build(file).getPackageDeclarations()) {
            if (declaration.getElementName() != null) {
                return declaration.getElementName();
            }
        }
        return null;
    }
}
