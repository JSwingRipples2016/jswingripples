package org.incha.core.search;

/**
 * Created by fcocl_000 on 07-05-2016.
 * Class containing constants for the Indexer and Searcher classes.
 */
public class LuceneConstants {
    public static final String CONTENTS = "contents";
    public static final String FILE_NAME = "filename";
    public static final String FILE_PATH = "filepath";
    public static final String INDEX_DIRECTORY_PATH = System.getProperty("user.dir") + "/.SearchIndexes";
    public static final int MAX_RESULTS_ITEMS = 10000;
}
