package org.incha.ui;

import org.incha.core.telemetry.CSVTelemetryLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by eaburto on 28-01-17.
 */
public class LogTableView extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private static String[] COLUMN_NAMES = new String[]{
            "Timestamp",
            "Phase",
            "Action",
            "Entity"
    };

    public LogTableView(String[][] data){
        super(new GridLayout(1,0));
        model = new DefaultTableModel(data, COLUMN_NAMES);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        CSVTelemetryLogger.registerTable(this);
    }

    public DefaultTableModel getModel(){
        return model;
    }
}
