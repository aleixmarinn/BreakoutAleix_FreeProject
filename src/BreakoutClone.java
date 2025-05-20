import javax.swing.*;

public class BreakoutClone extends JFrame {

    public BreakoutClone() {
        setTitle("Breakout Clone - Version Españita");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ✅ Fondo con imagen
        JLabel fondo = new JLabel(new ImageIcon("src/Images/dragonite.jpeg"));
        fondo.setLayout(null); // Muy importante para que funcione setBounds()
        setContentPane(fondo); // Establece fondo como contentPane



        // Panel del juego
        // ✅ Panel del juego encima del fondo
        GestorJuego juego = new GestorJuego(); // Ya no le pasamos this
        fondo.add(juego); // Añadimos el juego al fondo, NO lo seteamos como contentPane
        setContentPane(fondo);
        addKeyListener(juego.getKeyAdapter());
        setFocusable(true);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BreakoutClone::new);
    }
}
