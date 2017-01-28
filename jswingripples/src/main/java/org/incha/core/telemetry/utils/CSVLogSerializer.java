package org.incha.core.telemetry.utils;

import java.io.*;
import java.util.List;

/**
 * Created by eaburto on 26-01-17.
 */
public class CSVLogSerializer {
    private BufferedWriter out;

    public CSVLogSerializer(File file) throws IOException {
        out = new BufferedWriter(new FileWriter(file));
    }

    public void write(List<CSVQuad> quads) throws IOException {
        for(CSVQuad quad: quads){
            out.write(quad.toString());
            out.newLine();
        }
        out.flush();
        out.close();
    }

    public static class CSVQuad {
        private final String timestamp;
        private final Phase phase;
        private final String action;
        private final String entity;
        static final String SEPARATOR = ";";


        public CSVQuad(String timestamp, Phase phase, String action, String entity){
            this.timestamp = timestamp;
            this.phase = phase;
            this.action = action;
            this.entity = entity;
        }

        CSVQuad(String fileLine){
            this(fileLine.split(SEPARATOR));
        }

        private CSVQuad(String[] array){
            this(array[0], Phase.parse(array[1]), array[2], array[3]);
        }

        @Override
        public String toString(){
            /* Since Java 8 this can be more beautiful */
//            return String.join(SEPARATOR, toList());

            StringBuilder builder = new StringBuilder();
            builder.append(timestamp);
            builder.append(SEPARATOR);
            builder.append(phase);
            builder.append(SEPARATOR);
            builder.append(action);
            builder.append(SEPARATOR);
            builder.append(entity);

            return builder.toString();
        }


        public String[] toArray(){
            return new String[]{
                    timestamp,
                    phase.toString(),
                    action,
                    entity};
        }
    }
}
