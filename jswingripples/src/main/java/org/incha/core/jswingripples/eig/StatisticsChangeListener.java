package org.incha.core.jswingripples.eig;

import org.incha.core.Statistics;

public interface StatisticsChangeListener {
    /**
     * @param id statistics ID.
     * @param stats the statistics.
     */
    void statisticsAdded(String id, Statistics stats);
    /**
     * @param id statistics ID.
     * @param stats the statistics.
     */
    void statisticsRemoved(String id, Statistics stats);
    /**
     * @param id statistics ID.
     * @param oldStats old statistics.
     * @param newStats new statistics.
     */
    void statisticsReload(String id, Statistics oldStats, Statistics newStats);
}
