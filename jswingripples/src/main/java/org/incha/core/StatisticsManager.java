/**
 *
 */
package org.incha.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.StatisticsChangeListener;

/**
 * Manages listeners, a Map with Stadistics and actions over it.
 */
public class StatisticsManager {
    /**
     * ID prefix is the launch time of application and number of instance in scope
     * of current application session.
     */
    private static final String ID_PREFIX = System.currentTimeMillis() + "-";
    private final AtomicInteger localId = new AtomicInteger();

    private final int ADDED = 0;
    private final int REMOVED = 1;
    private final int RELOAD = 2;

    protected static StatisticsManager MODEL = new StatisticsManager();

    private final List<StatisticsChangeListener> listeners = new LinkedList<StatisticsChangeListener>();
    private final Map<String, Statistics> eigMap = new HashMap<String, Statistics>();

    /**
     * Default constructor.
     */
    protected StatisticsManager() {
        super();
    }

    public static StatisticsManager getInstance() {
        return MODEL;
    }

    /**
     * @param l the EIG model change listener.
     */
    public void addStatisticsChangeListener(final StatisticsChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    /**
     * @param l the EIG model change listener.
     */
    public void removeStatisticsChangeListener(final StatisticsChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    /**
     * @param projectName project name.
     * @param eig EIG for given project.
     * @return statistics ID.
     */
    public String addStatistics(final ModuleConfiguration config, final JSwingRipplesEIG eig) {
        final Statistics stats = new Statistics(eig, config);
        stats.id = generateId();

        notifyModelListeners(stats.id, null, stats, ADDED);
        return stats.id;
    }
    /**
     * @param id statistics ID..
     * @return EIG for given project.
     */
    public Statistics getStatistics(final String id) {
        synchronized (eigMap ) {
            return eigMap.get(id);
        }
    }
    /**
     * @param id project name.
     */
    public void removeStatitics(final String id) {
        Statistics stats;
        synchronized (eigMap) {
            stats = eigMap.remove(id);
        }

        if (stats != null) {
            notifyModelListeners(id, null, stats, REMOVED);
        }
    }

    /**
     * @param oldStats only for changed event.
     * @param newStats the ADDED added/removed/changed
     * @param type
     */
    protected void notifyModelListeners(
            final String projectName,
            final Statistics oldStats,
            final Statistics newStats,
            final int type) {
        List<StatisticsChangeListener> list;
        synchronized (listeners) {
            list = new LinkedList<StatisticsChangeListener>(listeners);
        }

        for (final StatisticsChangeListener l : list) {
            switch(type) {
                case ADDED:
                    l.statisticsAdded(projectName, newStats);
                    break;
                case REMOVED:
                    l.statisticsRemoved(projectName, newStats);
                    break;
                case RELOAD:
                    l.statisticsReload(projectName, oldStats, newStats);
                    break;
                    default:
                        //ignore
            }
        }
    }

    /**
     * @return generates unique statistics ID.
     */
    private String generateId() {
        return ID_PREFIX + localId.incrementAndGet();
    }
}
