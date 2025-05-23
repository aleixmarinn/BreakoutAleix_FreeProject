package Componentes;

import javax.swing.*;
import java.awt.*;

public class Bloque extends JLabel {
    public Bloque(int x, int y, Color color) {
        setBounds(x, y, 60, 20);
        setOpaque(true);
        setBackground(color);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
