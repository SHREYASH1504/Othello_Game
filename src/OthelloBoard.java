import java.awt.*;

public class OthelloBoard {
    public static final int SIZE = 8;
    private char[][] board;
    private char currentPlayer;

    public OthelloBoard() {
        board = new char[SIZE][SIZE];
        initializeBoard();
        currentPlayer = 'B';
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' ';
            }
        }
        // Starting position
        board[3][3] = board[4][4] = 'W';
        board[3][4] = board[4][3] = 'B';
    }

    public boolean isValidMove(int row, int col) {
        if (board[row][col] != ' ') return false;
        return checkDirections(row, col, false);
    }

    public void placeMove(int row, int col) {
        if (!isValidMove(row, col)) return;
        board[row][col] = currentPlayer;
        checkDirections(row, col, true);
        currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
    }

    private boolean checkDirections(int row, int col, boolean flip) {
        boolean valid = false;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        char opponent = (currentPlayer == 'B') ? 'W' : 'B';

        for (int[] dir : directions) {
            int x = row + dir[0], y = col + dir[1];
            boolean foundOpponent = false;

            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] == opponent) {
                x += dir[0];
                y += dir[1];
                foundOpponent = true;
            }

            if (foundOpponent && x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] == currentPlayer) {
                valid = true;
                if (flip) {
                    x = row + dir[0];
                    y = col + dir[1];
                    while (board[x][y] == opponent) {
                        board[x][y] = currentPlayer;
                        x += dir[0];
                        y += dir[1];
                    }
                }
            }
        }
        return valid;
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean gameOver() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (isValidMove(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] getScores() {
        int blackScore = 0, whiteScore = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 'B') blackScore++;
                else if (board[row][col] == 'W') whiteScore++;
            }
        }
        return new int[]{blackScore, whiteScore};
    }

    public OthelloBoard(OthelloBoard other) {
        this.board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, SIZE);
        }
        this.currentPlayer = other.currentPlayer;
    }

}
