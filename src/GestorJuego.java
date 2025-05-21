import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GestorJuego extends JPanel {
    private Pelota pelota;
    private Paleta paleta;
    private ArrayList<Bloque> bloques;
    private JLabel labelPuntuacion;
    private Timer timer, acelerador;
    private int puntuacion = 0;

    public GestorJuego() {
        setLayout(null);
        setBounds(0, 0, 500, 600);
        setOpaque(false);
        iniciarJuego();
    }

    public void iniciarJuego() {
        removeAll();
        bloques = new ArrayList<>();
        puntuacion = 0;

        setLayout(null);

        paleta = new Paleta();
        add(paleta);

        pelota = new Pelota();
        add(pelota);

        crearBloques();
        crearPuntuacion();

        timer = new Timer(10, e -> moverPelota());
        acelerador = new Timer(5000, e -> pelota.aumentarVelocidad());

        timer.start();
        acelerador.start();

        revalidate();
        repaint();
    }

    private void crearBloques() {
        Color[] colores = {new Color(244, 67, 54), new Color(255, 193, 7)};
        int blockWidth = 60, blockHeight = 20, padding = 5;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7; col++) {
                Bloque bloque = new Bloque(10 + col * (blockWidth + padding), 30 + row * (blockHeight + padding), colores[row % 2]);
                bloques.add(bloque);
                add(bloque);
            }
        }
    }

    private void crearPuntuacion() {
        labelPuntuacion = new JLabel("Puntos: 0");
        labelPuntuacion.setForeground(Color.white);
        labelPuntuacion.setBounds(380, 500, 100, 30);
        add(labelPuntuacion);
    }

    public void actualizarPuntuacion(int puntosGanados) {
        puntuacion += puntosGanados;
        labelPuntuacion.setText("Puntos: " + puntuacion);
    }

    private void moverPelota() {
        pelota.mover();

        if (pelota.colisionaCon(paleta)) {
            pelota.reboteVertical();
        }

        for (Bloque bloque : bloques) {
            if (bloque.isVisible() && pelota.colisionaCon(bloque)) {
                bloque.setVisible(false);
                pelota.reboteVertical();
                actualizarPuntuacion(100);
                break;
            }
        }

        if (pelota.estaFuera(getHeight())) {
            finDelJuego(false);
        } else if (bloques.stream().noneMatch(Bloque::isVisible)) {
            finDelJuego(true);
        }
    }

    private void finDelJuego(boolean victoria) {
        timer.stop();
        acelerador.stop();

        int opcion = JOptionPane.showConfirmDialog(this,
                victoria ? "¡Ganaste con " + puntuacion + " puntos! ¿Jugar otra vez?" : "¡Has perdido! ¿Jugar de nuevo?",
                "Fin del juego", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            iniciarJuego();
        } else {
            System.exit(0);
        }
    }

    public KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                paleta.mover(e.getKeyCode(), getWidth());
            }
        };
    }
}
