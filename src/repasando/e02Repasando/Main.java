package repasando.e02Repasando;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/geografia_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){

            DAO dao = new DAO(connection);

            System.out.println("Ciudades por pais: " + dao.listadoCiudades("Japón"));

            System.out.println("Se ha llevado a cabo el trasvase de poblacion" + (dao.transvasePoblacion("Lyón", "París") ? " Siii" : " Tus ganas gitanas"));

            ArrayList <Ciudad> listadoCiudades = new ArrayList<>();
            try (Scanner sc = new Scanner(new File("nuevas_ciudades.txt"))){

                while (sc.hasNextLine()){
                    String linea = sc.nextLine();
                    String [] aTrozos = linea.split(";");
                    Ciudad ciudad = new Ciudad();
                    ciudad.setNombre(aTrozos[0]);
                    ciudad.setPoblacion(Integer.parseInt(aTrozos[1]));
                    ciudad.setId_pais(Integer.parseInt(aTrozos[2]));
                    listadoCiudades.add(ciudad);
                }
            } catch (IOException ex){
                System.out.println("Error al leer txt");
                ex.printStackTrace();
            }

            ArrayList <Ciudad> listadoC = (ArrayList<Ciudad>) dao.obtenerTodasLasCiudades();

            for (Ciudad ciudad : listadoC){
                System.out.print(ciudad.getNombre() + ", ");
            }


        }catch (SQLException e){
            System.out.println("Error al conectar");
            e.printStackTrace();
        }

        Path archivo = Paths.get("backup_geografia.txt.");

        if (!Files.exists(archivo)){
            try {
                Files.createFile(archivo);
                System.out.println("Creado");
            } catch (IOException e) {
                System.out.println("Error al crear");
                e.printStackTrace();
            }
        }




    }
}
