import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PantallaInicio extends JPanel {
    private JTextField campoUsuario;
    private JButton botonJugar;
    private BreakoutClone ventana;

    public PantallaInicio(BreakoutClone ventana) {
        this.ventana = ventana;

        setLayout(null);
        setBounds(0, 0, 500, 600);
        setBackground(new Color(220, 240, 255));

        JLabel titulo = new JLabel("Breakout Españita");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBounds(140, 80, 250, 30);
        add(titulo);

        JLabel etiqueta = new JLabel("Introduce tu nombre:");
        etiqueta.setBounds(150, 160, 200, 25);
        add(etiqueta);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(150, 190, 200, 30);
        add(campoUsuario);

        botonJugar = new JButton("Jugar");
        botonJugar.setBounds(200, 240, 100, 30);
        botonJugar.addActionListener((ActionEvent e) -> {
            String nombre = campoUsuario.getText().trim();
            if (!nombre.isEmpty()) {
                ventana.iniciarJuego(nombre);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre.");
            }
        });

        add(botonJugar);
    }
}
