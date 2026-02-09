import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Home {
    JButton close;
    Random rand = new Random();

    public JPanel createHome() {
        // Background-image
        ImagePanel background = new ImagePanel("images/MainBg.png");
        background.setLayout(new GridBagLayout());

        // Pop-up
        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(420, 260));
        popup.setBackground(new Color(230, 230, 230));
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Title bar hit pop-up
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(120, 150, 255));

        JLabel title = new JLabel("WARNING");
        title.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));
        title.setForeground(Color.RED);
        title.setFont(new Font("Monospaced", Font.BOLD, 16));

        close = new JButton("X");
        close.setFocusable(false);
        close.setMargin(new Insets(2, 8, 2, 8));
        close.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.WARNING);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        // Warning Message
        JTextArea message = new JTextArea(
                "!!! SYSTEM BREACH DETECTED !!!\n" +
                        "ALL FILES AT RISK!\n\n" +
                        "CLICK PLAY TO SECURE OS.");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.BOLD, 14));
        message.setBackground(new Color(0, 0, 0));
        message.setForeground(Color.GREEN);
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.BLACK);
        messagePanel.add(message, BorderLayout.CENTER);

        // Play and About Buttons
        // ************************************************************************************************
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.setBackground(new Color(230, 230, 230));

        JButton playButton = new JButton("PLAY GAME");
        playButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).restartGame();
            }
        });

        JButton aboutButton = new JButton("ABOUT GAME");
        aboutButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.ABOUT);
            }
        });

        Dimension btnSize = new Dimension(120, 30);
        playButton.setPreferredSize(btnSize);
        aboutButton.setPreferredSize(btnSize);

        playButton.setFocusable(false);
        aboutButton.setFocusable(false);

        buttonPanel.add(playButton);
        buttonPanel.add(aboutButton);
        // ************************************************************************************************

        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(messagePanel, BorderLayout.CENTER);
        popup.add(buttonPanel, BorderLayout.SOUTH);

        // Pag center sa pop-up
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        background.add(popup, gbc);

        // May pa virus effect
        Timer flickerTimer = new Timer(100, e -> {

            int rTitle = 150 + rand.nextInt(106);
            int gTitle = rand.nextInt(50);
            int bTitle = rand.nextInt(50);
            title.setForeground(new Color(rTitle, gTitle, bTitle));

            int rMsg = 150 + rand.nextInt(106);
            int gMsg = rand.nextInt(50);
            int bMsg = rand.nextInt(50);
            message.setForeground(new Color(rMsg, gMsg, bMsg));

            popup.setLocation(popup.getX() + rand.nextInt(3) - 1, popup.getY() + rand.nextInt(3) - 1);
        });

        flickerTimer.start();
        return background;
    }
}
