package Util;

import Componentes.Bloque;
import Componentes.Paleta;
import Componentes.Pelota;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GestorJuego extends JPanel {
    private Pelota pelota;
    private Paleta paleta;
    private ArrayList<Bloque> bloques;
    private JLabel labelPuntuacion, labelJugador;
    private Timer timer, acelerador;
    private int puntuacion = 0;
    private String nombreJugador;
    private GestorBD gestorBD;


    public GestorJuego(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        this.gestorBD = new GestorBD();

        setLayout(null);
        setBounds(0, 0, 500, 600);
        setOpaque(false);
        setFocusable(true);

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
        crearNombreJugador();

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

    private void crearNombreJugador() {
        labelJugador = new JLabel("Jugador: " + nombreJugador);
        labelJugador.setForeground(Color.white);
        labelJugador.setBounds(20, 500, 200, 30);
        add(labelJugador);
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

        gestorBD.guardarPartida(nombreJugador, puntuacion);

        int opcion = JOptionPane.showConfirmDialog(this,
                victoria ?
                        "¡Felicidades, " + nombreJugador + "! Has ganado con " + puntuacion + " puntos. ¿Jugar otra vez?" :
                        "¡Ánimo, " + nombreJugador + "! Has perdido. ¿Intentarlo de nuevo?",
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
