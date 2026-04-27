package repasando.e03Examen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/accesos_bd";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {

        List<Registro_acceso> listadoAccesos = new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss");

        try (Scanner sc = new Scanner(new File("accesos_diario.txt"))){

            String linea = sc.nextLine();

            while (sc.hasNextLine()){
                String [] trozos = linea.split(";");
                Registro_acceso registroAcceso = new Registro_acceso();
                registroAcceso.setDni(trozos[0]);
                LocalDateTime fecha = LocalDateTime.parse(trozos[1], dateTimeFormatter);
                registroAcceso.setFecha(fecha);
                registroAcceso.setZona(trozos[2]);

                listadoAccesos.add(registroAcceso);

                linea = sc.nextLine();

            }
        }catch (IOException e){
            System.out.println("Error al leer");
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){

            DAO dao = new DAO(connection);



        }catch (SQLException e){
            System.out.println("Error al conectar");
        }



    }
}
