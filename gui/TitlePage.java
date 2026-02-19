import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class TitlePage extends JPanel {

    private final String chars = "010101010101010101001";
    private final Random random = new Random();
    private int[] drops;
    private Timer timer;
    private boolean flicker = true;
    private String overview = "Welcome to HackedMan. \nDefend your system\nor lose everything.";
    private String displayedOverview = "";
    private int overviewIndex = 0;
    private Timer typingTimer;
    
    // Add SoundManager and flag
    private SoundManager soundManager;
    private boolean soundPlayed = false;

    public TitlePage() {
        setBackground(Color.BLACK);
        
        // Initialize SoundManager
        soundManager = SoundManager.getInstance();

        timer = new Timer(50, e -> {
            flicker = !flicker;
            repaint();
        });
        timer.start();

        typingTimer = new Timer(100, e -> {
            if (overviewIndex < overview.length()) {
                displayedOverview += overview.charAt(overviewIndex);
                overviewIndex++;
                repaint();
            } else {
                typingTimer.stop();
            }
        });
        typingTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        if (drops == null) {
            drops = new int[width / 15];
            for (int i = 0; i < drops.length; i++) {
                drops[i] = random.nextInt(height);
            }
        }

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.GREEN);
        g2.setFont(new Font("Monospaced", Font.BOLD, 15));
        for (int i = 0; i < drops.length; i++) {
            String text = String.valueOf(chars.charAt(random.nextInt(chars.length())));
            int x = i * 15;
            int y = drops[i] * 15;
            g2.drawString(text, x, y);

            if (y > height && random.nextDouble() > 0.975) {
                drops[i] = 0;
            }
            drops[i]++;
        }

        for (int i = 0; i < 8; i++) {
            g2.setColor(new Color(0, 255, 0, 40));
            int glitchY = random.nextInt(height);
            g2.fillRect(0, glitchY, width, 2);
        }

        g2.setFont(new Font("Monospaced", Font.BOLD, 50));
        g2.setColor(flicker ? Color.GREEN : new Color(0, 255, 0, 150));
        String title = "HackedMan";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        g2.drawString(title, (width - textWidth) / 2, height / 2);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        String[] lines = displayedOverview.split("\n");
        for (int i = 0; i < lines.length; i++) {
            FontMetrics fm2 = g2.getFontMetrics();
            int lineWidth = fm2.stringWidth(lines[i]);
            g2.drawString(lines[i], (width - lineWidth) / 2, height / 2 + 60 + i * 25);
        }
    }

    public JPanel createTitlePanel() {
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.add(this, BorderLayout.CENTER);
        
        // Add component listener to play sound when panel is shown
        outerWrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (!soundPlayed) {
                    // Small delay to ensure everything is loaded
                    Timer delayTimer = new Timer(500, ev -> {
                        soundManager.playSound(SoundManager.TITLE_PAGE);
                        soundPlayed = true;
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                }
            }
            
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                // Don't reset soundPlayed flag so it only plays once per game session
                // This ensures it only plays on first launch
            }
        });
        
        return outerWrapper;
    }
}
