package org.incha.core.telemetry.utils;

/**
 * Created by eaburto on 25-01-17.
 */
public class Phase {
    private final String text;

    public static final Phase CL;
    public static final Phase IA;
    public static final Phase CP;
    private static final Phase[] phaseArray;

    static {
        CL = new Phase("Concept Location");
        IA = new Phase("Impact Analysis");
        CP = new Phase("Change Propagation");
        phaseArray = new Phase[] {CL, IA, CP};
    }

    private Phase(String str){
        text = str;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Phase parse(String string){
        for(Phase phase: phaseArray){
            if (string.equals(phase.toString())){
                return phase;
            }
        }

        return new Phase("Unknown");
    }
}
