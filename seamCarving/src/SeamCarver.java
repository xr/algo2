import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Topological;

import java.awt.*;

public class SeamCarver {

    private Picture picture;

    private EdgeWeightedDigraph verticalGraph;

    private Topological top;

    private double[] distTo;
    private DirectedEdge[] edgeTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        this.picture = picture;
        this.verticalGraph = this.buildVerticalGraph();
        this.top = new Topological(this.verticalGraph);
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pic = new Picture("./pic.png");
        SeamCarver sc = new SeamCarver(pic);
//        StdOut.printf("%f", sc.energy(100, 284));


//        for (DirectedEdge e : sc.verticalGraph.adj(1)) {
//            StdOut.println(e);
//        }
//
//        for (int i = 0; i < sc.picture.width(); i++) {
//            StdOut.println(sc.verticalGraph.outdegree(i));
//        }


//        StdOut.println(sc.verticalCoordinateToIndex(506, 0));
        int[] b = sc.indexToVerticalCoordinate(1013);
        StdOut.println(b[0]);
        StdOut.println(b[1]);


//        for (int v : sc.top.order()) {
//            StdOut.println(v);
//        }

    }


    private int verticalCoordinateToIndex(int col, int row) {
        return row * this.picture.width() + col;
    }

    private int[] indexToVerticalCoordinate(int x) {
        int width = this.picture.width();
        int[] result = new int[2];
        result[0] = x % width; // column
        result[1] = x / width; // row
        return result;
    }

    private EdgeWeightedDigraph buildVerticalGraph() {
        int vertices = this.picture.height() * this.picture.width();
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(vertices);
        for (int w = 0; w < this.picture.width(); w++) {
            for (int h = 0; h < this.picture.height() - 1; h++) {
                int from = this.verticalCoordinateToIndex(w, h);
                int toBottom = this.verticalCoordinateToIndex(w, h + 1);
                int toBottomLeft = this.verticalCoordinateToIndex(w - 1, h + 1);
                int toBottomRight = this.verticalCoordinateToIndex(w + 1, h + 1);
                DirectedEdge toBottomEdge = new DirectedEdge(from, toBottom, this.energy(w, h) + this.energy(w, h + 1));
                g.addEdge(toBottomEdge);

                if (w > 0 && w < this.picture.width() - 1) {
                    // bottom left, bottom, bottom right
                    DirectedEdge toBottomLeftEdge = new DirectedEdge(from, toBottomLeft, this.energy(w, h) + this.energy(w - 1, h + 1));
                    DirectedEdge toBottomRightEdge = new DirectedEdge(from, toBottomRight, this.energy(w, h) + this.energy(w + 1, h + 1));
                    g.addEdge(toBottomLeftEdge);
                    g.addEdge(toBottomRightEdge);
                } else if (w == 0) {
                    // down and bottom right
                    DirectedEdge toBottomRightEdge = new DirectedEdge(from, toBottomRight, this.energy(w, h) + this.energy(w + 1, h + 1));
                    g.addEdge(toBottomRightEdge);
                } else if (w == this.picture.width() - 1) {
                    //StdOut.println(w);
                    // bottom and bottom left
                    DirectedEdge toBottomLeftEdge = new DirectedEdge(from, toBottomLeft, this.energy(w, h) + this.energy(w - 1, h + 1));
                    g.addEdge(toBottomLeftEdge);
                }
            }
        }
        return g;
    }

    private boolean isBorderPixel(int x, int y) {
        if (x == 0 || y == 0 || x == this.width() - 1 || y == this.height() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private int square(Color color1, Color color2) {
        int deltaR = color1.getRed() - color2.getRed();
        int deltaG = color1.getGreen() - color2.getGreen();
        int deltaB = color1.getBlue() - color2.getBlue();

        return deltaR * deltaR + deltaG * deltaG + deltaB * deltaB;
    }

    private int deltaXSquare(int x, int y) {
        Color color1 = this.picture.get(x + 1, y);
        Color color2 = this.picture.get(x - 1, y);
        return this.square(color1, color2);
    }

    private int deltaYSquare(int x, int y) {
        Color color1 = this.picture.get(x, y + 1);
        Color color2 = this.picture.get(x, y - 1);
        return this.square(color1, color2);
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= this.width() || x < 0 || y >= this.height() || y < 0) {
            throw new IllegalArgumentException("out of range");
        }
        boolean isBorder = this.isBorderPixel(x, y);
        if (isBorder) {
            return 1000;
        } else {
            return Math.sqrt(this.deltaXSquare(x, y) + this.deltaYSquare(x, y));
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }

        if (this.height() <= 1) {
            throw new IllegalArgumentException("height is less than 1px");
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }

        if (this.width() <= 1) {
            throw new IllegalArgumentException("width is less than 1px");
        }
    }

}