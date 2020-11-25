import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class SeamCarver {

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pic = new Picture("./pic.png");
        SeamCarver sc = new SeamCarver(pic);
        StdOut.printf("%f", sc.energy(100, 200));
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
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
    }

}