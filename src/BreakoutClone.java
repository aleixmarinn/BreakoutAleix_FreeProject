import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BreakoutClone extends JFrame {
    private JLabel paddle, ball, labelPuntuacion;
    private ArrayList<JLabel> blocks = new ArrayList<>();
    private Timer timer, acelerador;
    private int ballDX = 2, ballDY = -2;
    private int paddleSpeed = 15;
    private int puntuacion = 0;

    public BreakoutClone() {
        setTitle("Breakout Clone - Version Españita");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        iniciarJuego();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                moverPaleta(e.getKeyCode());
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    private void iniciarJuego() {
        // Detenemos cualquier Timer anterior si existe
        if (timer != null) timer.stop();
        if (acelerador != null) acelerador.stop();

        // Eliminamos todos los componentes de la ventana
        getContentPane().removeAll();

        // Limpiamos el array de bloques
        blocks.clear();

        // Reiniciamos velocidad y puntuación
        ballDX = 2;
        ballDY = -2;
        puntuacion = 0;

        // Creamos los elementos del juego desde cero
        crearPaleta();
        crearPelota();
        crearBloques();
        crearPuntuacion();

        // Volvemos a pintar todo
        revalidate();
        repaint();

        // Iniciamos los timers
        timer = new Timer(10, e -> moverPelota());
        timer.start();

        acelerador = new Timer(5000, e -> aumentarVelocidad());
        acelerador.start();
    }

    private void crearPuntuacion() {
        labelPuntuacion = new JLabel("Puntos: 0");
        labelPuntuacion.setForeground(Color.BLACK);
        labelPuntuacion.setBounds(getWidth() - 100, getHeight() - 70, 100, 30); // esquina inferior derecha
        add(labelPuntuacion);
    }

    private void actualizarPuntuacion(int puntosGanados) {
        puntuacion += puntosGanados;
        labelPuntuacion.setText("Puntos: " + puntuacion);
    }

    private void aumentarVelocidad() {
        ballDX *= 1.5;
        ballDY *= 1.5;
        System.out.println("Nueva velocidad: " + ballDX + ", " + ballDY);
    }

    private void crearPaleta() {
        paddle = new JLabel();
        paddle.setOpaque(true);
        paddle.setBackground(new Color(244, 67, 54));
        paddle.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        paddle.setBounds(220, 500, 100, 10);
        add(paddle);
    }

    private void crearPelota() {
        ball = new JLabel();
        ball.setOpaque(true);
        ball.setBackground(new Color(0, 0, 0));
        ball.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        ball.setBounds(240, 480, 15, 15);
        add(ball);
    }

    private void crearBloques() {
        int blockWidth = 60, blockHeight = 20, padding = 5;
        Color[] filaColores = {
                new Color(244, 67, 54),
                new Color(255, 193, 7),
                new Color(255, 193, 7),
                new Color(244, 67, 54)
        };

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7; col++) {
                JLabel block = new JLabel();
                block.setOpaque(true);
                block.setBackground(filaColores[row % filaColores.length]);
                block.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                block.setBounds(10 + col * (blockWidth + padding), 30 + row * (blockHeight + padding), blockWidth, blockHeight);
                blocks.add(block);
                add(block);
            }
        }
    }

    private void moverPaleta(int keyCode) {
        int x = paddle.getX();
        if (keyCode == KeyEvent.VK_LEFT && x > 0) {
            paddle.setLocation(x - paddleSpeed, paddle.getY());
        } else if (keyCode == KeyEvent.VK_RIGHT && x + paddle.getWidth() < getWidth()) {
            paddle.setLocation(x + paddleSpeed, paddle.getY());
        }
    }

    private void moverPelota() {
        int bx = ball.getX();
        int by = ball.getY();

        if (bx <= 0 || bx + ball.getWidth() >= getWidth()) ballDX *= -1;
        if (by <= 0) ballDY *= -1;

        if (ball.getBounds().intersects(paddle.getBounds())) {
            ballDY *= -1;
        }

        for (int i = 0; i < blocks.size(); i++) {
            JLabel block = blocks.get(i);
            if (block.isVisible() && ball.getBounds().intersects(block.getBounds())) {
                block.setVisible(false);
                ballDY *= -1;
                actualizarPuntuacion(100); // 100 puntos por bloque destruido
                break;
            }
        }

        if (by > getHeight()) {
            timer.stop();
            acelerador.stop();
            int opcion = JOptionPane.showConfirmDialog(this, "¡Has perdido! ¿Jugar de nuevo?", "Fin del juego", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                iniciarJuego();
            } else {
                System.exit(0);
            }
        }

        if (blocks.stream().noneMatch(JLabel::isVisible)) {
            timer.stop();
            acelerador.stop();
            int opcion = JOptionPane.showConfirmDialog(this, "¡Ganaste con " + puntuacion + " puntos! ¿Jugar otra vez?", "Victoria", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                iniciarJuego();
            } else {
                System.exit(0);
            }
        }

        ball.setLocation((int)(bx + ballDX), (int)(by + ballDY));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BreakoutClone::new);
    }
}
