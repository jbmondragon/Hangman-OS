import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Warning {

    JButton close;
    Random rand = new Random();
    private SoundManager soundManager;
    private boolean soundPlayed = false;

    public JPanel createWarning() {
        soundManager = SoundManager.getInstance();

        // bg
        ImagePanel openingPanel = new ImagePanel("/images/MainBg.png");
        openingPanel.setLayout(new GridBagLayout());

        // sound
        openingPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
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

        // pop-up
        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(460, 280));
        popup.setBackground(Color.BLACK);
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 60, 60), 3),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        // title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);

        JLabel title = new JLabel("URGENT ALERT!");
        title.setFont(new Font("Monospaced", Font.BOLD, 18));
        title.setForeground(new Color(255, 60, 60));

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

        // msg area
        String fullMessage = "> WARNING!!!\n" +
                "> The Operating System is under attack!\n\n" +
                "> Save your OS immediately!\n";

        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.BOLD, 15));
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text.setForeground(new Color(255, 60, 60));
        text.setBackground(Color.BLACK);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        // pop-up assemble
        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(text, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        openingPanel.add(popup, gbc);

        // load effect
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

        // flicker
        Timer flickerTimer = new Timer(100, e -> {
            int glow = 180 + rand.nextInt(75);
            Color flickerColor = new Color(255, 60 + rand.nextInt(60), 60);
            title.setForeground(flickerColor);
            text.setForeground(flickerColor);
        });
        flickerTimer.start();

        return openingPanel;
    }

    // styles
    private void styleCyberButton(JButton button, Color redColor) {
        button.setFocusPainted(false);
        button.setForeground(Color.red);
        button.setBackground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        button.setFont(new Font("Monospaced", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(40, 30));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.red);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
                button.setForeground(Color.red);
            }
        });
    }
}
