import javax.swing.*;

public class BreakoutClone extends JFrame {

    public BreakoutClone() {
        setTitle("Breakout Clone - Versión Españita");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // No redimensionar la ventana

        mostrarPantallaInicio(); // Solo mostramos la pantalla de inicio al principio

        setVisible(true); // Mostrar ventana al final del constructor
    }

    private void mostrarPantallaInicio() {
        PantallaInicio pantalla = new PantallaInicio(this);
        setContentPane(pantalla);
        setFocusable(true);
        pantalla.requestFocusInWindow();
        revalidate();
        repaint();
    }

    // Este método se llamará desde PantallaInicio cuando el jugador pulse "Jugar"
    public void iniciarJuego(String nombreJugador) {
        // ✅ Creamos fondo con imagen
        JLabel fondo = new JLabel(new ImageIcon("src/Images/dragonite.jpeg"));
        fondo.setLayout(null);

        // ✅ Creamos el panel del juego con el nombre del jugador
        GestorJuego juego = new GestorJuego(nombreJugador);
        fondo.add(juego);

        // ✅ Colocamos el fondo como contentPane
        setContentPane(fondo);

        // 🔁 Este truco garantiza que el panel tenga el foco
        juego.setFocusable(true);
        juego.requestFocusInWindow();

        // ✅ Añadimos el keyListener al panel del juego, no a la ventana
        // (porque la ventana ya no lo recibe al haber cambiado el contentPane)
        juego.addKeyListener(juego.getKeyAdapter());

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BreakoutClone::new);
    }
}
