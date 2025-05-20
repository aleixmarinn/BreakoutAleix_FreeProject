import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Paleta extends JLabel {
    private final int velocidad = 20;

    public Paleta() {
        setBounds(220, 500, 100, 10);
        setOpaque(true);
        setBackground(new Color(244, 67, 54));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    public void mover(int keyCode, int anchoPanel) {
        int x = getX();
        if (keyCode == KeyEvent.VK_LEFT && x > 0) {
            setLocation(x - velocidad, getY());
        } else if (keyCode == KeyEvent.VK_RIGHT && x + getWidth() < anchoPanel) {
            setLocation(x + velocidad, getY());
        }
    }
}
