package org.incha.core.search;

import java.awt.*;

/**
 * Manages search results highlight colors.
 * Created by fcocl_000 on 09-05-2016.
 */
public class Highlight {
    /**
     * Return a highlight color based on the percentage of search hits.
     * @param fileName the file to be highlighted.
     * @return the highlight color.
     */
    public static Color getColor(String fileName) {
        double hitPercentage = Searcher.getInstance().hitPercentage(fileName);
        if (hitPercentage == -1) return Color.WHITE;
        return new Color((int) (255 * hitPercentage), 0, (int) (255 * (1 - hitPercentage)), 150 );
    }
}
