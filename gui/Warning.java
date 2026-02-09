import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Warning {

    JButton close;
    Random rand = new Random();

    public JPanel createWarning() {

        ImagePanel openingPanel = new ImagePanel("images/image1.png");
        openingPanel.setLayout(new GridBagLayout());

        JPanel popup = new JPanel(new BorderLayout());
        popup.setPreferredSize(new Dimension(420, 260));
        popup.setBackground(new Color(230, 230, 230));
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // ---- TITLE BAR ----
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(120, 150, 255));

        JLabel title = new JLabel("HELP!");
        title.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 5));
        title.setFont(new Font("Monospaced", Font.BOLD, 16));
        title.setForeground(new Color(139, 0, 0));

        close = new JButton("X");
        close.setFocusable(false);
        close.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(close);
            if (frame instanceof MainFrame) {
                ((MainFrame) frame).showScreen(MainFrame.HOME);
            }
        });

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(close, BorderLayout.EAST);

        // ---- MESSAGE ----
        JTextArea text = new JTextArea(
                "HELP!\nThe Operating System is under attack...\n\nGood Luck!");
        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.BOLD, 12));
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text.setForeground(Color.RED);
        text.setBackground(Color.BLACK);
        text.setForeground(Color.RED);

        popup.add(titleBar, BorderLayout.NORTH);
        popup.add(text, BorderLayout.CENTER);

        // ---- CENTER POPUP ----
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        openingPanel.add(popup, gbc);
        return openingPanel;
    }
}
