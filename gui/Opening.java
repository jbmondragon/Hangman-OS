import javax.swing.*;

public class Opening {

    private JPanel openingPanel;

    public JPanel createOpening() {
        openingPanel = new ImagePanel("/Images/MainBg.png");
        return openingPanel;
    }
}
