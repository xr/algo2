import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.StdIn;

import java.util.HashMap;

public class SAP {

    private final Digraph digraph;
    private final HashMap<String, Integer> lengthCache;
    private final HashMap<String, Integer> ancestorCache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("argument is null");
        digraph = new Digraph(G);
        lengthCache = new HashMap<>();
        ancestorCache = new HashMap<>();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void assertNotNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("argument is null");
    }


    private void assertVertexInRange(int v) {
        if (v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException("vertex is not in range");
        }
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        assertVertexInRange(v);
        assertVertexInRange(w);
        String key = "" + v + "-" + w;
        if (lengthCache.containsKey(key)) {
            return lengthCache.get(key);
        }
        int[] results = helper(new BreadthFirstDirectedPaths(digraph, v), new BreadthFirstDirectedPaths(digraph, w));
        int result = modify(results)[0];
        String key1 = "" + v + "-" + w;
        String key2 = "" + w + "-" + v;
        lengthCache.put(key1, result);
        lengthCache.put(key2, result);
        return result;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        assertVertexInRange(v);
        assertVertexInRange(w);
        String key = "" + v + "-" + w;
        if (ancestorCache.containsKey(key)) {
            return ancestorCache.get(key);
        }

        int[] results = helper(new BreadthFirstDirectedPaths(digraph, v), new BreadthFirstDirectedPaths(digraph, w));
        int result = modify(results)[1];
        String key1 = "" + v + "-" + w;
        String key2 = "" + w + "-" + v;
        ancestorCache.put(key1, result);
        ancestorCache.put(key2, result);
        return result;
    }

    private int[] helper(BreadthFirstDirectedPaths bfs1, BreadthFirstDirectedPaths bfs2) {
        int length = Integer.MAX_VALUE, ancestor = 0;
        for (int i = 0, temp = 0; i < digraph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                temp = bfs1.distTo(i) + bfs2.distTo(i);
                if (temp < length) {
                    length = temp;
                    ancestor = i;
                }
            }
        }
        return new int[]{length, ancestor};
    }

    private int[] modify(int[] a) {
        if (a[0] == Integer.MAX_VALUE) {
            a[0] = -1;
            a[1] = -1;
        }
        return a;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        assertNotNull(v);
        assertNotNull(w);
        int shortestLength = Integer.MAX_VALUE;
        for (int first : v) {
            for (int second : w) {
                int len = length(first, second);
                if (len != -1) {
                    shortestLength = Integer.min(len, shortestLength);
                }
            }
        }
        return shortestLength != Integer.MAX_VALUE ? shortestLength : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        assertNotNull(v);
        assertNotNull(w);
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int first : v) {
            for (int second : w) {
                int newShortestLength = length(first, second);
                if (newShortestLength != -1 && newShortestLength < shortestLength) {
                    shortestLength = newShortestLength;
                    ancestor = ancestor(first, second);
                }
            }
        }
        return ancestor;
    }
}
