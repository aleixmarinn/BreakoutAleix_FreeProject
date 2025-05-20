import javax.swing.*;

public class BreakoutClone extends JFrame {

    public BreakoutClone() {
        setTitle("Breakout Clone - Version Españita");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // No redimensionar la ventana

        // Fondo
        JLabel fondo = new JLabel(new ImageIcon("src/Images/dragonite.jpeg"));
        fondo.setLayout(null);
        setContentPane(fondo);

        // Panel del juego
        GestorJuego juego = new GestorJuego();
        fondo.add(juego);
        setContentPane(fondo);
        addKeyListener(juego.getKeyAdapter());
        setFocusable(true);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BreakoutClone::new);
    }
}
