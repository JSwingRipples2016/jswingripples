package org.incha.core.telemetry;

import org.incha.core.telemetry.utils.Phase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by emilio-aburto on 12-01-17.
 * Interface to be implemented by any Telemetry Logger
 */
public abstract class AbstractTelemetryLogger {
    private SimpleDateFormat dateFormat;
    private AbstractTelemetryLogger instance;

    public AbstractTelemetryLogger() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    }

    public abstract void log(Phase phase, String action, String entity);

    public AbstractTelemetryLogger getLogger(){
        if (instance == null){
            instance = this;
        }
        return instance;
    }

    public String getTimestamp(){
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}
