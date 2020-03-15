package gmbh.optimaize.service;

import gmbh.optimaize.domain.Stats;

public interface Aggregator {

    void submit(double number);

    Stats getStats();
}
