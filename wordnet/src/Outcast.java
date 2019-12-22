import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.HashMap;

public class Outcast {
    private final WordNet wordnet;
    private final HashMap<String, Integer> distanceCache;
    private final MaxPQ<outcastNode> outcastSum;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
        distanceCache = new HashMap<>();
        outcastSum = new MaxPQ<>(Comparator.comparingInt((outcastNode n) -> n.sum));
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    private int getDistance(String noun1, String noun2) {
        String key = noun1 + "-" + noun2;
        if (distanceCache.containsKey(key)) {
            return distanceCache.get(key);
        } else {
            int distance = wordnet.distance(noun1, noun2);
            distanceCache.put(key, distance);
            return distance;
        }
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        for (String noun : nouns) {
            int sum = 0;
            for (String noun2 : nouns) {
                if (noun != noun2) {
                    sum += getDistance(noun, noun2);
                }
            }
            outcastSum.insert(new outcastNode(noun, sum));
        }

        return outcastSum.max().noun;
    }

    private class outcastNode {
        private final String noun;
        private final int sum;

        public outcastNode(String noun, int sum) {
            this.noun = noun;
            this.sum = sum;
        }
    }
}