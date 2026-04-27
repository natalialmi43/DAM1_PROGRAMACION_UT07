package repasando.e03Examen;

import java.time.LocalDateTime;
import java.util.Date;

public class Registro_acceso {

    private String dni;
    private LocalDateTime fecha;
    private String zona;


    public Registro_acceso() {
    }

    public Registro_acceso(String dni, LocalDateTime fecha, String zona) {
        this.dni = dni;
        this.fecha = fecha;
        this.zona = zona;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
