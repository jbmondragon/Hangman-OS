import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Win {

    private SoundManager soundManager;
    private final Random rand = new Random();
    JButton close;
    private boolean soundPlayed = false; // Add flag

    public JPanel createWin() {
        soundManager = SoundManager.getInstance();

        // bg
        ImagePanel background = new ImagePanel("/images/MainBg.png");
        background.setLayout(new GridBagLayout());

        // sound
        background.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                soundManager.stopAllSounds();

                Timer delayTimer = new Timer(300, ev -> {
                    if (!soundPlayed) {
                        soundManager.playSound(SoundManager.WIN);
                        soundPlayed = true;
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                soundPlayed = false;
            }
        });

        // pop-up
        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(460, 300));
        popup.setBackground(Color.BLACK);

        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 255, 120), 2),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        // title
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);

        JLabel title = new JLabel("SYSTEM SECURED");
        title.setForeground(new Color(0, 255, 120));
        title.setFont(new Font("Monospaced", Font.BOLD, 18));

        close = new JButton("✕");
        styleCyberButton(close);
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

        // msg area
        JTextArea message = new JTextArea(
                "> ACCESS GRANTED\n\n" +
                        "> You successfully eliminated the virus.\n\n" +
                        "> THE OPERATING SYSTEM IS NOW SAFE.");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.PLAIN, 15));
        message.setBackground(Color.BLACK);
        message.setForeground(new Color(0, 255, 120));
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
        styleCyberButton(homeButton);
        styleCyberButton(playAgainButton);

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

        // flicker effect
        Timer flickerTimer = new Timer(120, e -> {
            int glow = 200 + rand.nextInt(55);
            Color flickerColor = new Color(0, glow, 120);
            title.setForeground(flickerColor);
            message.setForeground(flickerColor);
        });
        flickerTimer.start();

        return background;
    }

    // styles
    private void styleCyberButton(JButton button) {
        button.setFocusPainted(false);
        button.setForeground(new Color(0, 255, 120));
        button.setBackground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 120)));
        button.setFont(new Font("Monospaced", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 255, 120));
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
                button.setForeground(new Color(0, 255, 120));
            }
        });
    }
}
