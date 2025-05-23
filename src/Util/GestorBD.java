package Util;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorBD {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/BreakoutClone?useSSL=false&serverTimezone=UTC";
    private static final String USER = "FreeProject";
    private static final String PASSWORD = "aleix";

    private int obtenerIdJugador(String nombreJugador) {
        String select = "select id from jugadores where nombre = ?";
        String insert = "insert into jugadores (nombre) values (?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            try (PreparedStatement psSelect = conn.prepareStatement(select)) {
                psSelect.setString(1, nombreJugador);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setString(1, nombreJugador);
                int affectedRows = psInsert.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No se pudo insertar el jugador.");
                }

                try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se obtuvo el ID generado del jugador.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener/insertar jugador: " + e.getMessage());
        }
        return -1;
    }

    public void guardarPartida(String nombreJugador, int puntuacion) {
        int idJugador = obtenerIdJugador(nombreJugador);
        if (idJugador == -1) {
            System.out.println("No se pudo obtener el ID del jugador.");
            return;
        }

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String insertPartida = "insert into partidas (jugador_id, puntuacion, fecha) values (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(insertPartida)) {

            ps.setInt(1, idJugador);
            ps.setInt(2, puntuacion);
            ps.setString(3, fecha);
            ps.executeUpdate();

            System.out.println("Partida guardada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        GestorBD gestor = new GestorBD();

        gestor.guardarPartida("Aleix", 1500);
        gestor.guardarPartida("Maria", 2300);
        gestor.guardarPartida("Aleix", 1800);

    }
}
