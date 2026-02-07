import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(String resourcePath) {
        try {
            image = new ImageIcon(
                    getClass().getResource(resourcePath)).getImage();
        } catch (Exception e) {
            System.err.println("Image not found: " + resourcePath);
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null)
            return;

        int panelW = getWidth();
        int panelH = getHeight();

        int imgW = image.getWidth(this);
        int imgH = image.getHeight(this);

        if (imgW <= 0 || imgH <= 0)
            return;

        double scale = Math.min(
                (double) panelW / imgW,
                (double) panelH / imgH);

        int drawW = (int) (imgW * scale);
        int drawH = (int) (imgH * scale);

        int x = (panelW - drawW) / 2;
        int y = (panelH - drawH) / 2;

        g.drawImage(image, x, y, drawW, drawH, this);
    }
}
