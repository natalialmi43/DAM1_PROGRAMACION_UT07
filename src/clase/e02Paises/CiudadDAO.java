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
                    int poblacion =resultSet.getInt("poblacion");
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
                preparedStatementRestar.setString(1, origen);
                preparedStatementRestar.setInt(2, personas);
                preparedStatementRestar.executeUpdate();

                preparedStatementSumar.setString(1, destino);
                preparedStatementSumar.setInt(2, personas);
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

    pu



    /*
    Este método debe conectarse a la base de datos, buscar todas las ciudades que
    pertenezcan al idPais pasado por parámetro, instanciar un objeto Ciudad por cada
    fila del ResultSet, guardarlos en un ArrayList y devolver esa lista
     */

    public List<Ciudad> obtenerCiudadesPorPais(int idPais){

        List<Ciudad> ciudadesPorPais = new ArrayList<>();

        String sql = "select c.id, c.nombre, c.poblacion, c.id_pais \n" +
                "from ciudades c join paises p \n" +
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
