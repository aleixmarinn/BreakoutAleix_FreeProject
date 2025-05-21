import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorBD {

    // Cambia aquí por tu usuario y contraseña MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BreakoutClone?useSSL=false&serverTimezone=UTC";
    private static final String USER = "FreeProject";
    private static final String PASSWORD = "aleix";

    // Carga el driver JDBC al iniciar la clase (opcional en JDBC 4+ pero recomendable)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando driver MySQL: " + e.getMessage());
        }
    }

    public GestorBD() {
        crearTablasSiNoExisten();
    }

    private void crearTablasSiNoExisten() {
        String sqlJugadores = """
            CREATE TABLE IF NOT EXISTS jugadores (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre TEXT NOT NULL UNIQUE
            )
        """;

        String sqlPartidas = """
            CREATE TABLE IF NOT EXISTS partidas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                jugador_id INT NOT NULL,
                puntuacion INT NOT NULL,
                fecha TEXT NOT NULL,
                FOREIGN KEY (jugador_id) REFERENCES jugadores(id)
            )
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlJugadores);
            stmt.execute(sqlPartidas);

            System.out.println("Tablas creadas o ya existen.");

        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    private int obtenerIdJugador(String nombreJugador) {
        String select = "SELECT id FROM jugadores WHERE nombre = ?";
        String insert = "INSERT INTO jugadores (nombre) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Buscar jugador
            try (PreparedStatement psSelect = conn.prepareStatement(select)) {
                psSelect.setString(1, nombreJugador);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }

            // Insertar jugador si no existe
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
        String insertPartida = "INSERT INTO partidas (jugador_id, puntuacion, fecha) VALUES (?, ?, ?)";

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

    // Método para mostrar todas las partidas almacenadas
    public void mostrarPartidas() {
        String sql = """
            SELECT p.id, j.nombre, p.puntuacion, p.fecha
            FROM partidas p
            JOIN jugadores j ON p.jugador_id = j.id
            ORDER BY p.fecha DESC
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Listado de partidas:");
            while (rs.next()) {
                int idPartida = rs.getInt("id");
                String jugador = rs.getString("nombre");
                int puntuacion = rs.getInt("puntuacion");
                String fecha = rs.getString("fecha");

                System.out.printf("Partida %d - Jugador: %s, Puntuación: %d, Fecha: %s%n",
                        idPartida, jugador, puntuacion, fecha);
            }

        } catch (SQLException e) {
            System.out.println("Error al mostrar partidas: " + e.getMessage());
        }
    }

    // Método main para probar
    public static void main(String[] args) {
        GestorBD gestor = new GestorBD();

        // Insertar y guardar partidas de ejemplo
        gestor.guardarPartida("Aleix", 1500);
        gestor.guardarPartida("Maria", 2300);
        gestor.guardarPartida("Aleix", 1800);

        // Mostrar partidas
        gestor.mostrarPartidas();
    }
}
