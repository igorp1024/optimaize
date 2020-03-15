package gmbh.optimaize.domain;

public class Stats {
    private double min;
    private double max;
    private int total;
    private double avg;

    public Stats(double min, double max, int total, double avg) {
        this.min = min;
        this.max = max;
        this.total = total;
        this.avg = avg;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getTotal() {
        return total;
    }

    public double getAvg() {
        return avg;
    }

    @Override
    public String toString() {
        return String.format("Minimal number: %.02f; Maximal number: %.02f; Average (of %d): %.02f;", min, max, total, avg);
    }
}
