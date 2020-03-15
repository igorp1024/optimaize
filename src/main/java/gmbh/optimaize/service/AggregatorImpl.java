package gmbh.optimaize.service;

import gmbh.optimaize.domain.Stats;

public class AggregatorImpl implements Aggregator {

    private double min = Float.MAX_VALUE;
    private double max = Float.MIN_VALUE;
    private int total = 0;
    private double sum = 0;

    @Override
    public synchronized void submit(double number) {

        sum += number;
        total++;

        if (min > number) {
            min = number;
        }

        if (max < number) {
            max = number;
        }
    }

    @Override
    public synchronized Stats getStats() {
        return (total == 0)
                ? new Stats(0, 0, 0, 0)
                : new Stats(min, max, total, sum / total);
    }
}
