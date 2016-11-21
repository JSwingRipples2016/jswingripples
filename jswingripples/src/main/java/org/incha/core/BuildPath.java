package org.incha.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.incha.utils.CollectionUtils;

/**
 * Stores the Sources of the project, with ArrayList for the folders
 *
 */
public class BuildPath {

    /**
     * The source files property name.
     */
    public static final String SOURCES = "sources";

    /**
     * The list of java source folders.
     */
    private final List<File> sources = new ArrayList<File>();
    /**
     * Property change support.
     */
    private final PropertyChangeSupport pcs;
    /**
     * Owner project.
     */
    private final JavaProject project;

    /**
     * @param project owner java project.
     */
    public BuildPath(final JavaProject project) {
        super();
        this.project = project;
        pcs = new PropertyChangeSupport(project);
    }

    /**
     * @return the sourceFolders
     */
    public List<File> getSources() {
        return new ArrayList<File>(sources);
    }
    /**
     * @param file the source folder.
     */
    public void addSource(final File file) {
        final File equals = CollectionUtils.getEquals(sources, file);
        if (equals == null) {
            final List<File> old = new ArrayList<File>(sources);
            sources.add(file);
            firePropertyChange(SOURCES, old, sources);
        }
    }
    /**
     * @param file the source folder.
     */
    public void deleteSource(final File file) {
        final File equals = CollectionUtils.getEquals(sources, file);
        if (equals != null) {
            final List<File> old = new ArrayList<File>(sources);
            sources.remove(file);
            firePropertyChange(SOURCES, old, sources);
        }
    }

    /**
     * @param property property name.
     * @param oldValue old property value.
     * @param newValue new property value.
     */
    protected void firePropertyChange(
            final String property, final List<File> oldValue, final List<File> newValue) {
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
     * @return the project
     */
    public JavaProject getProject() {
        return project;
    }
    
    //added: return the first path related to the sources
    public String getFirstPath(){
        if (!sources.isEmpty()){
            return sources.get(0).getPath();
        }
        return "";
    } 
}
