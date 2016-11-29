package org.incha.ui.search;

import org.incha.core.search.Searcher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clears current search results.
 */
class ClearSearchAction implements ActionListener {
    private JTextField searchedWords;
    private Searcher searcher;

    /**
     * Default constructor.
     * @param searchedWords UI text field for entering search terms.
     */
    ClearSearchAction(JTextField searchedWords) {
        this.searchedWords = searchedWords;
        searcher = Searcher.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Remove results from searcher class (clears table view)
        searcher.clearResults();
        // Remove search terms from search bar.
        searchedWords.setText("");
    }
}