import java.awt.*;
import javax.swing.*;

public class LevelTransition {

    private JPanel panel;
    private JLabel textLabel;
    private Timer dotTimer;
    
    public JPanel createTransitionPanel() {

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        
        textLabel = new JLabel("GENERATING LEVEL");
        textLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        textLabel.setForeground(Color.RED);
        
        panel.add(textLabel);
        
        return panel;
    }
    
    // Animation of '...'
    public void startAnimation() {
        if (dotTimer != null && dotTimer.isRunning()) return;
        
        dotTimer = new Timer(500, e -> {
            String text = textLabel.getText();
            if (text.endsWith("...")) {
                textLabel.setText("GENERATING LEVEL");
            } else {
                textLabel.setText(text + ".");
            }
        });
        dotTimer.start();
    }
    
    public void stopAnimation() {
        if (dotTimer != null) {
            dotTimer.stop();
        }
    }
}
