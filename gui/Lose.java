import java.awt.*;
import javax.swing.*;

public class Lose {

    private SoundManager soundManager;
    private boolean soundPlayed = false;

    public JPanel createLose() {
        soundManager = SoundManager.getInstance();
        
        ImagePanel background = new ImagePanel("images/GameOverBg.png");
        background.setLayout(new GridBagLayout());

        // Add component listener to play sound only when panel is shown
        background.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (!soundPlayed) {
                    soundManager.playSound(SoundManager.LOSE);
                    soundPlayed = true;
                }
            }
            
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                // Reset flag when hidden so it plays again if shown later
                soundPlayed = false;
            }
        });

        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(420, 260));
        popup.setBackground(new Color(230, 230, 230));
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(180, 60, 60));

        JLabel title = new JLabel("SYSTEM FAILURE");
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));

        JButton close = new JButton("X");
        close.setFocusable(false);
        close.setMargin(new Insets(2, 8, 2, 8));
        close.addActionListener(e -> {
            soundManager.stopAllSounds();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        JTextArea message = new JTextArea(
                "CRITICAL ERROR!\n\n" +
                        "The virus has\n" +
                        "infected the system.\n\n" +
                        "OPERATING SYSTEM\n" +
                        "HAS BEEN COMPROMISED.");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.BOLD, 12));
        message.setBackground(new Color(245, 245, 245));
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.setBackground(new Color(230, 230, 230));

        JButton homeButton = new JButton("HOME");
        homeButton.addActionListener(e -> {
            soundManager.stopAllSounds();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(homeButton);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        JButton playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.addActionListener(e -> {
            soundManager.stopAllSounds();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(playAgainButton);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).restartGame();
            }
        });

        Dimension btnSize = new Dimension(120, 30);
        homeButton.setPreferredSize(btnSize);
        playAgainButton.setPreferredSize(btnSize);

        homeButton.setFocusable(false);
        playAgainButton.setFocusable(false);

        buttonPanel.add(homeButton);
        buttonPanel.add(playAgainButton);

        // Pag assemble sa pop-up
        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(messagePanel, BorderLayout.CENTER);
        popup.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        background.add(popup, gbc);

        return background;
    }
}
