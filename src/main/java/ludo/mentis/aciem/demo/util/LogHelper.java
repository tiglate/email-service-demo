package ludo.mentis.aciem.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {

    private static Logger log = LoggerFactory.getLogger(LogHelper.class);

    private LogHelper() {
        throw new IllegalStateException("Utility class");
    }

    static void setLogger(Logger logger) {
        log = logger;
    }

    public static void traceMethodCall(String methodName, Object... params) {
        if (log.isTraceEnabled()) {
            StringBuilder message = new StringBuilder(methodName + "(");
            for (int i = 0; i < params.length; i++) {
                message.append(params[i]);
                if (i < params.length - 1) {
                    message.append(", ");
                }
            }
            message.append(")");
            log.trace(message.toString());
        }
    }
}