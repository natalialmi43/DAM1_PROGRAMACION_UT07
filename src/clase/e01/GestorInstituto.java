package clase.e01;

import java.sql.*;

public class GestorInstituto {

    private static final String URL = "jdbc:mysql://localhost:3306/instituto_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // Que cada alumno ponga la suya
    private Connection conexion;

    // TODO Paso 1: Crear el método conectar() y desconectar()
    // Mantenemos una única conexión en toda la aplicación
    public void conectar (){
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conectado");

        } catch (SQLException e){
            System.out.println("Error no se pudo conectar");
        }

    }

    public void desconectar (){

        try{
            if (conexion != null && !conexion.isClosed()){
                conexion.close();
                System.out.println("Conexion cerrada");
            }

        } catch (SQLException e){
            System.out.println("error al desconectar");
        }
    }

    // TODO Paso 2: Crear el método registrarAlumno(String nombre, String email)
    // Alta de un alumno en su tabla correspondiente

    public void registrarAlumno(String nombre, String email){

        String sql = "INSERT INTO Alumnos (nombre, email) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    System.out.println("Alumno registrado. ID: " + generatedKeys.getLong(1));
                }

            }

        } catch (SQLException e){
            System.out.println("Error al registrar usuario");
        }
    }

    // TODO Paso 3: Crear el método matricularAlumno(int idAlumno, int idAsignatura)
    // Aquí estamos insertando una relación N:N

    public void matricularAlumno(int idAlumno, int idAsignatura){
        String sql = "INSERT INTO Matriculas (id_alumno, id_asignatura) VALUES (?,?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setInt(1, idAlumno);
            preparedStatement.setInt(2, idAsignatura);
            preparedStatement.executeUpdate();
            System.out.println("[INFO] Matrícula completada (Alumno ID: " + idAlumno + " -> Asignatura ID: " + idAsignatura + ").");

        } catch (SQLException e){
            System.out.println("Error matricula");
        }
    }

    // TODO Paso 4: Crear el método mostrarAsignaturasDeAlumno(int idAlumno)
    // Select con join...

    public void mostrarAsignaturasDeAlumno(int idAlumno){
        String sql = "SELECT a.nombre, a.creditos " +
                "FROM Asignaturas a " +
                "JOIN Matriculas m ON a.id = m.id_asignatura " +
                "WHERE m.id_alumno = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){

            preparedStatement.setInt(1, idAlumno);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                System.out.println("Expediente del alumno id: " + idAlumno);
                boolean tieneAsignadasAsignaturas = false;

                while (resultSet.next()){
                    tieneAsignadasAsignaturas = true;
                    String nombreAsignatura = resultSet.getString("nombre");
                    int creditos = resultSet.getInt("creditos");
                    System.out.println("- " + nombreAsignatura + " (" + creditos + " créditos)");
                }

                if(!tieneAsignadasAsignaturas){
                    System.out.println("No está matriculado");
                }

            }

        } catch (SQLException e){
            System.err.println("[ERROR] Fallo al consultar expediente: " + e.getMessage());
        }
    }

    // TODO Paso 5: Crear el método darDeBajaAlumno(int idAlumno)
    // Delete...
    public void darDeBajaAlumno(int idAlumno){
        String sql = "DELETE FROM Alumnos WHERE id = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)){
            preparedStatement.setInt(1,idAlumno);
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0){
                System.out.println("[INFO] Alumno ID " + idAlumno + " eliminado junto con todas sus matrículas.");
            } else {
                System.out.println("[AVISO] No se encontró un alumno con ID " + idAlumno + " para borrar.");            }

        } catch (SQLException e){
            System.out.println("Error al eliminar");
        }

    }

    public static void main(String[] args) {
        GestorInstituto gestor = new GestorInstituto();
        // Aquí iremos probando los métodos paso a paso

        gestor.conectar();

        gestor.registrarAlumno("Carlos Perez", "carlos@instituto.es");
        gestor.registrarAlumno("Laura Gomez", "laura@instituto.es");

        gestor.matricularAlumno(1, 1);
        gestor.matricularAlumno(1, 2);

        gestor.mostrarAsignaturasDeAlumno(1);

        gestor.darDeBajaAlumno(1);

        gestor.mostrarAsignaturasDeAlumno(1);

        gestor.desconectar();
    }
}
