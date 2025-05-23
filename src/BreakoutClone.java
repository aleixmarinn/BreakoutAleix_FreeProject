import Util.GestorJuego;

import javax.swing.*;

public class BreakoutClone extends JFrame {

    public BreakoutClone() {
        setTitle("Breakout Clone - Versión Españita");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mostrarPantallaInicio();

        setVisible(true);
    }

    private void mostrarPantallaInicio() {
        PantallaInicio pantalla = new PantallaInicio(this);
        setContentPane(pantalla);
        setFocusable(true);
        pantalla.requestFocusInWindow();
        revalidate();
        repaint();
    }

    public void iniciarJuego(String nombreJugador) {

        JLabel fondo = new JLabel(new ImageIcon("src/Images/dragonite.jpeg"));
        fondo.setLayout(null);


        GestorJuego juego = new GestorJuego(nombreJugador);
        fondo.add(juego);


        setContentPane(fondo);


        juego.setFocusable(true);
        juego.requestFocusInWindow();

        juego.addKeyListener(juego.getKeyAdapter());

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BreakoutClone::new);
    }
}
