import java.awt.*;
import javax.swing.*;

public class About {

    public JPanel createAbout() {

        ImagePanel background = new ImagePanel("images/MainBg.png");
        background.setLayout(new GridBagLayout());

        // Pop-up
        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(420, 260));
        popup.setBackground(new Color(230, 230, 230));
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(120, 150, 255));

        JLabel title = new JLabel("WARNING");
        title.setForeground(new Color(139, 0, 0));
        title.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));

        // Logic pag gin pindot an X
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

        JTextArea message = new JTextArea(
                "HELP!!!\nTHE OPERATING SYSTEM IS UNDER ATTACK\n" +
                        "AND THE VIRUS CAN ONLY BE DEFEATED\n\n" +
                        "BY SUCCESSFULLY DECRYPTING THE KEY WORD.\n" +
                        "YOU ONLY HAVE 6 TRIES\nGOODLUCK!!!\n\n");
        message.setEditable(false);
        message.setFont(new Font("Monospaced", Font.BOLD, 12));
        message.setForeground(Color.RED);
        message.setBackground(Color.BLACK);
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.add(message, BorderLayout.CENTER);

        // Buttons
        // ************************************************************************************************************
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.setBackground(new Color(230, 230, 230));

        JButton homeButton = new JButton("HOME");
        homeButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        Dimension btnSize = new Dimension(120, 30);
        homeButton.setPreferredSize(btnSize);
        exitButton.setPreferredSize(btnSize);

        homeButton.setFocusable(false);
        exitButton.setFocusable(false);

        buttonPanel.add(homeButton);
        buttonPanel.add(exitButton);

        // ************************************************************************************************************

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
