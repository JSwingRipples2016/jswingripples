package org.incha.ui.search;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchMenu {

	private JPanel searchPanel;
	
	/**
     * Default constructor. Se crea un panel de búsqueda para colocarlo en algún menú.
     */
    private JButton searchButton;
    private JButton clearButton;

    public JButton getSearchButton(){return searchButton;}
    public JButton getClearButton() {return clearButton;}

	public SearchMenu() {
        searchPanel = new JPanel();
        final JTextField searchedWords = new JTextField(15);
        searchPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchedWords);
        searchButton = new JButton("Go");
        clearButton = new JButton("Clear");
        searchButton.setEnabled(false);
        clearButton.setEnabled(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartSearchWorker(searchedWords).execute();
            }
        });
        clearButton.addActionListener(new ClearSearchAction(searchedWords));
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
	}
	
	public JPanel getSearchPanel() {
		return searchPanel;
	}
}
