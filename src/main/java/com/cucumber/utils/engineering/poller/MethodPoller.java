package com.cucumber.utils.engineering.poller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MethodPoller<T> {
    private Logger log = LogManager.getLogger();

    private Duration pollDurationSec = Duration.ofSeconds(30);
    private Long pollIntervalMillis = 3000L;
    private Double exponentialBackOff = 1.0;

    private Supplier<T> pollMethod = null;
    private Predicate<T> pollResultPredicate = null;

    public MethodPoller<T> duration(Long pollIntervalMillis) {
        return duration((Duration) null, pollIntervalMillis);
    }

    public MethodPoller<T> duration(Integer pollDurationSec) {
        return duration(pollDurationSec, null);
    }

    public MethodPoller<T> duration(Integer pollDurationSec, Long pollIntervalMillis) {
        return duration(pollDurationSec != null ? Duration.ofSeconds(pollDurationSec) : (Duration) null, pollIntervalMillis);
    }

    public MethodPoller<T> duration(Duration pollDurationSec, Long pollIntervalMillis) {
        this.pollDurationSec = pollDurationSec != null ? pollDurationSec : this.pollDurationSec;
        this.pollIntervalMillis = pollIntervalMillis != null ? pollIntervalMillis : this.pollIntervalMillis;
        return this;
    }

    public MethodPoller<T> exponentialBackOff(double exp) {
        this.exponentialBackOff = exp;
        return this;
    }

    public MethodPoller<T> method(Supplier<T> supplier) {
        pollMethod = supplier;
        return this;
    }

    public MethodPoller<T> until(Predicate<T> predicate) {
        pollResultPredicate = predicate;
        return this;
    }

    public T poll() {
        log.debug("Polling for result...");
        boolean pollSucceeded = false;
        boolean pollTimeout = false;
        T result = null;
        while (!pollSucceeded && !pollTimeout) {
            long start = System.currentTimeMillis();
            result = pollMethod.get();
            pollSucceeded = pollResultPredicate.test(result);
            if (!pollSucceeded) {
                try {
                    log.debug("Poll failed, I'll take another shot after {}ms", pollIntervalMillis);
                    Thread.sleep(pollIntervalMillis);
                    pollIntervalMillis = (long) (pollIntervalMillis * exponentialBackOff);
                    long elapsed = System.currentTimeMillis() - start;
                    pollDurationSec = pollDurationSec.minusMillis(elapsed);
                    if (pollDurationSec.isZero() || pollDurationSec.isNegative()) {
                        pollTimeout = true;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        log.debug(!pollTimeout ? "Found correct result" : "Poll timeout");
        return result;
    }
}