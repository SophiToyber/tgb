package io.bf.messengers.telegram.logger;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerService {

    public static String populateTrace() {
        return populateTrace(null);
    }

    public static String populateTrace(String trace) {
        trace = Optional.ofNullable(trace).orElse(UUID.randomUUID().toString());
        MDC.put("trace", trace);
        return trace;
    }

    public static void clear() {
        MDC.clear();
    }
}
