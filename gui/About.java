import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class About {

    private SoundManager soundManager;
    private boolean soundPlayed = false;
    private final Random rand = new Random();
    JButton close;

    public JPanel createAbout() {
        soundManager = SoundManager.getInstance();

        ImagePanel background = new ImagePanel("/images/MainBg.png");
        background.setLayout(new GridBagLayout());

        background.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (!soundPlayed) {
                    soundManager.playSound(SoundManager.ABOUT_PAGE);
                    soundPlayed = true;
                }
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                soundPlayed = false;
            }
        });

        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(460, 300));
        popup.setBackground(Color.BLACK);
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 60, 60), 3),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);

        JLabel title = new JLabel("GUIDE TO SAVE YOUR OS!");
        title.setForeground(new Color(255, 60, 60));
        title.setFont(new Font("Monospaced", Font.BOLD, 18));

        close = new JButton("✕");
        styleCyberButton(close, new Color(255, 60, 60));
        close.addActionListener(e -> {
            soundManager.playSound(SoundManager.KEYBOARD);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        JTextArea message = new JTextArea(
                "> THE OPERATING SYSTEM IS BEING CONTAMINATED \n" +
                        "> BY SOME UNKNOWN VIRUS\n" +
                        "> AND THE VIRUS CAN ONLY BE DEFEATED\n\n" +
                        "> BY SUCCESSFULLY DECRYPTING THE KEY WORD.\n" +
                        "> YOU ONLY HAVE 6 TRIES\nGOODLUCK!!!");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.PLAIN, 15));
        message.setBackground(Color.BLACK);
        message.setForeground(new Color(255, 60, 60));
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setBorder(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        JButton homeButton = new JButton("HOME");
        JButton exitButton = new JButton("EXIT");
        styleCyberButton(homeButton, new Color(255, 60, 60));
        styleCyberButton(exitButton, new Color(255, 60, 60));

        homeButton.addActionListener(e -> {
            soundManager.playSound(SoundManager.KEYBOARD);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        exitButton.addActionListener(e -> {
            soundManager.playSound(SoundManager.KEYBOARD);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                System.exit(0);
            }
        });

        buttonPanel.add(homeButton);
        buttonPanel.add(exitButton);

        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(message, BorderLayout.CENTER);
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
            Color flickerColor = new Color(255, 60 + rand.nextInt(60), 60);
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
        button.setPreferredSize(new Dimension(120, 35));

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
