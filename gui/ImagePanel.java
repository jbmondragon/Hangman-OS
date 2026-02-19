import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private Image mainImage;
    private Image patternImage;

    public ImagePanel(String resourcePath) {
        this(resourcePath, true);
    }

    public ImagePanel(String resourcePath, boolean usePattern) {
        this.setBackground(Color.black);

        loadMainImage(resourcePath);

        if (usePattern) {
            java.net.URL patternUrl = getClass().getResource("/images/bar.png");
            if (patternUrl == null)
                patternUrl = getClass().getResource("images/bar.png");

            if (patternUrl != null) {
                patternImage = new ImageIcon(patternUrl).getImage();
            }
        }
    }

    // Load image
    private void loadMainImage(String resourcePath) {
        if (getClass().getResource("/" + resourcePath) != null) {
            mainImage = new ImageIcon(getClass().getResource("/" + resourcePath)).getImage();
        } else if (getClass().getResource(resourcePath) != null) {
            mainImage = new ImageIcon(getClass().getResource(resourcePath)).getImage();
        } else {
            System.err.println("Error: Could not find main image: " + resourcePath);
        }
    }

    // Public method to change the background dynamically
    public void updateBackgroundImage(String resourcePath) {
        loadMainImage(resourcePath);
        repaint();
    }

    public void updatePatternImage(String resourcePath) {
        java.net.URL patternUrl = getClass().getResource("/" + resourcePath);
        if (patternUrl == null)
            patternUrl = getClass().getResource(resourcePath);

        if (patternUrl != null) {
            patternImage = new ImageIcon(patternUrl).getImage();
            repaint();
        } else {
            System.err.println("Error: Could not find pattern image: " + resourcePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelW = getWidth();
        int panelH = getHeight();

        int imgX = 0, imgY = 0, imgW = 0, imgH = 0;

        if (mainImage != null) {
            int rawW = mainImage.getWidth(this);
            int rawH = mainImage.getHeight(this);
            if (rawW > 0 && rawH > 0) {

                double scale = Math.min((double) panelW / rawW, (double) panelH / rawH);
                imgW = (int) (rawW * scale);
                imgH = (int) (rawH * scale);
                imgX = (panelW - imgW) / 2;
                imgY = (panelH - imgH) / 2;
            }
        }

        if (patternImage != null && patternImage.getWidth(this) > 0) {

            // Top Bar
            if (imgY > 0) {
                g.drawImage(patternImage, 0, 0, panelW, imgY, this);
            }

            // Bottom Bar
            if (imgY + imgH < panelH) {
                g.drawImage(patternImage, 0, imgY + imgH, panelW, panelH - (imgY + imgH), this);
            }

            // Left Bar
            if (imgX > 0) {
                g.drawImage(patternImage, 0, imgY, imgX, imgH, this);
            }

            // Right Bar
            if (imgX + imgW < panelW) {
                g.drawImage(patternImage, imgX + imgW, imgY, panelW - (imgX + imgW), imgH, this);
            }
        }
        if (mainImage != null && imgW > 0 && imgH > 0) {
            g.drawImage(mainImage, imgX, imgY, imgW, imgH, this);
        }
    }
}