import java.awt.*;

public class OthelloBoardOpt {
    public static final int SIZE = 8;  // 8x8 board
    private char[][] board;
    private char currentPlayer;
    private int blackScore;
    private int whiteScore;
    private int emptyCells; // NEW: Track empty cells

    public OthelloBoardOpt() {
        board = new char[SIZE][SIZE];
        initializeBoard();
        currentPlayer = 'B'; // Black starts
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' '; // Empty board
            }
        }
        // Starting position
        board[3][3] = board[4][4] = 'W';
        board[3][4] = board[4][3] = 'B';

        blackScore = 2;
        whiteScore = 2;
        emptyCells = SIZE * SIZE - 4; // 60 empty cells
    }

    public boolean isValidMove(int row, int col) { //O(n)
        if (!isInBounds(row, col) || board[row][col] != ' ') return false;
        return checkDirections(row, col, false);
    }

    public boolean placeMove(int row, int col) { //O(n)
        if (!isValidMove(row, col)) return false;

        board[row][col] = currentPlayer;
        if (currentPlayer == 'B') blackScore++;
        else whiteScore++;
        emptyCells--; // Occupied one more cell

        checkDirections(row, col, true);
        switchPlayer();
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
    }

    private boolean checkDirections(int row, int col, boolean flip) {
        boolean valid = false;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        char opponent = (currentPlayer == 'B') ? 'W' : 'B';

        for (int[] dir : directions) {
            int x = row + dir[0], y = col + dir[1];
            boolean foundOpponent = false;

            while (isInBounds(x, y) && board[x][y] == opponent) {
                x += dir[0];
                y += dir[1];
                foundOpponent = true;
            }

            if (foundOpponent && isInBounds(x, y) && board[x][y] == currentPlayer) {
                valid = true;
                if (flip) {
                    flipPieces(row, col, dir, opponent);
                }
            }
        }
        return valid;
    }

    private void flipPieces(int row, int col, int[] dir, char opponent) { //O(n)
        int x = row + dir[0];
        int y = col + dir[1];

        while (board[x][y] == opponent) {
            board[x][y] = currentPlayer;
            if (currentPlayer == 'B') {
                blackScore++;
                whiteScore--;
            } else {
                whiteScore++;
                blackScore--;
            }
            x += dir[0];
            y += dir[1];
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean gameOver() {
        if (emptyCells == 0) return true; // No empty cells left, definitely game over

        // Check if current player has any valid move
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (isValidMove(row, col)) return false;
            }
        }

        // Also check if opponent has any valid move
        switchPlayer();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (isValidMove(row, col)) {
                    switchPlayer(); // Switch back
                    return false;
                }
            }
        }
        switchPlayer(); // Switch back
        return true;
    }

    public int[] getScores() {
        return new int[]{blackScore, whiteScore};
    }

    public OthelloBoardOpt(OthelloBoardOpt other) {
        this.board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, SIZE);
        }
        this.currentPlayer = other.currentPlayer;
        this.blackScore = other.blackScore;
        this.whiteScore = other.whiteScore;
        this.emptyCells = other.emptyCells;
    }
}
