package org.incha.core;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;

/**
 * Keeps information about eig, config and id.
 */
public class Statistics {
    private final JSwingRipplesEIG eig;
    private final ModuleConfiguration config;
    protected String id;

    /**
     * @param eig EIG.
     * @param config configuration.
     */
    public Statistics(final JSwingRipplesEIG eig, final ModuleConfiguration config) {
        super();
        this.eig = eig;
        this.config = config;
    }
    /**
     * @return the config
     */
    public ModuleConfiguration getConfig() {
        return config;
    }
    /**
     * @return the eig
     */
    public JSwingRipplesEIG getEIG() {
        return eig;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
}
