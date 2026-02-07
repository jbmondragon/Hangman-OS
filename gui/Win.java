import java.awt.*;
import javax.swing.*;

public class Win {

    public JPanel createWin() {

        // Background panel with image
        ImagePanel background = new ImagePanel("images/image1.png");
        background.setLayout(new GridBagLayout());

        // ---- POPUP PANEL ----
        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(420, 260));
        popup.setBackground(new Color(230, 230, 230));
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // ---- TITLE BAR ----
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(60, 180, 90)); // green = success

        JLabel title = new JLabel("SYSTEM SECURED");
        title.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));

        JButton close = new JButton("X");
        close.setFocusable(false);
        close.setMargin(new Insets(2, 8, 2, 8));
        close.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        // ---- MESSAGE ----
        JTextArea message = new JTextArea(
                "ACCESS GRANTED\n\n" +
                        "You successfully\n" +
                        "eliminated the virus.\n\n" +
                        "THE OPERATING SYSTEM\n" +
                        "IS NOW SAFE.");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.BOLD, 12));
        message.setBackground(new Color(245, 245, 245));
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.add(message, BorderLayout.CENTER);

        // ---- BUTTONS ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.setBackground(new Color(230, 230, 230));

        JButton homeButton = new JButton("HOME");
        homeButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(homeButton);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        JButton playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.addActionListener(e -> {
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

        // ---- ASSEMBLE POPUP ----
        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(messagePanel, BorderLayout.CENTER);
        popup.add(buttonPanel, BorderLayout.SOUTH);

        // ---- CENTER POPUP ----
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
