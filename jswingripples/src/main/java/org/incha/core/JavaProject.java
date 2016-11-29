package org.incha.core;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the information about the JavProject being worked on, like name, Objects, Statistics, etc.
 */
public class JavaProject {
    /**
     * Project name.
     */
    private final String name;
    /**
     * The list of class path.
     */
    private final BuildPath buildPath = new BuildPath(this);
    /**
     * Map of user objects
     */
    private final Map<String, Object> userObjects = new ConcurrentHashMap<String, Object>();
    /**
     * Current statistics for given project.
     */
    private Statistics currentStatistics;
    private ModuleConfiguration moduleConfiguration = new ModuleConfiguration();

    /**
     * @param name the project name.
     */
    public JavaProject(final String name) {
        super();
        this.name = name;
    }

    /**
     * @return the class path.
     */
    public BuildPath getBuildPath() {
        return buildPath;
    }

    /**
     * @return the project's source Files.
     */
    public List<File> getSources() { return buildPath.getSources(); }
    /**
     * @return the project name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param key key.
     * @return user object.
     */
    public Object getUserObject(final String key) {
        return userObjects.get(key);
    }
    /**
     * @param key the key.
     * @param value the value.
     */
    public void putUserObject(final String key, final Object value) {
        userObjects.put(key, value);
    }

    /**
     * Current statistics for given project.
     * @param stats statistics.
     */
    public void setCurrentStatistics(final Statistics stats) {
        this.currentStatistics = stats;
    }
    /**
     * @return the currentStatistics
     */
    public Statistics getCurrentStatistics() {
        return currentStatistics;
    }

    /**
     * @return the moduleConfiguration.
     */
    public ModuleConfiguration getModuleConfiguration() {
        return moduleConfiguration;
    }
    /**
     * @param moduleConfiguration the moduleConfiguration to set.
     */
    public void setModuleConfiguration(final ModuleConfiguration moduleConfiguration) {
        this.moduleConfiguration = moduleConfiguration;
    }
}
