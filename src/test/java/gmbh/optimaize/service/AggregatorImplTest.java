package gmbh.optimaize.service;

import gmbh.optimaize.domain.Stats;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class AggregatorImplTest {

    @Test
    void initializationTest() {
        assertThat(new AggregatorImpl().getStats())
                .isNotNull()
                .isInstanceOf(Stats.class)
                .extracting(Stats::getMin, Stats::getMax, Stats::getTotal, Stats::getAvg)
                .containsExactly(0.0d, 0.0d, 0, 0.0d);
    }

    @Test
    void naiveTest() {
        Aggregator aggregator = new AggregatorImpl();
        Arrays.stream(
                new Double[]{1.0d, 2.0d, 3.0d, 4.0d, 5.0d,}
        ).forEach(aggregator::submit);
        assertThat(aggregator.getStats())
                .isNotNull()
                .isInstanceOf(Stats.class)
                .extracting(Stats::getMin, Stats::getMax, Stats::getTotal, Stats::getAvg)
                .containsExactly(1.0d, 5.0d, 5, 3.0d);
    }

    @Test
    void parallelStressTest() throws InterruptedException {
        // Given
        Aggregator aggregator = new AggregatorImpl();
        final int THREADS = 1000;
        final int TASKS_PER_THREAD = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(THREADS);
        Phaser phaser = new Phaser();
        phaser.register();

        // When
        for (int i = 0; i < THREADS; i++) {
            final int degree = i;
            executor.submit(() -> {
                phaser.register();
                try {
                    // Launch all tasks simultaneously
                    latch.await();
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < TASKS_PER_THREAD; j++) {
                    int number = calcNumber(TASKS_PER_THREAD, degree + 1, j + 1);
                    aggregator.submit(number);
                }
                // Report the job completion
                phaser.arrive();
            });
            latch.countDown();
        }

        phaser.arriveAndAwaitAdvance();
        executor.shutdown();
        executor.awaitTermination(60L, TimeUnit.SECONDS);

        // Then
        double min = Float.MAX_VALUE;
        double max = Float.MIN_VALUE;
        int count = 0;
        double sum = 0.0f;
        for (int i = 0; i < THREADS; i++) {
            for (int j = 0; j < TASKS_PER_THREAD; j++) {
                int number = calcNumber(TASKS_PER_THREAD, i + 1, j + 1);
                if (min > number) {
                    min = number;
                }
                if (max < number) {
                    max = number;
                }
                sum += number;
                count++;
            }
        }
        Stats stats = aggregator.getStats();
        assertThat(stats)
                .extracting(Stats::getMin, Stats::getMax, Stats::getTotal)
                .containsExactly(min, max, count);
        assertThat(stats.getAvg()).isEqualTo(sum / count, offset(0.001d))
        ;
    }

    private int calcNumber(int TASKS_PER_THREAD, int degree, int value) {
        return degree * TASKS_PER_THREAD + value;
    }
}
