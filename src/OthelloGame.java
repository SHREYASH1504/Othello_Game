import javax.swing.*;

public class OthelloGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OthelloAI.setDifficulty();
            OthelloBoard board = new OthelloBoard();
            JFrame frame = new JFrame("Othello Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new OthelloGUI(board));
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
