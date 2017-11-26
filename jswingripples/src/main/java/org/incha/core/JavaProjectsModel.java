package org.incha.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.ui.JSwingRipplesApplication;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores information about the Java Projects and methods for their listeners.
 */
public class JavaProjectsModel {
    private static final Log log = LogFactory.getLog(JavaProjectsModel.class);

    private static final JavaProjectsModel instance = loadModel();
    /**
     * Project property name.
     */
    public static final String PROJECTS = "projects";
    /**
     * Property change support.
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /**
     * List of projects.
     */
    private final List<JavaProject> projects = new LinkedList<>();

    /**
     * Default constructor.
     */
    protected JavaProjectsModel() {
        super();
    }

    private boolean projectWithSameNameExists(String projectName) {
        return getProjectByName(projectName) != null;
    }

    private void changeProjectNameToNonExistent(JavaProject project) {
        String projectName = project.getName();
        while (projectWithSameNameExists(projectName)) {
            projectName += "_new";
        }
        project.setProjectName(projectName);
    }

    /**
     * @param project new project.
     */
    public void addProject(final JavaProject project) {
        if (projectWithSameNameExists(project.getName())) {
            changeProjectNameToNonExistent(project);
        }

        final List<JavaProject> old = new ArrayList<>(projects);
        projects.add(project);
        firePropertyChange(PROJECTS, old, projects);
    }
    /**
     * @param project project to delete.
     */
    public void deleteProject(final JavaProject project) {
        final JavaProject equals = getProjectByName(project.getName());
        if (equals != null) {
            final List<JavaProject> old = new ArrayList<>(projects);
            projects.remove(project);
            firePropertyChange(PROJECTS, old, projects);
        }
    }
    /**
     * @param name project name.
     * @return project by given name.
     */
    public JavaProject getProjectByName(final String name) {
        for (final JavaProject p : projects) {
            if (name.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }
    /**
     * @param property property name.
     * @param oldValue old property value.
     * @param newValue new property value.
     */
    protected void firePropertyChange(
            final String property, final List<JavaProject> oldValue, final List<JavaProject> newValue) {
        pcs.firePropertyChange(property, oldValue, newValue);
    }
    /**
     * @param l property change listener.
     */
    public void addPropertyChangeListener(final PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    /**
     * @param l property change listener.
     */
    public void removePropertyChangeListener(final PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    /**
     * @return java projects list.
     */
    public List<JavaProject> getProjects() {
        return projects;
    }
    /**
     * @param selectedItem
     */
    public JavaProject getProject(final String selectedItem) {
        for (final JavaProject prg : projects) {
            if (prg.getName().equals(selectedItem)) {
                return prg;
            }
        }
        return null;
    }

    public void renameProject(String oldName, String newName) {
        JavaProject project = getProjectByName(oldName);
        if (project == null) {
            throw new AssertionError("The project " + oldName + " should exist, otherwise it can't have been renamed.");
        }
        deleteProject(project);
        project.setProjectName(newName);
        addProject(project);
    }
    /**
     * @return
     */
    public static JavaProjectsModel getInstance() {
        return instance;
    }

    /**
     * Load model from file.
     * @return model.
     */
    private static JavaProjectsModel loadModel() {
        JavaProjectsModel model = new JavaProjectsModel();

        final File file = getModelFile();
        if (file.exists()) {
            try {
                final Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                try {
                    model = new ModelSerializer().parse(r);
                } finally {
                    r.close();
                }
            } catch (final Exception e) {
                log.error("Failed to parse application file", e);
            }

        }
        return model;
    }
    /**
     * @return
     */
    public static File getModelFile() {
        return new File(JSwingRipplesApplication.getHome(), "application.xml");
    }
}
