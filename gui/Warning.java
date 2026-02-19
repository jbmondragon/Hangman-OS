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

        ImagePanel openingPanel = new ImagePanel("images/MainBg.png");
        openingPanel.setLayout(new GridBagLayout());

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

        // ... (Rest of UI code) ...
        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(420, 260));
        popup.setBackground(new Color(230, 230, 230));
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(120, 150, 255));

        JLabel title = new JLabel("HELP!");
        title.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));
        title.setFont(new Font("Monospaced", Font.BOLD, 16));
        title.setForeground(new Color(139, 0, 0));

        close = new JButton("X");
        close.setFocusable(false);
        close.addActionListener(e -> {
            soundManager.playSound(SoundManager.KEYBOARD); // Sound
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        JTextArea text = new JTextArea(
                "HELP!\nThe Operating System is under attack...\n\nGood Luck!");
        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.BOLD, 12));
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text.setForeground(Color.RED);
        text.setBackground(Color.BLACK);

        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(text, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; gbc.weighty = 1.0;

        openingPanel.add(popup, gbc);
        return openingPanel;
    }
}