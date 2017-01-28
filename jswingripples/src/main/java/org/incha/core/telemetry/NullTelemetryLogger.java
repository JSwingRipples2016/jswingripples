package org.incha.core.telemetry;

import org.incha.core.telemetry.utils.Phase;

/**
 * Created by eaburto on 26-01-17.
 */
public class NullTelemetryLogger extends AbstractTelemetryLogger {
    @Override
    public void log(Phase phase, String action, String entity) {

    }
}
