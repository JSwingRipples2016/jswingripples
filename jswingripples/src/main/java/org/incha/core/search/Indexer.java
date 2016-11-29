package org.incha.core.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by fcocl_000 on 26-04-2016.
 * Indexes data to make it searchable with the Lucene library.
 */
public class Indexer {
    /**
     * Class instance for singleton pattern.
     */
    private static Indexer instance = null;
    /**
     * Filter for manageable files.
     */
    private static final ValidFileFilter validFileFilter = new ValidFileFilter();
    /**
     * Filter for files with the .java extension.
     */
    private static final JavaFileFilter javaFileFilter = new JavaFileFilter();

    /**
     * Returns the current instance.
     * @return the current Indexer instance.
     * @throws IOException
     */
    public static Indexer getInstance() throws IOException {
        if (instance == null) {
            instance = new Indexer();
        }
        return instance;
    }

    /**
     * Creates an index of all files in the project.
     * @param eig the eig of the java project being indexed.
     * @throws IOException
     */
    public void indexEIG(JSwingRipplesEIG eig) throws IOException {
        IndexWriter writer = openWriter();
        for (JSwingRipplesEIGNode node : eig.getAllNodes()) {
            File file = new File(node.getNodeIMember().getCompilationUnit().getPath().toString());
            if (validFileFilter.accept(file)) indexFile(file, writer);
        }
        writer.close();
    }

    /**
     * Creates a new IndexWriter object for creating and maintaining indexes.
     * @return the new IndexWriter.
     * @throws IOException
     */
    private IndexWriter openWriter() throws IOException {
        deleteOldIndex();
        // Directory that will contain indexes
        Directory indexDirectory = FSDirectory.open(new File(LuceneConstants.INDEX_DIRECTORY_PATH));

        // Create the new writer
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(indexDirectory, indexWriterConfig);
    }

    private void deleteOldIndex() {
        File searchIndexesDirectory = new File(LuceneConstants.INDEX_DIRECTORY_PATH);
        if(searchIndexesDirectory.exists() && searchIndexesDirectory.isDirectory()){
            for (File f: searchIndexesDirectory.listFiles()){
                f.delete();
            }
            searchIndexesDirectory.delete();
        }
    }

    /**
     * Creates a Lucene Document object from the specified file.
     * @param file the file to be indexed.
     * @return a new Document instance with file content, name and path fields.
     * @throws FileNotFoundException
     */
    private Document createDocument(File file) throws IOException {
        // Create new document.
        Document document = new Document();

        // Create document fields
        Field content = new Field(LuceneConstants.CONTENTS, new FileReader(file));
        Field name = new Field(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field path = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(),  Field.Store.YES, Field.Index.NOT_ANALYZED);

        // Add fields to document
        document.add(content);
        document.add(name);
        document.add(path);

        return document;
    }

    /**
     * Indexes the file by adding it to the writer.
     * If file is a directory, adds all files in the directory.
     * @param file the file to be indexed.
     * @throws IOException
     */
    private void indexFile(File file, IndexWriter writer) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) for (File subFile : files) indexFile(subFile, writer);
        } else {
            if (!javaFileFilter.accept(file)) return;
            Document document = createDocument(file);
            writer.addDocument(document);
        }
    }
}
