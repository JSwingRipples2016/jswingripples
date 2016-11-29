package org.incha.core.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.incha.ui.classview.ClassTreeView;
import org.incha.ui.search.SearchMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Created by fcocl_000 on 05-05-2016.
 * Class that manages user searches using the indexes in the index Directory.
 */
public class Searcher {
    /**
     * Class instance for singleton pattern.
     */
    private static Searcher instance = null;
    /**
     * Lucene class which handles user input and creates a Lucene query.
     */
    private QueryParser queryParser;
    /**
     * Lucene class for searching the created indexes.
     */
    private IndexSearcher indexSearcher;
    /**
     * Contains all distinct search hits and the number of occurrences of the search
     * term in each search hit.
     */
    private Map<String, Integer> results = new HashMap<>();

    private List<String> resultsList = new ArrayList<>();

    /**
     * Class view UI.
     */
    private ClassTreeView classTreeView;
    
    /**
     * Contains information about the part of the file where 
     * the word was found. 
     */
    private List<Object []> res_information = new ArrayList<Object []>();
    /**
     * Maximum number of appearances of the search term in a single file.
     */
    private int maxFrequency;
    /**
     * Minimum number of appearances of the search term in a single file.
     */
    private int minFrequency;

    /**
     * Returns the current instance.
     * @return the current Indexer instance.
     */
    public static Searcher getInstance() {
        if (instance == null) {
            try {
                instance = new Searcher();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Default constructor.
     * @throws IOException
     */
    private Searcher() throws IOException {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        queryParser = new QueryParser(Version.LUCENE_36, LuceneConstants.CONTENTS, analyzer);
    }

    /**
     * Closes the IndexSearcher.
     * @throws IOException
     */
    public void close() throws IOException {
        indexSearcher.close();
    }

    /**
     * Set current class view.
     * @param classTreeView the current class view.
     */
    public void setClassTreeView(ClassTreeView classTreeView) {
        this.classTreeView = classTreeView;
    }

    /**
     * Searches the indexes in the Directory.
     * @param searchQuery the user's query.
     * @return the query results.
     * @throws IOException
     * @throws ParseException
     */
    private TopDocs searchIndexes(String searchQuery) throws IOException, ParseException {
        Query query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, LuceneConstants.MAX_RESULTS_ITEMS);
    }

    /**
     * Retrieves Lucene Document from ScoreDoc.
     * @param scoreDoc the document to be retrieved.
     * @return the corresponding Document.
     * @throws IOException
     */
    private Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    /**
     * Handles complete search process.
     * @param searchQuery the user's query string.
     * @throws IOException
     * @throws ParseException
     */
    public void search(String searchQuery) throws IOException, ParseException {
        // The directory containing the indexes.
        Directory indexDirectory = FSDirectory.open(new File(LuceneConstants.INDEX_DIRECTORY_PATH));
        indexSearcher = new IndexSearcher(IndexReader.open(indexDirectory));
        TopDocs topDocs = searchIndexes(searchQuery);
        results = new HashMap<>();
        for(ScoreDoc doc : topDocs.scoreDocs) {
            String aux = removeJavaExtension(getDocument(doc).get(LuceneConstants.FILE_NAME));
            if (results.containsKey(aux)) {
                // If contained, update appearance frequency.
                results.put(aux, results.get(aux) + 1);
            } else {
                // Entry added if filename not contained.
                results.put(aux, 1);
            }
            resultsList.add(aux);
            int index_query = resultsList.indexOf(aux);
            readFileAndLook(getDocument(doc).get(LuceneConstants.FILE_PATH), searchQuery, index_query);
        }

        // Update files with most/least search term occurrences
        if(results.size() > 0){
            refreshMaxMin();
        }
    }

    // Refresh analysis table
    public void repaintClassTreeView(){
        classTreeView.repaint();
    }

    /**
     * Updates max and min frequency fields upon a new search. Used to avoid Collections operations
     * for every hitPercentage() call.
     */
    private void refreshMaxMin() {
        maxFrequency = Collections.max(results.values());
        minFrequency = Collections.min(results.values());
    }

    /**
     * Calculates the proportion of times the last searched term appears in the specified file
     * to the difference of the maximum and minimum number of search hits in any file for the same term.
     * @param fileName the file's name.
     * @return the percentage of hits.
     */
    double hitPercentage(String fileName) {
        if (!results.containsKey(fileName)) return -1;
        int frequency = results.get(fileName);
        if (frequency >  maxFrequency || frequency < minFrequency) return -1; // file process not finished yet
        else return (results.get(fileName) - minFrequency)  * 1.0 / (maxFrequency - minFrequency);
    }

    /**
     * Returns the total number of term appearance in the file.
     * @param fileName the file's name.
     * @return the total number of hits.
     */
    public int totalHits (String fileName) {
        return !results.containsKey(fileName) ? 0 : results.get(fileName);
    }

    /**
     * Closes index searcher and clears the result HashMap.
     */
    public void clearResults() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        results = new HashMap<>();

        // Refresh analysis table
        classTreeView.repaint();
    }

    /**
     * Removes the .java extension from the given filename.
     * @param fileName the filename to modify.
     * @return the filename without the .java extension.
     */
    private String removeJavaExtension(String fileName) {
        return fileName.replace(".java", "");
    }
    
    /**
     * Returns the results of the query
     * @return
     */
    public Map<String, Integer> getResults() {
    	return results;
    }
    
    /**
     * Return a list of the lines where the words were found
     */
    public List<Object []> getResInfo() {
    	return res_information;
    }
    
    /**
     * Reads the file where the word was found to provide information
     * @throws FileNotFoundException 
     * 
     */
    public void readFileAndLook(String path, String searchQuery, int index_query) throws FileNotFoundException {
        RandomAccessFile file = new RandomAccessFile(path.replace('\\', '/'), "r");    	
    	String line; 
    	int num_line = 0;    	
    	try{    	      	   
           while ((line = file.readLine()) != null) {   
        	  ++num_line; 
              if (line.contains(" " + searchQuery.trim() + " ")) {
            	  Object [] new_info = {line, index_query, num_line};
            	  res_information.add(new_info);
            	  break; //Sólo se toma la primera línea
              }
            }   
         }catch(Exception e){
            e.printStackTrace();            
         }
    	try {
    		if (file != null) file.close();     		
    	}
        catch(Exception e){
            e.printStackTrace();            
        }
    }
    
}
