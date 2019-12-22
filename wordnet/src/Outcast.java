import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// Return the word that is furthest from all the other synset
public class Outcast {

    final private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }        // constructor takes a WordNet object

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    public String outcast(String[] nouns) {
        if (nouns.length == 0) throw new IllegalArgumentException("Noun list should not be empty");
        int maxDist = 0;
        String maxNoun = nouns[0];
        for (String cand : nouns) {
            int dist = 0;
            for (String noun : nouns) {
                dist += wordnet.distance(cand, noun);
            }
            if (dist > maxDist) {
                maxDist = dist;
                maxNoun = cand;
            }
        }
        return maxNoun;
    }  // given an array of WordNet nouns, return an outcast
}