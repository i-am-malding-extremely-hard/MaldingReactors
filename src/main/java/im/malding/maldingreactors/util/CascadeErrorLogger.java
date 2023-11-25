package im.malding.maldingreactors.util;

import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class CascadeErrorLogger implements Supplier<String> {
    private final String msg;
    private final TriFunction<String, String, TriConsumer<Logger, String, Object[]>, Object> baseLogHandler;

    public CascadeErrorLogger(TriFunction<String, String, TriConsumer<Logger, String, Object[]>, Object> logError) {
        this.msg = "";
        this.baseLogHandler = logError;
    }

    public CascadeErrorLogger(String msg, TriFunction<String, String, TriConsumer<Logger, String, Object[]>, Object> logError) {
        this.msg = msg;
        this.baseLogHandler = logError;
    }

    public CascadeErrorLogger modifyMSG(String additionalInfo) {
        return new CascadeErrorLogger(msg + additionalInfo, baseLogHandler);
    }

    public Object warn(String additionalMSG) {
        return baseLogHandler.apply(msg + additionalMSG,"Issue", (logger, s, objects) -> logger.warn(s, objects));
    }

    public <T> T warn(String additionalMSG, T object) {
        baseLogHandler.apply(msg + additionalMSG, "Issue", (logger, s, objects) -> logger.warn(s, objects));

        return object;
    }

    public Object error(String additionalMSG) {
        return baseLogHandler.apply(msg + additionalMSG, "Error", (logger, s, objects) -> logger.error(s, objects));
    }

    public <T> T error(String additionalMSG, T object) {
        baseLogHandler.apply(msg + additionalMSG, "Error", (logger, s, objects) -> logger.error(s, objects));

        return object;
    }

    @Override
    public String get() {
        return msg;
    }
}
