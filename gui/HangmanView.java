import backend.Database;
import backend.Hangman;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class HangmanView {

    Hangman game;
    Database db = new Database();
    private SoundManager soundManager;

    private JLabel wordLabel;
    private JLabel livesLabel;
    private ImagePanel hangmanImagePanel;
    private JPanel keyboardPanel;
    private JButton[] buttons = new JButton[26];
    private static long lastTime = 0;
    private static final long minimum = 200;
    
    // Timers for S6 and flatline synchronization
    private Timer s6DisplayTimer;
    private Timer s6SoundTimer;
    private Timer flatlineTimer;
    private Timer gameOverTransitionTimer;
    private String currentHangmanSound = null;
    private boolean isS6Playing = false;
    private boolean gameOverTriggered = false;
    private JPanel mainPanel;
    
    // Sound durations (in milliseconds)
    private static final int S6_SOUND_DURATION = 2000; // S6.wav is 2 seconds
    private static final int FLATLINE_DURATION = 2000; // Flatline.wav is 2 seconds
    private static final int S6_TOTAL_DISPLAY_TIME = 6000; // 6 seconds total for S6.gif

    // Hangman Game Panel
    public JPanel createHangmanPanel() {
        soundManager = SoundManager.getInstance();
        game = new Hangman(db.getRandomWord());
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 80, 150));
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(mainPanel), BorderLayout.SOUTH);

        return mainPanel;
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
                long current = System.currentTimeMillis();
                if(current - lastTime < minimum){
                    return;
                }
                
                // Don't process if game is already over
                if (gameOverTriggered) {
                    return;
                }
                
                // Play keyboard sound for keyboard press
                soundManager.playSound(SoundManager.KEYBOARD);
                
                String event = e.getActionCommand();
                char c = event.charAt(0);
                char letter = Character.toLowerCase(c);
                int count = letter - 'a';
                if (count >= 0 && count < buttons.length && buttons[count] != null && buttons[count].isEnabled()) {
                    buttons[count].setEnabled(false);
                    game.guess(letter);
                    updateUI();
                    
                    // Check if game is over after update
                    if (game.isGameOver() && !gameOverTriggered) {
                        handleGameOver(main);
                    }
                }
                lastTime = current;
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
            final char letter = Character.toLowerCase(c);
            final int buttonIndex = count;

            buttons[count].addActionListener(e -> {
                // Don't process if game is already over
                if (gameOverTriggered) {
                    return;
                }
                
                // Play keyboard sound for mouse click
                soundManager.playSound(SoundManager.KEYBOARD);
                
                JButton source = (JButton) e.getSource();
                if (source.isEnabled()) {
                    source.setEnabled(false);
                    game.guess(letter);
                    updateUI();
                    
                    // Check if game is over after update
                    if (game.isGameOver() && !gameOverTriggered) {
                        handleGameOver(main);
                    }
                }
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
        String[] soundNames = {null, SoundManager.S1, SoundManager.S2, SoundManager.S3, 
                               SoundManager.S4, SoundManager.S5, SoundManager.S6};
        
        int index = 6 - game.getRemainingAttempts();
        // Ensure index is within bounds
        index = Math.min(index, 6);
        
        // Stop all S6-related timers if we're leaving S6
        if (isS6Playing && index != 6) {
            stopS6Timers();
        }
        
        // Stop previous hangman sound if playing
        if (currentHangmanSound != null) {
            soundManager.stopSound(currentHangmanSound);
        }
        
        // Play new hangman sound if index > 0 (S1 to S6)
        if (index > 0 && index <= 6) {
            currentHangmanSound = soundNames[index];
            
            if (index == 6) {
                // Special handling for S6
                handleS6Sound();
            } else {
                // For S1-S5, play in a continuous loop
                soundManager.playSoundLoop(currentHangmanSound);
            }
        }
        
        // Update the image panel
        Container parent = hangmanImagePanel.getParent();
        if (parent != null) {
            parent.remove(hangmanImagePanel);
            hangmanImagePanel = new ImagePanel("images/" + imageNames[index]);
            hangmanImagePanel.setOpaque(false);
            parent.add(hangmanImagePanel, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    // Special handling for S6 sound sequence
    private void handleS6Sound() {
        isS6Playing = true;
        gameOverTriggered = true; // Mark that game is over but we're showing animation
        
        // Disable keyboard immediately
        disableKeyboard();
        
        // First, play S6.wav (2 seconds)
        soundManager.playSound(SoundManager.S6);
        
        // Schedule Flatline-after-S6 to play immediately after S6 finishes
        s6SoundTimer = new Timer(S6_SOUND_DURATION, e -> {
            soundManager.playSound(SoundManager.FLATLINE);
            
            // Schedule the transition to game over screen after flatline finishes
            flatlineTimer = new Timer(FLATLINE_DURATION, e2 -> {
                // After flatline finishes, wait a bit more then transition
                gameOverTransitionTimer = new Timer(2000, e3 -> { // Remaining 2 seconds of S6 display
                    transitionToGameOver();
                });
                gameOverTransitionTimer.setRepeats(false);
                gameOverTransitionTimer.start();
            });
            flatlineTimer.setRepeats(false);
            flatlineTimer.start();
        });
        s6SoundTimer.setRepeats(false);
        s6SoundTimer.start();
        
        // Set a timer for the total S6 display duration (6 seconds) as backup
        s6DisplayTimer = new Timer(S6_TOTAL_DISPLAY_TIME, e -> {
            // If we haven't transitioned yet for some reason, do it now
            if (isS6Playing) {
                transitionToGameOver();
            }
        });
        s6DisplayTimer.setRepeats(false);
        s6DisplayTimer.start();
    }
    
    // Transition to appropriate game over screen
    private void transitionToGameOver() {
        if (!isS6Playing) return; // Already transitioned
        
        stopS6Timers();
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;
        
        if (game.isWordGuessed()) {
            mainFrame.showScreen(MainFrame.WIN);
        } else {
            mainFrame.showScreen(MainFrame.LOSE);
        }
    }
    
    // Stop all S6-related timers
    private void stopS6Timers() {
        if (s6SoundTimer != null && s6SoundTimer.isRunning()) {
            s6SoundTimer.stop();
        }
        if (flatlineTimer != null && flatlineTimer.isRunning()) {
            flatlineTimer.stop();
        }
        if (s6DisplayTimer != null && s6DisplayTimer.isRunning()) {
            s6DisplayTimer.stop();
        }
        if (gameOverTransitionTimer != null && gameOverTransitionTimer.isRunning()) {
            gameOverTransitionTimer.stop();
        }
        isS6Playing = false;
    }

    // Handle end of game
    private void handleGameOver(JPanel main) {
        // If we're already handling S6, don't do anything
        if (isS6Playing) {
            return;
        }
        
        // Check if this is a loss with 0 attempts (S6 case)
        if (game.getRemainingAttempts() <= 0 && !game.isWordGuessed()) {
            // This will trigger S6 display
            updateUI(); // This will call handleS6Sound() through updateUI
            return;
        }
        
        // For win cases or other game over scenarios
        disableKeyboard();
        
        // Stop any playing hangman sounds
        if (currentHangmanSound != null) {
            soundManager.stopSound(currentHangmanSound);
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;
        
        if (game.isWordGuessed()) {
            mainFrame.showScreen(MainFrame.WIN);
        }
        // Note: Loss with remaining attempts doesn't happen since we handle S6 above
    }

    // Disable all buttons
    private void disableKeyboard() {
        for (Component c : keyboardPanel.getComponents()) {
            c.setEnabled(false);
        }
    }

    // Restart Game
    private void restartGame(JPanel main) {
        // Stop all sounds and timers before restarting
        soundManager.stopAllSounds();
        stopS6Timers();
        gameOverTriggered = false;
        isS6Playing = false;
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame)
            ((MainFrame) frame).restartGame();
    }

    // Go to home
    private void exitToHome(JPanel main) {
        // Stop all sounds and timers when exiting to home
        soundManager.stopAllSounds();
        stopS6Timers();
        gameOverTriggered = false;
        isS6Playing = false;
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame)
            ((MainFrame) frame).showScreen(MainFrame.HOME);
    }
}
