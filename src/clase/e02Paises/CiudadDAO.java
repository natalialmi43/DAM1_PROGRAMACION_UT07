package clase.e02Paises;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CiudadDAO {
    private Connection connection;

    public CiudadDAO(Connection connection) {
        this.connection = connection;
    }


    public List <String> obtenerCiudadesPorContinente (String continente){

        List <String> ciudadesPorContinente = new ArrayList<>();

        String sql = "select c.nombre, c.poblacion, p.nombre \n" +
                "from ciudades c join paises p \n" +
                "where p.continente  = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, continente);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    String nombreCiudad = resultSet.getString("c.nombre");
                    int poblacion =resultSet.getInt("c.poblacion");
                    String nombrePais =  resultSet.getString("p.nombre");

                    String textoFormateado  = String.format("Ciudad: %s | País: %s | Población: %d habitantes",
                            resultSet.getString("ciudad"), resultSet.getString("pais"), resultSet.getInt("poblacion"));

                    ciudadesPorContinente.add(textoFormateado);
                }
            }
        } catch (SQLException e){
            System.out.println("Error al obtener ciudades por continente");
        }

        return ciudadesPorContinente;
    }

    public boolean transvasarPoblacion (String origen, String destino, int personas){

        String sqlRestar = "UPDATE Ciudades SET poblacion = poblacion - ? WHERE nombre = ?";
        String sqlSumar = "UPDATE Ciudades SET poblacion = poblacion + ? WHERE nombre = ?";

        try{

            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementRestar = connection.prepareStatement(sqlRestar);
                 PreparedStatement preparedStatementSumar = connection.prepareStatement(sqlSumar)) {
                preparedStatementRestar.setInt(1, personas);
                preparedStatementRestar.setString(2, origen);
                preparedStatementRestar.executeUpdate();

                preparedStatementSumar.setInt(1, personas);
                preparedStatementSumar.setString(2, destino);
                preparedStatementSumar.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error en el rollback");
            }
            return false;
        } finally {
            try{
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al activar autocommit");
            }
        }
    }



    public int calcularPoblacionContinente(String continente) {
        int totalPoblacion = 0;
        String sql = "{call calcular_poblacion_continente(?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            //Se le pasa el continente
            callableStatement.setString(1, continente);
            //Aqui se le dice que el segundo ? es el numero que devuelve cd pasamos el continente
            callableStatement.registerOutParameter(2, Types.INTEGER);
            //Se ejecuta
            callableStatement.execute();

            //Le pasamos el numero  que devuelve TRAS ejecutar
            totalPoblacion =  callableStatement.getInt(2); // Recuperar el resultado devuelto por MySQL

        } catch (SQLException e){
            System.out.println("Error al buscar");
        }

        return totalPoblacion;

    }



    public List<Ciudad> obtenerCiudadesPorPais(int idPais){

        List<Ciudad> ciudadesPorPais = new ArrayList<>();

        String sql = "select c.id as id, c.nombre as nombre, c.poblacion as poblacion, c.id_pais as id_pais \n" +
                "from ciudades c join paises p on c.id_pais = p.id \n" +
                "where p.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, idPais);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setId(resultSet.getInt("id"));
                    ciudad.setNombre(resultSet.getString("nombre"));
                    ciudad.setPoblacion(resultSet.getInt("poblacion"));
                    ciudad.setIdPais(resultSet.getInt("id_pais"));
                    ciudadesPorPais.add(ciudad);
                }

            }

        } catch (SQLException e){
            System.out.println("Error al obtener ciudades");
        }

        return ciudadesPorPais;
    }


}
