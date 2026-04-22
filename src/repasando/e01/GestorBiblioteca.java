package repasando.e01;
import java.sql.*;

public class GestorBiblioteca {

    // Modifica la URL para apuntar a la nueva base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    private Connection conexion;

    // TODO Paso 1: Implementar conectar() y desconectar()
    public void conectar() {

        try{
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conectado");

        } catch (SQLException e){
            System.out.println("Error no se pudo conectar");
        }
    }

    public void desconectar() {

        try{
            if (conexion != null && !conexion.isClosed()){
                conexion.close();
            }
        } catch (SQLException e){
            System.out.println("Error no se pudo desconectar");
        }

    }

    // TODO Paso 2: Registrar un nuevo lector en el sistema (INSERT)
    public void registrarLector(String nombre, String dni) {
        String sql = "INSERT INTO Lectores (nombre, dni) VALUES (?,?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, dni);
            preparedStatement.executeUpdate();
            System.out.println("[INFO] Lector registrado (nombre: " + nombre  + " -> dni: " + dni + ").");

        } catch (SQLException e){
            System.out.println("Error no se pudo registrar un nuevo lector");
        }

    }

    // TODO Paso 3: Registrar el préstamo de un libro a un lector (INSERT en tabla intermedia)
    public void prestarLibro(int idLector, int idLibro) {

        String sql = "INSERT INTO Prestamos (idLector, idLibro) VALUES (?,?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setInt(1,idLector);
            preparedStatement.setInt(2, idLibro);
            preparedStatement.executeUpdate();
            System.out.println("[INFO] Prestamo completo (id Lector: " + idLector  + " -> id Libro: " + idLibro + ").");


        } catch (SQLException e){
            System.out.println("Error no se pudo registrar un nuevo lector");
        }

    }

    // TODO Paso 4: Mostrar los títulos y autores de los libros que tiene un lector (SELECT con JOIN)
    public void mostrarLibrosDeLector(int idLector) {
        String sql = "SELECT l.titulo, l.autor" +
                "FROM Prestamos p JOIN Libros l ON p.id_lector = l.id)" +
                "WHERE p.id_lector = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idLector);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                System.out.println("ID lector "+ idLector);
                boolean tienePrestamos =  false;

                while (resultSet.next()){
                    tienePrestamos = true;
                    String titulo = resultSet.getString("titulo");
                    String autor = resultSet.getString("autor");
                    System.out.println("Titulo: " + titulo + " Autor:" + autor);
                }

                if (!tienePrestamos){
                    System.out.println("No tiene prestamos");
                }
            }
        } catch (SQLException e){
            System.out.println("Error no se pudo mostrar los titulos y autores prestados");
        }
    }

    // TODO Paso 5: Eliminar a un lector del sistema comprobando el borrado en cascada (DELETE)
    public void eliminarLector(int idLector) {

        String sql = "DELETE FROM Lectores WHERE id = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setInt(1, idLector);
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0){
                System.out.println("Se ha eliminado al id: " + idLector);
            } else {
                System.out.println("No se encontro al id " + idLector);
            }


        }catch (SQLException e){
            System.out.println("Error al borrar");
        }

    }

    public static void main(String[] args) {
        GestorBiblioteca gestor = new GestorBiblioteca();

        gestor.conectar();

        // 1. Dar de alta a un lector
        gestor.registrarLector("Elena Martinez", "12345678A");

        // 2. Prestarle los libros con ID 1 y 3
        gestor.prestarLibro(1, 1);
        gestor.prestarLibro(1, 3);

        // 3. Comprobar qué libros tiene
        gestor.mostrarLibrosDeLector(1);

        // 4. Eliminar al lector (esto devolverá los libros automáticamente por el CASCADE)
        gestor.eliminarLector(1);

        gestor.desconectar();
    }
}