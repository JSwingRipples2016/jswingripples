package org.incha.core.telemetry;

import org.incha.core.telemetry.utils.Phase;

public class SimpleTelemetryLogger extends AbstractTelemetryLogger {
    @Override
    public void log(Phase phase, String action, String entity){
        System.err.format("Timestamp: %s, Phase: %s, Action: %s, Entity: %s\n",
                getTimestamp(), phase, action, entity);
    }
}
