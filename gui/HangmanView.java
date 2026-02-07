import backend.Database;
import backend.Hangman;
import java.awt.*;
import javax.swing.*;

public class HangmanView {

    // ---- GAME LOGIC ----
    Hangman game;
    Database db = new Database();

    // ---- UI COMPONENTS THAT NEED UPDATING ----
    private JLabel wordLabel;
    private JLabel livesLabel;
    private JLabel hangmanImage;
    private ImageIcon[] hangmanStages;
    private JPanel keyboardPanel;

    public JPanel createHangmanPanel() {

        // random words from database
        game = new Hangman(db.getRandomWord());

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(0, 80, 150));

        // ---------- TOP BAR ----------
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(new Color(0, 60, 120));

        JLabel title = new JLabel("GUESS.exe    VIRUS.exe");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.BOLD, 14));

        topBar.add(title);
        main.add(topBar, BorderLayout.NORTH);

        // ---------- CENTER ----------
        JPanel center = new JPanel(new GridLayout(1, 2));
        center.setOpaque(false);

        // LEFT: word + lives
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(0, 90, 170));
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        wordLabel = new JLabel(game.getGuessedWord(), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        wordLabel.setForeground(Color.WHITE);

        livesLabel = new JLabel("Lives: " + game.getRemainingAttempts(),
                SwingConstants.CENTER);
        livesLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        livesLabel.setForeground(Color.WHITE);

        left.add(wordLabel, BorderLayout.CENTER);
        left.add(livesLabel, BorderLayout.SOUTH);

        // RIGHT: hangman image placeholder
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(0, 70, 140));
        right.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        hangmanImage = new JLabel();
        hangmanImage.setHorizontalAlignment(SwingConstants.CENTER)
        hangmanImage.setVerticalAlignment(SwingConstants.CENTER);
        
        loadImages();

        right.add(hangmanImage, BorderLayout.CENTER);

        center.add(left);
        center.add(right);
        main.add(center, BorderLayout.CENTER);

        // ---------- CONTROL BAR ----------
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controlBar.setBackground(new Color(0, 60, 120));

        JButton restartBtn = new JButton("RESTART");
        JButton exitBtn = new JButton("HOME");

        restartBtn.setFocusable(false);
        exitBtn.setFocusable(false);

        controlBar.add(restartBtn);
        controlBar.add(exitBtn);

        // ---------- KEYBOARD ----------
        keyboardPanel = new JPanel(new GridLayout(3, 9, 5, 5));
        keyboardPanel.setBackground(new Color(0, 60, 120));
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (char c = 'A'; c <= 'Z'; c++) {
            JButton key = new JButton(String.valueOf(c));
            key.setFont(new Font("Monospaced", Font.BOLD, 12));
            key.setFocusable(false);

            char letter = Character.toLowerCase(c);

            key.addActionListener(e -> {
                key.setEnabled(false);
                game.guess(letter);
                updateUI();

                if (game.isGameOver()) {
                    handleGameOver(main);
                }
            });

            keyboardPanel.add(key);
        }

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(controlBar, BorderLayout.NORTH);
        bottom.add(keyboardPanel, BorderLayout.CENTER);

        main.add(bottom, BorderLayout.SOUTH);

        // ---------- CONTROL BUTTON ACTIONS ----------
        restartBtn.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).restartGame();
            }
        });

        exitBtn.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        return main;
    }

    // ---------- LOAD IMAGE ----------

    private void loadImages(){
        hangmanStages = new ImageIcon[7];
        for (int i = 0; i<7; i++){
            hangmanStages[i] = new ImageIcon("images/hangman" + i + ".png");
        }

        hangmanImage.setIcon(hangmanStages[0]);
        hangmanImage.setText("");
    }

    // ---------- UPDATE UI ----------
    private void updateUI() {
        wordLabel.setText(game.getGuessedWord());
        livesLabel.setText("Lives: " + game.getRemainingAttempts());

        // Later: swap images based on lives here
        int stage = 6 - game.getRemainingAttempts();
        hangmanImage.setIcon(hangmanStages[stage]);
    }

    // ---------- GAME OVER ----------
    private void handleGameOver(JPanel main) {
        disableKeyboard();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;

        if (game.isWordGuessed()) {
            mainFrame.showScreen(MainFrame.WIN);
        } else {
            mainFrame.showScreen(MainFrame.LOSE);
        }
    }

    private void disableKeyboard() {
        for (Component c : keyboardPanel.getComponents()) {
            c.setEnabled(false);
        }
    }
}
