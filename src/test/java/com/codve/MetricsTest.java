package com.codve;

import com.codahale.metrics.*;
import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author jiangyu26
 * @date 2021/5/13
 */
class MetricsTest {

    @Test
    public void counter() {
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);
        Counter counter = registry.counter("counter");

        IntStream.rangeClosed(0, 6).forEach(e -> {
            counter.inc();
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
        });
    }

    @Test
    public void gauge() {
        List<Integer> nums = new ArrayList<>();
        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);

        registry.register("watcher", (Gauge<Integer>) nums::size);

        IntStream.rangeClosed(0, 6).forEach(e -> {
            nums.add(1);
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
        });
    }

    @Test
    public void meter() {
        Random random = new Random();

        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);

        Meter meter = registry.meter("meter");

        IntStream.rangeClosed(1, 5).forEach(e -> {
            meter.mark(random.nextInt(10));
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
        });
    }

    @Test
    public void histogram() {
        Random random = new Random();

        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);

        Histogram histogram = new Histogram(new ExponentiallyDecayingReservoir());
        registry.register("histogram", histogram);

        IntStream.rangeClosed(1, 6).forEach(e -> {
            histogram.update(random.nextInt(1000));
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
        });

    }



}