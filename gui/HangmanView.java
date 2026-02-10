import backend.Database;
import backend.Hangman;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class HangmanView {

    Hangman game;
    Database db = new Database();

    private JLabel wordLabel;
    private JLabel livesLabel;
    private ImagePanel hangmanImagePanel;
    private JPanel keyboardPanel;
    private JButton[] buttons = new JButton[26];

    // Hangman Game Panel
    public JPanel createHangmanPanel() {
        game = new Hangman(db.getRandomWord());

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(0, 80, 150));
        main.add(createCenterPanel(), BorderLayout.CENTER);
        main.add(createBottomPanel(main), BorderLayout.SOUTH);

        return main;
    }

    // Create center panel: word, lives, and hangman image
    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new GridLayout(1, 2));
        center.setOpaque(false);

        center.add(createLeftPanel());
        center.add(createRightPanel());

        return center;
    }

    // Left panel: word and lives
    private JPanel createLeftPanel() {
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(0, 90, 170));
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        wordLabel = new JLabel(game.getGuessedWord(), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        wordLabel.setForeground(Color.WHITE);

        livesLabel = new JLabel("Lives: " + game.getRemainingAttempts(), SwingConstants.CENTER);
        livesLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        livesLabel.setForeground(Color.WHITE);

        left.add(wordLabel, BorderLayout.CENTER);
        left.add(livesLabel, BorderLayout.SOUTH);

        return left;
    }

    // Right panel: hangman image
    private JPanel createRightPanel() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(0, 70, 140));
        right.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Use ImagePanel instead of JLabel for the hangman image
        hangmanImagePanel = new ImagePanel("images/S0.gif");
        hangmanImagePanel.setOpaque(false);
        
        right.add(hangmanImagePanel, BorderLayout.CENTER);
        return right;
    }

    // Bottom panel: controls and keyboard
    private JPanel createBottomPanel(JPanel main) {
        JPanel bottom = new JPanel(new BorderLayout());

        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controlBar.setBackground(new Color(0, 60, 120));

        JButton restartBtn = new JButton("RESTART");
        JButton exitBtn = new JButton("HOME");
        restartBtn.setFocusable(false);
        exitBtn.setFocusable(false);

        restartBtn.addActionListener(e -> restartGame(main));
        exitBtn.addActionListener(e -> exitToHome(main));

        controlBar.add(restartBtn);
        controlBar.add(exitBtn);

        keyboardPanel = createKeyboardPanel(main);

        bottom.add(controlBar, BorderLayout.NORTH);
        bottom.add(keyboardPanel, BorderLayout.CENTER);

        return bottom;
    }

    // Keyboard Panel with buttons
    private JPanel createKeyboardPanel(JPanel main) {
        JPanel keyboard = new JPanel(new GridLayout(3, 9, 5, 5));
        keyboard.setBackground(new Color(0, 60, 120));
        keyboard.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        AbstractAction actionA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String event = e.getActionCommand();
                char c = event.charAt(0);
                char letter = Character.toLowerCase(c);
                int count = letter - 'a';
                buttons[count].setEnabled(false);
                game.guess(letter);
                updateUI();
                if (game.isGameOver())
                    handleGameOver(main);
            }
        };

        InputMap im = main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = main.getActionMap();

        for (char c = 'A'; c <= 'Z'; c++) {
            String actionMapKey = "press" + c;
            KeyStroke keyStroke = KeyStroke.getKeyStroke(c, 0);
            im.put(keyStroke, actionMapKey);
            am.put(actionMapKey, actionA);
            
            int count = c - 'A';
            buttons[count] = new JButton(String.valueOf(c));
            buttons[count].setFont(new Font("Monospaced", Font.BOLD, 12));
            buttons[count].setFocusable(false);
            char letter = Character.toLowerCase(c);
            

            buttons[count].addActionListener(e -> {
                buttons[count].setEnabled(false);
                game.guess(letter);
                updateUI();
                if (game.isGameOver())
                    handleGameOver(main);
            });

            keyboard.add(buttons[count]);
        }

        return keyboard;
    }

    // Update word, lives, and loads hangman image
    private void updateUI() {
        wordLabel.setText(game.getGuessedWord());
        livesLabel.setText("Lives: " + game.getRemainingAttempts());
        
        String[] imageNames = {"S0.gif", "S1.gif", "S2.gif", "S3.gif", "S4.gif", "S5.gif", "S6.gif"};
        int index = 6 - game.getRemainingAttempts();
        
        JPanel right = (JPanel) hangmanImagePanel.getParent();
        right.remove(hangmanImagePanel);
        hangmanImagePanel = new ImagePanel("images/" + imageNames[index]);
        hangmanImagePanel.setOpaque(false);
        right.add(hangmanImagePanel, BorderLayout.CENTER);
        right.revalidate();
        right.repaint();
    }

    // Handle end of game
    private void handleGameOver(JPanel main) {
        disableKeyboard();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;
        if (game.isWordGuessed())
            mainFrame.showScreen(MainFrame.WIN);
        else
            mainFrame.showScreen(MainFrame.LOSE);
    }

    // Disable all buttons
    private void disableKeyboard() {
        for (Component c : keyboardPanel.getComponents())
            c.setEnabled(false);
    }

    // Restart Game
    private void restartGame(JPanel main) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame)
            ((MainFrame) frame).restartGame();
    }

    // Go to home
    private void exitToHome(JPanel main) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame)
            ((MainFrame) frame).showScreen(MainFrame.HOME);
    }
}
