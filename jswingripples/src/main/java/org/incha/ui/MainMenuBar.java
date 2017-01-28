package org.incha.ui;

import org.apache.log4j.lf5.viewer.LogTable;
import org.incha.core.telemetry.CSVTelemetryLogger;
import org.incha.ui.search.SearchMenu;
import org.incha.ui.stats.GraphVisualizationAction;
import org.incha.ui.stats.ImpactGraphVisualizationAction;
import org.incha.ui.stats.ShowCurrentStateAction;
import org.incha.ui.stats.StartAnalysisAction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainMenuBar {
    private JMenuBar bar;
    private SearchMenu searchMenu;

    public MainMenuBar() {
        bar = new JMenuBar();
        bar.add(createFileMenu());
        bar.add(createjRipplesMenu());
        bar.add(createTelemetryMenu());
        bar.add(createHelpMenu());
        bar.add(createSearchPanel());
    }

    private JMenu createTelemetryMenu() {
        final JMenu telemetryMenu = new JMenu("Telemetry");
        final JMenuItem startTelemetry, openTelemetryLog, importTelemetry, exportTelemetry;

        // Create and add menu items
        startTelemetry = new JMenuItem("Enable logging");
        telemetryMenu.add(startTelemetry);
        openTelemetryLog = new JMenuItem("View log");
        openTelemetryLog.setEnabled(false); // Disabled until logging is enabled
        telemetryMenu.add(openTelemetryLog);
        telemetryMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        importTelemetry = new JMenuItem("Import from file...");
        telemetryMenu.add(importTelemetry);
        importTelemetry.setEnabled(false); // Disabled until logging is enabled
        exportTelemetry = new JMenuItem("Save to file...");
        exportTelemetry.setEnabled(false); // Disabled until logging is enabled
        telemetryMenu.add(exportTelemetry);

        // Create menu items' listeners
        startTelemetry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set new logger
                JSwingRipplesApplication.setLogger(CSVTelemetryLogger.getInstance());

                // Enable opening telemetry view and allow import/export
                openTelemetryLog.setEnabled(true);
                importTelemetry.setEnabled(true);
                exportTelemetry.setEnabled(true);
                }
        });

        openTelemetryLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Log table");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                LogTableView table = new LogTableView(
                        CSVTelemetryLogger.getInstance().getLogsArray());
                frame.setContentPane(table);
                frame.pack();
                frame.setVisible(true);
            }
        });

        importTelemetry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "CSV files", "csv");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Open log file...");
                int returnVal = chooser.showOpenDialog(JSwingRipplesApplication.getInstance());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        CSVTelemetryLogger.getInstance().
                                importFromFile(chooser.getSelectedFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        exportTelemetry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "CSV files", "csv");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Save log file as...");
                int returnVal = chooser.showOpenDialog(JSwingRipplesApplication.getInstance());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        CSVTelemetryLogger.getInstance().
                                exportToFile(chooser.getSelectedFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        return telemetryMenu;
    }

    public SearchMenu getSearchMenu() {
        return searchMenu;
    }

    public JMenuBar getJBar() {
        return bar;
    }

    private JMenu createFileMenu() {
        final JMenu file = new JMenu("File");
        bar.add(file);

        //File Menu ---> Submenu: Import Project
        final JMenuItem importProject = new JMenuItem("Import Project");
        importProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JSwingRipplesApplication.getInstance().importProject();
            }
        });
        file.add(importProject);
        final JMenuItem importProjectGithub = new JMenuItem("Import Project from GitHub");
        importProjectGithub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JSwingRipplesApplication.getInstance().importProjectGithub();
            }
        });
        file.add(importProjectGithub);
        return file;
    }

    private JMenu createjRipplesMenu() {
        /* Start: JRipples Menu */
        final JMenu menu = new JMenu("JRipples");
        //JRipples Menu ---> Submenu: Start analysis
        final JMenuItem startAnalysis = new JMenuItem("Start analysis");
        startAnalysis.addActionListener(new StartAnalysisAction());
        menu.add(startAnalysis);

        menu.add(new JSeparator(JSeparator.HORIZONTAL));

        //JRipples Menu ---> Submenu: Current state - statistics
        final JMenuItem currentState = new JMenuItem("Current state - statistics");
        currentState.addActionListener(new ShowCurrentStateAction());
        menu.add(currentState);

        //JRipples Menu ---> Submenu: Current Graph
        final JMenuItem currentGraph = new JMenuItem("Current Graph");
        currentGraph.addActionListener(new GraphVisualizationAction());
        menu.add(currentGraph);

        //JRipples Menu ---> Submenu: Impact Set Graph
        final JMenuItem impactGraph = new JMenuItem("Impact Set Graph");
        impactGraph.addActionListener(new ImpactGraphVisualizationAction());
        menu.add(impactGraph);

        return menu;
    }

    private JMenu createHelpMenu() {
        final JMenu Help = new JMenu("Help");
        //Help Menu ---> Submenu: Help
        final JMenuItem subHelp = new JMenuItem("Help");
        Help.add(subHelp);

        Help.add(new JSeparator(JSeparator.HORIZONTAL));

        //Help Menu ---> Submenu: Help
        final JMenuItem about = new JMenuItem("About");
        Help.add(about);
        return Help;
    }

    private JPanel createSearchPanel() {
        searchMenu = new SearchMenu();
        return searchMenu.getSearchPanel();
    }
}
