import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class WordNet {

    private final HashMap<String, List<Integer>> wordNetMap;
    private final HashMap<Integer, Set<String>> wordSynsets;
    private final Digraph digraph;
    private final SAP sapInstance;
    private int verticesAmount;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("argument is null");
        wordNetMap = new HashMap<>();
        wordSynsets = new HashMap<>();
        verticesAmount = parseSynsets(synsets);
        digraph = new Digraph(verticesAmount);
        parseHypernyms(hypernyms);
        sapInstance = new SAP(digraph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.print(wordnet.distance("worm", "bird"));
        StdOut.printf(wordnet.sap("worm", "bird"));
    }

    private int parseSynsets(String synsets) {
        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            String line = synsetsIn.readLine();
            String fields[] = line.split(",");
            String[] wordsList = fields[1].split(" ");
            int synsetId = Integer.parseInt(fields[0]);

            for (String word : wordsList) {
                if (wordNetMap.get(word) != null) {
                    wordNetMap.get(word).add(synsetId);
                } else {
                    LinkedList<Integer> synsetIdList = new LinkedList<>();
                    synsetIdList.add(synsetId);
                    wordNetMap.put(word, synsetIdList);
                }

                if (wordSynsets.get(synsetId) != null) {
                    wordSynsets.get(synsetId).add(word);
                } else {
                    Set<String> set = new HashSet<String>();
                    set.add(word);
                    wordSynsets.put(synsetId, set);
                }
            }
            verticesAmount++;
        }
        return verticesAmount;
    }

    private void parseHypernyms(String hypernyms) {
        In hypernymsIn = new In(hypernyms);
        while (!hypernymsIn.isEmpty()) {
            String line = hypernymsIn.readLine();
            String fields[] = line.split(",");
            int n = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int v = Integer.parseInt(fields[i]);
                digraph.addEdge(n, v);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        List<String> keys = new ArrayList(wordNetMap.keySet());
        return keys;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("argument is null");
        return wordNetMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("argument is null");
        Iterable<Integer> it1 = wordNetMap.get(nounA);
        Iterable<Integer> it2 = wordNetMap.get(nounB);
        return sapInstance.length(it1, it2);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // self comment: method name should be sca? https://www.coursera.org/learn/algorithms-part2/discussions/all/threads/b77CquKjEemaWwpSRcqLyg
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("argument is null");
        Iterable<Integer> it1 = wordNetMap.get(nounA);
        Iterable<Integer> it2 = wordNetMap.get(nounB);

        int ancestor = sapInstance.ancestor(it1, it2);

        String result = "";
        for (String word : wordSynsets.get(ancestor)) {
            result += " " + word;
        }

        return result;
    }
}