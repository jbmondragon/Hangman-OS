import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Home {

    JButton close;
    Random rand = new Random();

    public JPanel createHome() {

        ImagePanel background = new ImagePanel("images/MainBg.png");
        background.setLayout(new GridBagLayout());

        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(460, 280));
        popup.setBackground(Color.BLACK);
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 60, 60), 3),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);

        JLabel title = new JLabel("SYSTEM ALERT!");
        title.setFont(new Font("Monospaced", Font.BOLD, 18));
        title.setForeground(new Color(255, 60, 60));

        close = new JButton("✕");
        styleCyberButton(close, new Color(255, 60, 60));
        close.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.WARNING);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        String fullMessage = "> Intrusion detected...\n\n" +
                "> Your OS is in grave danger.\n" +
                "> You might lose what's important to you!\n" +
                "> Press PLAY to save the SYSTEM!\n" +
                "> Note: Kill the virus or be compromised!";

        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.BOLD, 15));
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text.setForeground(new Color(255, 60, 60));
        text.setBackground(Color.BLACK);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        JButton playButton = new JButton("PLAY");
        JButton aboutButton = new JButton("ABOUT");
        styleCyberButton(playButton, new Color(255, 60, 60));
        styleCyberButton(aboutButton, new Color(255, 60, 60));

        playButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).restartGame();
            }
        });

        aboutButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.ABOUT);
            }
        });

        buttonPanel.add(playButton);
        buttonPanel.add(aboutButton);

        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(text, BorderLayout.CENTER);
        popup.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        background.add(popup, gbc);

        Timer typingTimer = new Timer(50, null);
        final int[] index = { 0 };
        typingTimer.addActionListener(e -> {
            if (index[0] < fullMessage.length()) {
                text.append(String.valueOf(fullMessage.charAt(index[0])));
                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        typingTimer.start();

        Timer flickerTimer = new Timer(100, e -> {
            int glow = 180 + rand.nextInt(75);
            Color flickerColor = new Color(255, 60 + rand.nextInt(60), 60);
            title.setForeground(flickerColor);
            text.setForeground(flickerColor);
        });
        flickerTimer.start();

        return background;
    }

    private void styleCyberButton(JButton button, Color redColor) {
        button.setFocusPainted(false);
        button.setForeground(redColor);
        button.setBackground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(redColor, 2));
        button.setFont(new Font("Monospaced", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(redColor);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
                button.setForeground(redColor);
            }
        });
    }
}
