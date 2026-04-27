package clase.e02Paises;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/geografia_db";
    private static final String USER = "root";
    private static final String PASSWORD = "tu_password";

    public static void main(String[] args) {


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){

            //Imporante instanciar la clase para aplicar metodos.
            CiudadDAO ciudadDAO = new CiudadDAO(connection);


            System.out.println("Ciudades por continente: ");
            String continente = "Europa";
            List<String> cuidadesEuropa = ciudadDAO.obtenerCiudadesPorContinente(continente);
            for (String c : cuidadesEuropa){
                System.out.println(c);
            }


            System.out.println("\nTransvase poblacion");
            boolean exito = ciudadDAO.transvasarPoblacion("Barcelona", "Madrid", 50000);

            if (exito) {
                System.out.println("Operación completada con éxito (COMMIT ejecutado).");
            } else {
                System.out.println("Fallo en la operación. Se ha dejado la base de datos intacta (ROLLBACK ejecutado).");
            }


            System.out.println("\nCalcular población:");
            String continente2 = "Asia";
            System.out.println("La poblacion total de " + continente2 + " es: " + ciudadDAO.calcularPoblacionContinente(continente2));


            System.out.println("\nCiudades por país:");
            int idPais = 4;
            List<Ciudad> ciudadesPorPais = ciudadDAO.obtenerCiudadesPorPais(idPais);
            for (Ciudad c : ciudadesPorPais){
                System.out.println(c.getNombre());
            }




        }catch (SQLException e){
            System.out.println("Error al conectar");
        }

    }
}
