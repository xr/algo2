import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SAP {

    private final Digraph digraph;
    private final HashMap<Integer, BreadthFirstDirectedPaths> bfsMap;
    private final HashMap<String, Integer> lengthCache;
    private final HashMap<String, Integer> ancestorCache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        assertDAG(G);
        StdOut.printf("DAG checking passed");
        this.digraph = new Digraph(G);
        this.bfsMap = new HashMap<>();
        this.lengthCache = new HashMap<>();
        this.ancestorCache = new HashMap<>();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Iterable<Integer> it1 = Arrays.asList(13, 23, 24);
        Iterable<Integer> it2 = Arrays.asList(6, 16, 17);

        int length = sap.length(it1, it2);
        int ancestor = sap.ancestor(it1, it2);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length = sap.length(v, w);
//            int ancestor = sap.ancestor(v, w);
//            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        }
    }

    private void assertDAG(Digraph G) {
        Topological top = new Topological(G);
        if (!top.hasOrder()) throw new IllegalArgumentException("the digraph is not a DAG, can not continue");
    }

    private void assertVertexInRange(int v) {
        if (v < 0 || v >= this.digraph.V()) {
            throw new IllegalArgumentException("vertex is not in range");
        }
    }

    private void bfs(Digraph g, int s) {
        this.bfsMap.computeIfAbsent(s, k -> new BreadthFirstDirectedPaths(g, s));
    }

    public int sizeOfIterable(Iterable<Integer> it) {
        int count = 0;
        for (int i : it) count++;
        return count;
    }

    private int recur(Iterable<Integer> it, int w, int nearestLengthSoFar) {
        if (this.sizeOfIterable(it) == 0) {
            return -1;
        }

        List<Integer> parentVertices = new ArrayList<Integer>();

        int ancestor = -1;
        int nearestLength = nearestLengthSoFar;
        for (int vertex : it) {
            for (int adj : this.digraph.adj(vertex)) {

                if (this.bfsMap.get(w).hasPathTo(adj)) {
                    int newNearestLength = Integer.min(this.bfsMap.get(w).distTo(adj), nearestLength);
                    if (newNearestLength < nearestLength) {
                        nearestLength = newNearestLength;
                        ancestor = adj;
                    }
                } else {
                    parentVertices.add(adj);
                }

            }
        }


        return ancestor != -1 ? ancestor : recur(parentVertices, w, nearestLength);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        assertVertexInRange(v);
        assertVertexInRange(w);
        String key = "" + v + "-" + w;
        if (this.lengthCache.containsKey(key)) {
            return this.lengthCache.get(key);
        }
        this.bfs(this.digraph, v);
        this.bfs(this.digraph, w);
        int ancestor = this.ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        int len = this.bfsMap.get(w).distTo(ancestor) + this.bfsMap.get(v).distTo(ancestor);
        String key1 = "" + v + "-" + w;
        String key2 = "" + w + "-" + v;
        this.lengthCache.put(key1, len);
        this.lengthCache.put(key2, len);
        return len;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        assertVertexInRange(v);
        assertVertexInRange(w);
        String key = "" + v + "-" + w;
        if (this.ancestorCache.containsKey(key)) {
            return this.ancestorCache.get(key);
        }
        this.bfs(this.digraph, w);
        int ancestor = -1;
        if (this.bfsMap.get(w).hasPathTo(v)) {
            ancestor = v;
        } else {
            ancestor = this.recur(Arrays.asList(v), w, Integer.MAX_VALUE);
        }

        String key1 = "" + v + "-" + w;
        String key2 = "" + w + "-" + v;
        this.ancestorCache.put(key1, ancestor);
        this.ancestorCache.put(key2, ancestor);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int shortestLength = Integer.MAX_VALUE;
        for (int first : v) {
            for (int second : w) {
                int len = this.length(first, second);
                if (len != -1) {
                    shortestLength = Integer.min(len, shortestLength);
                }
            }
        }
        return shortestLength != Integer.MAX_VALUE ? shortestLength : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int first : v) {
            for (int second : w) {
                int newShortestLength = this.length(first, second);
                if (newShortestLength != -1 && newShortestLength < shortestLength) {
                    shortestLength = newShortestLength;
                    ancestor = this.ancestor(first, second);
                }
            }
        }
        return ancestor;
    }
}
