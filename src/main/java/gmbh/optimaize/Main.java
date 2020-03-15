package gmbh.optimaize;

import gmbh.optimaize.service.Aggregator;
import gmbh.optimaize.service.AggregatorImpl;

import java.util.Scanner;

public class Main {

    private static final String QUIT = "q";
    private static final String GET_STATS = "?";
    private static final String HELP = "h";

    public static void main(String[] args) {

        Aggregator aggregator = new AggregatorImpl();
        try (Scanner s = new Scanner(System.in)) {

            printHelp(true);

            while (s.hasNext()) {
                String input = s.next().trim();
                switch (input) {
                    case QUIT:
                        return;
                    case GET_STATS:
                        System.out.println(aggregator.getStats());
                        break;
                    case HELP:
                        printHelp(false);
                        break;
                    default:
                        try {
                            float number = Float.parseFloat(input);
                            aggregator.submit(number);
                        } catch (NumberFormatException ignore) {
                            System.out.print("I don't get it. ");
                            printHelp(true);
                        }
                        break;
                }
            }
        }
    }

    private static void printHelp(boolean terse) {
        if (terse) {
            System.out.println("Enter a float number or '" + HELP + "' to get help");
        } else {
            System.out.println(
                    String.join(
                            System.lineSeparator(),
                            "'" + HELP + "' to get help",
                            "'" + QUIT + "' to quit",
                            "'" + GET_STATS + "' to get stats",
                            "<a number> to submit the number"
                    )
            );
        }
    }
}
