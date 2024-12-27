package ludo.mentis.aciem.tabellarius.util;

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

    public static <T> void traceMethodCall(Class<T> clazz, String methodName, Object... params) {
        if (log.isTraceEnabled()) {
            var message = new StringBuilder();
            message.append(clazz.getName());
            message.append(".");
            message.append(methodName);
            message.append("(");
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