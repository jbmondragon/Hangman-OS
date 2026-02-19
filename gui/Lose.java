import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Lose {

    private SoundManager soundManager;
    private final Random rand = new Random();
    JButton close;

    public JPanel createLose() {
        soundManager = SoundManager.getInstance();

        ImagePanel background = new ImagePanel("images/GameOverBg.png");
        background.setLayout(new GridBagLayout());

        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(460, 300));
        popup.setBackground(Color.BLACK);

        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 60, 60), 2),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);

        JLabel title = new JLabel("SYSTEM FAILURE");
        title.setForeground(new Color(255, 60, 60));
        title.setFont(new Font("Monospaced", Font.BOLD, 18));

        close = new JButton("✕");
        styleCyberButton(close, new Color(255, 60, 60));
        close.addActionListener(e -> {
            soundManager.stopAllSounds();
            soundManager.playSound(SoundManager.KEYBOARD);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        JTextArea message = new JTextArea(
                "> CRITICAL ERROR!\n\n" +
                        "> The virus has infected the system.\n\n" +
                        "> OPERATING SYSTEM HAS BEEN COMPROMISED.");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.PLAIN, 15));
        message.setBackground(Color.BLACK);
        message.setForeground(new Color(255, 60, 60));
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.add(message, BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        JButton homeButton = new JButton("HOME");
        JButton playAgainButton = new JButton("PLAY AGAIN");
        styleCyberButton(homeButton, new Color(255, 60, 60));
        styleCyberButton(playAgainButton, new Color(255, 60, 60));

        homeButton.addActionListener(e -> {
            soundManager.stopAllSounds();
            soundManager.playSound(SoundManager.KEYBOARD);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(homeButton);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        playAgainButton.addActionListener(e -> {
            soundManager.stopAllSounds();
            soundManager.playSound(SoundManager.KEYBOARD);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(playAgainButton);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).restartGame();
            }
        });

        buttonPanel.add(homeButton);
        buttonPanel.add(playAgainButton);

        // pop-up
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

        Timer flickerTimer = new Timer(120, e -> {
            int glow = 180 + rand.nextInt(75);
            Color flickerColor = new Color(255, 60 + rand.nextInt(100), 60);
            title.setForeground(flickerColor);
            message.setForeground(flickerColor);
        });
        flickerTimer.start();

        return background;
    }

    private void styleCyberButton(JButton button, Color neonColor) {
        button.setFocusPainted(false);
        button.setForeground(neonColor);
        button.setBackground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(neonColor, 2));
        button.setFont(new Font("Monospaced", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(neonColor);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
                button.setForeground(neonColor);
            }
        });
    }
}
