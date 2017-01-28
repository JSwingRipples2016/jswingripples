package org.incha.core.telemetry.utils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eaburto on 26-01-17.
 */
public class CSVLogDeserializer {
    private BufferedReader in;

    public CSVLogDeserializer(File file) throws FileNotFoundException {
        in = new BufferedReader(new FileReader(file));
    }

    public List<CSVLogSerializer.CSVQuad> read() throws IOException {
        List<CSVLogSerializer.CSVQuad> list = new LinkedList<>();

        String line;
        while((line = in.readLine()) != null){
            list.add(new CSVLogSerializer.CSVQuad(line));
        }

        return list;
    }
}
