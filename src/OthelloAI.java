import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class OthelloAI {
    private static int MAX_DEPTH = 6; // Depth limit for Minimax

    public static void setDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose AI Difficulty Level:",
                "Difficulty Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        switch (choice) {
            case 0 -> MAX_DEPTH = 2; // Easy
            case 1 -> MAX_DEPTH = 4; // Medium
            case 2 -> MAX_DEPTH = 6; // Hard
            default -> MAX_DEPTH = 4; // Default to Medium
        }
    }

    public static int[] findBestMove(OthelloBoard board, char aiPlayer) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int[] move : getValidMoves(board, aiPlayer)) {
            OthelloBoard tempBoard = new OthelloBoard(board);
            tempBoard.placeMove(move[0], move[1]);

            int score = minimax(tempBoard, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiPlayer);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int minimax(OthelloBoard board, int depth, int alpha, int beta, boolean isMaximizing, char aiPlayer) {
        if (depth == 0) return evaluateBoard(board, aiPlayer);

        char currentPlayer = isMaximizing ? aiPlayer : (aiPlayer == 'B' ? 'W' : 'B');
        List<int[]> moves = getValidMoves(board, currentPlayer);

        if (moves.isEmpty()) return evaluateBoard(board, aiPlayer);

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] move : moves) {
            OthelloBoard tempBoard = new OthelloBoard(board);
            tempBoard.placeMove(move[0], move[1]);

            int score = minimax(tempBoard, depth - 1, alpha, beta, !isMaximizing, aiPlayer);

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }

            if (beta <= alpha) break;
        }
        return bestScore;
    }

    private static List<int[]> getValidMoves(OthelloBoard board, char player) {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < OthelloBoard.SIZE; row++) {
            for (int col = 0; col < OthelloBoard.SIZE; col++) {
                if (board.isValidMove(row, col)) {
                    moves.add(new int[]{row, col});
                }
            }
        }
        return moves;
    }

//    private static int evaluateBoard(OthelloBoard board, char aiPlayer) {
//        int score = 0;
//        char[][] grid = board.getBoard();
//
//        for (int row = 0; row < OthelloBoard.SIZE; row++) {
//            for (int col = 0; col < OthelloBoard.SIZE; col++) {
//                if (grid[row][col] == aiPlayer) score++;
//                else if (grid[row][col] != ' ') score--;
//            }
//        }
//        return score;
//    }

    private static int evaluateBoard(OthelloBoard board, char aiPlayer) {
        char[][] grid = board.getBoard();
        char opponent = (aiPlayer == 'B') ? 'W' : 'B';

        int aiDiscs = 0, oppDiscs = 0;
        int aiCorners = 0, oppCorners = 0;
        int aiMobility = getValidMoves(board, aiPlayer).size();
        int oppMobility = getValidMoves(board, opponent).size();
        int aiStable = 0, oppStable = 0;

        // Disc Count: count discs for both AI and human which will be used later to decide the phase of the game [Time Complexity: O(64) = O(1)]
        for (int row = 0; row < OthelloBoard.SIZE; row++) {
            for (int col = 0; col < OthelloBoard.SIZE; col++) {
                char piece = grid[row][col];
                if (piece == aiPlayer) aiDiscs++;
                else if (piece == opponent) oppDiscs++;
            }
        }

        // Corner Heuristic: count the corners for AI and human , as having corner is highly stable and strategic position [Time Complexity: O(4) = O(1)]
        int[][] corners = {{0, 0}, {0, 7}, {7, 0}, {7, 7}};
        for (int[] c : corners) {
            char corner = grid[c[0]][c[1]];
            if (corner == aiPlayer) aiCorners++;
            else if (corner == opponent) oppCorners++;
        }

        // Stability Heuristic (simplified: corners + edges assumed stable) : count the discs placed on the edge , as edges are more stable than inner cells [Time Complexity: O(64) = O(1)]
        for (int i = 0; i < OthelloBoard.SIZE; i++) {
            for (int j = 0; j < OthelloBoard.SIZE; j++) {
                if ((i == 0 || i == 7 || j == 0 || j == 7) && grid[i][j] != ' ') {
                    if (grid[i][j] == aiPlayer) aiStable++;
                    else oppStable++;
                }
            }
        }

        // Adaptive Heuristic Weights Based on Game Phase: Priority given on domain knowledge + tuning for best AI decision-making.
        int totalDiscs = aiDiscs + oppDiscs;
        int mobilityScore = (aiMobility - oppMobility) * 5;
        int cornerScore = (aiCorners - oppCorners) * 25;
        int stabilityScore = (aiStable - oppStable) * 10;
        int discScore = (aiDiscs - oppDiscs);

        int score;

        if (totalDiscs < 20) {
            score = 2 * mobilityScore + 3 * cornerScore;
        }else if (totalDiscs < 50) {
            score = discScore + mobilityScore + cornerScore + stabilityScore;
        }else {
            score = 3 * discScore + cornerScore + stabilityScore;
        }

        return score;
    }
}
