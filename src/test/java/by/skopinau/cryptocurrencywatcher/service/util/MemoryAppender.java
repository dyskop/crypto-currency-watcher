package by.skopinau.cryptocurrencywatcher.service.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * In memory slf4j appender<br/>
 * Convenient appender to be able to check slf4j invocations
 */
public class MemoryAppender extends ListAppender<ILoggingEvent> {
    public void reset() {
        this.list.clear();
    }

    public boolean contains(String string, Level level) {
        return this.list.stream()
                .anyMatch(event -> event.toString().contains(string)
                        && event.getLevel().equals(level));
    }

    public int countEventsForLogger(String loggerName) {
        return (int) this.list.stream()
                .filter(event -> event.getLoggerName().contains(loggerName)).count();
    }
}