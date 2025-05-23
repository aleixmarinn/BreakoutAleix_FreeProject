package Componentes;

import javax.swing.*;
import java.awt.*;

public class Pelota extends JLabel {
    private double dx = 2, dy = -2;

    public Pelota() {
        setBounds(240, 480, 15, 15);
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }

    public void mover() {
        setLocation((int) (getX() + dx), (int) (getY() + dy));
        if (getX() <= 0 || getX() + getWidth() >= getParent().getWidth()) dx *= -1;
        if (getY() <= 0) dy *= -1;
    }

    public boolean colisionaCon(JLabel otro) {
        return getBounds().intersects(otro.getBounds());
    }

    public void reboteVertical() {
        dy *= -1;
    }

    public boolean estaFuera(int alturaPanel) {
        return getY() > alturaPanel;
    }

    public void aumentarVelocidad() {
        dx *= 1.5;
        dy *= 1.5;
    }
}
