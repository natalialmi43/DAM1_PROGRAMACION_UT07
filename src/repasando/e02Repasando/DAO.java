package repasando.e02Repasando;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    public ArrayList <String> listadoCiudades (String pais){

        ArrayList <String> listado = new ArrayList<>();
        String sql = "select c.nombre \n" +
                "from paises p join ciudades c on p.id = c.id_pais \n" +
                "where p.nombre = ? order by c.poblacion desc";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, pais);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    listado.add(resultSet.getString("c.nombre"));
                }
            }catch (SQLException e){
                System.out.println("Error al ejecutar la consulta");
                e.printStackTrace();
            }

        } catch (SQLException e){
            System.out.println("Error al realizar la consulta");
            e.printStackTrace();
        }
        return listado;
    }
    public boolean transvasePoblacion (String ciudadOrigen, String ciudadDestino) {

        String sqlPoblacionLyon = "select c.poblacion \n" +
                "from ciudades c \n" +
                "where c.nombre = ?";

        String sqlRestar = " update ciudades  \n" +
                "set poblacion = poblacion - ?\n" +
                "where nombre = ?";
        String sqlSumar = "update ciudades  \n" +
                "set poblacion = poblacion + ?\n" +
                "where nombre = ?";

        String sqlBorrar = "DELETE FROM Ciudades WHERE nombre = ?";



        int poblacionLyon = 0;


        try{
            connection.setAutoCommit(false);


            try (PreparedStatement preparedStatementConsulta = connection.prepareStatement(sqlPoblacionLyon);
                 PreparedStatement preparedStatementRestar = connection.prepareStatement(sqlRestar);
                 PreparedStatement preparedStatementSumar = connection.prepareStatement(sqlSumar);
                 PreparedStatement preparedStatementBorrar = connection.prepareStatement(sqlBorrar)){

                preparedStatementConsulta.setString(1, ciudadOrigen);

                try (ResultSet resultSet = preparedStatementConsulta.executeQuery()){
                    if (resultSet.next()){
                        poblacionLyon = resultSet.getInt("c.poblacion");
                    }
                }catch (SQLException exception){
                    System.out.println("Error consulta");
                    exception.printStackTrace();
                }

                preparedStatementRestar.setInt(1, poblacionLyon);
                preparedStatementRestar.setString(2, ciudadOrigen);
                preparedStatementRestar.executeUpdate();

                preparedStatementSumar.setInt(1, poblacionLyon);
                preparedStatementSumar.setString(2, ciudadDestino);
                preparedStatementSumar.executeUpdate();

                preparedStatementBorrar.setString(1,ciudadOrigen);
                preparedStatementBorrar.executeUpdate();

            }
            connection.commit();
            return true;

        } catch (SQLException e){
            try{
                connection.rollback();
            } catch (SQLException ex){
                System.out.println("Error en rb");
                e.printStackTrace();
            }
            return false;

        } finally {
            try{
                connection.setAutoCommit(true);
            } catch (SQLException e){
                System.out.println("Error autoCommit");
                e.printStackTrace();
            }
        }
    }

    public void insterCiudades (ArrayList<Ciudad> listado){
        String sql = "insert into ciudades (nombre, poblacion, id_pais) values (?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            for (Ciudad ciudad : listado){
                String nombre = ciudad.getNombre();
                int poblacion = ciudad.getPoblacion();
                int id_pais = ciudad.getId_pais();

                preparedStatement.setString(1, nombre);
                preparedStatement.setInt(2, poblacion);
                preparedStatement.setInt(3, id_pais);
                preparedStatement.executeUpdate();
            }

        }catch (SQLException e){
            System.out.println("Error insert");
            e.printStackTrace();
        }
    }

    public List<Ciudad> obtenerTodasLasCiudades(){

        List<Ciudad> listado = new ArrayList<>();

        try (Statement statement = connection.createStatement()){

            try(ResultSet resultSet = statement.executeQuery("SELECT * FROM Ciudades")){
                while (resultSet.next()) {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setNombre(resultSet.getString("nombre"));
                    ciudad.setPoblacion(resultSet.getInt("poblacion"));
                    ciudad.setId_pais(resultSet.getInt("id_pais"));
                    listado.add(ciudad);
                }

            }
        } catch (SQLException e){
            System.out.println("Error al obtener ciudades");
            e.printStackTrace();
        }

        return listado;
    }
}
