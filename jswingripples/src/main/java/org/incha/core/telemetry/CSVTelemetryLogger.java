package org.incha.core.telemetry;

import org.incha.core.telemetry.utils.CSVLogDeserializer;
import org.incha.core.telemetry.utils.CSVLogSerializer;
import org.incha.core.telemetry.utils.Phase;
import org.incha.ui.LogTableView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.incha.core.telemetry.utils.CSVLogSerializer.*;

/**
 * Created by eaburto on 26-01-17.
 */
public class CSVTelemetryLogger extends AbstractTelemetryLogger{
    private List<CSVQuad> logs;
    private LogTableView table;
    private static CSVTelemetryLogger instance;

    private CSVTelemetryLogger(){
        logs = new LinkedList<>();
        table = null;
    }

    @Override
    public void log(Phase phase, String action, String entity) {
        CSVQuad quad = new CSVQuad(getTimestamp(), phase, action, entity);
        logs.add(quad);
        if (table != null){
            table.getModel().addRow(quad.toArray());
        }
    }

    public void importFromFile(File file) throws IOException {
        logs = new CSVLogDeserializer(file).read();
    }
    public void exportToFile(File file) throws IOException {
        new CSVLogSerializer(file).write(this.logs);
    }

    public String[][] getLogsArray(){
        String[][] array = new String[logs.size()][4];
        for (int i=0; i < logs.size(); i++){
            array[i] = logs.get(i).toArray();
        }
        return array;
    }

    public static CSVTelemetryLogger getInstance(){
        if (instance == null){
            instance = new CSVTelemetryLogger();
        }
        return instance;
    }

    public static void registerTable(LogTableView table){
        getInstance().table = table;
    }
}
