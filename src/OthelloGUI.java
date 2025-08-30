import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OthelloGUI extends JPanel {
    private static final int CELL_SIZE = 80;
    private OthelloBoard game;
    private char aiPlayer = 'W';

    public OthelloGUI(OthelloBoard game) {
        this.game = game;
        this.setPreferredSize(new Dimension(OthelloBoard.SIZE * CELL_SIZE, OthelloBoard.SIZE * CELL_SIZE));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (game.gameOver()) return; // Do nothing if the game is over

                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;

                if (game.getCurrentPlayer() == 'B') {
                    if (game.isValidMove(row, col)) {
                        game.placeMove(row, col);
                        repaint();

                        if (game.gameOver()) {
                            showGameOverPanel();
                            return;
                        }

                        // AI Move (After a small delay for realism)
                        SwingUtilities.invokeLater(() -> {
                            int[] aiMove = OthelloAI.findBestMove(game, aiPlayer);
                            if (aiMove[0] != -1) {
                                game.placeMove(aiMove[0], aiMove[1]);
                                repaint();
                            }

                            if (game.gameOver()) {
                                showGameOverPanel();
                            }
                        });
                    } else {
                        // Invalid move: Show message
                        JOptionPane.showMessageDialog(OthelloGUI.this,
                                "Cannot place there! ❌",
                                "Invalid Move",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
    }

    private void showGameOverPanel() {
        int[] scores = game.getScores();
        String winnerMessage;

        if (scores[0] > scores[1]) {
            winnerMessage = "Black (You) Wins! 🏆";
        } else if (scores[1] > scores[0]) {
            winnerMessage = "White (AI) Wins! 🤖";
        } else {
            winnerMessage = "It's a Draw! 🤝";
        }

        JOptionPane.showMessageDialog(this,
                "Game Over!\n" +
                        "Final Scores:\n" +
                        "Black (You): " + scores[0] + "\n" +
                        "White (AI): " + scores[1] + "\n\n" +
                        winnerMessage,
                "Game Over", JOptionPane.INFORMATION_MESSAGE);

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 128, 0)); // Green board
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i <= OthelloBoard.SIZE; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(0, i * CELL_SIZE, OthelloBoard.SIZE * CELL_SIZE, i * CELL_SIZE);
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, OthelloBoard.SIZE * CELL_SIZE);
        }

        char[][] board = game.getBoard();
        for (int row = 0; row < OthelloBoard.SIZE; row++) {
            for (int col = 0; col < OthelloBoard.SIZE; col++) {
                if (board[row][col] == 'B') {
                    g.setColor(Color.BLACK);
                    g.fillOval(col * CELL_SIZE + 10, row * CELL_SIZE + 10, 60, 60);
                } else if (board[row][col] == 'W') {
                    g.setColor(Color.WHITE);
                    g.fillOval(col * CELL_SIZE + 10, row * CELL_SIZE + 10, 60, 60);
                }
            }
        }
    }
}
