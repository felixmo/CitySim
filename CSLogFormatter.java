/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.logging.*;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * CSLogFormatter
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-15-2012
 * 
 * Formats log entries
 * 
 */

public class CSLogFormatter extends Formatter {

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * CONSTANTS *
     */
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");   // Date format

    // ---------------------------------------------------------------------------------------------------------------------

    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    /*
     * HELPERS *
     */

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}