import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;


public class BoggleSolver {
    private static final int[][] directions = {
            {-1, 0}, // top
            {-1, 1}, // top right
            {0, 1}, // right
            {1, 1}, // bottom right
            {1, 0}, // bottom
            {1, -1}, // bottom left
            {0, -1}, // left
            {-1, -1} // top left
    };
    private int boardRows;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private SET<String> validWords;
    private int boardCols;
    private int boardCount;
    private TST dictionaryTrie;

    public BoggleSolver(String[] dictionary) {
        validWords = new SET<>();
        dictionaryTrie = new TST();

        for (String s : dictionary) {
            dictionaryTrie.put(s, 1);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private void dfs(BoggleBoard board, int row, int col, boolean[][] visited, StringBuilder sb) {
        if (row < 0 || row > boardRows - 1) {
            return;
        }

        if (col < 0 || col > boardCols - 1) {
            return;
        }


        // visit board[row][col]
        if (visited[row][col]) {
            return;
        }

        if (sb.length() > 0) {
            if (!dictionaryTrie.keysWithPrefix(sb.toString()).iterator().hasNext()) {
                return;
            }
        }

        visited[row][col] = true;
        char letter = board.getLetter(row, col);
        if (letter == 'Q') {
            sb.append(letter);
            sb.append('U');
        } else {
            sb.append(letter);
        }


        if (sb.length() >= 3) {
            String s = sb.toString();
            if (dictionaryTrie.contains(s)) {
                validWords.add(s);
            }
        }

        // iterate over 9 directions

        for (int[] direction : directions) {
            dfs(board, row + direction[0], col + direction[1], visited, sb);
        }


        sb.deleteCharAt(sb.length() - 1);
        if (sb.length() > 0) {
            if (sb.charAt(sb.length() - 1) == 'Q') {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        visited[row][col] = false;

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boardRows = board.rows();
        boardCols = board.cols();
        boardCount = boardRows * boardCols;

        boolean[][] visited = new boolean[boardRows][boardCols];

        for (int row = 0; row < boardRows; row++) {
            for (int col = 0; col < boardCols; col++) {
                StringBuilder sb = new StringBuilder();
                dfs(board, row, col, visited, sb);
            }
        }

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (dictionaryTrie.contains(word)) {
            int wordLength = word.length();
            if (wordLength >= 3 && wordLength <= 4) {
                score = 1;
            } else if (wordLength == 5) {
                score = 2;
            } else if (wordLength == 6) {
                score = 3;
            } else if (wordLength == 7) {
                score = 5;
            } else if (wordLength >= 8) {
                score = 11;
            }
        }
        return score;
    }
}
