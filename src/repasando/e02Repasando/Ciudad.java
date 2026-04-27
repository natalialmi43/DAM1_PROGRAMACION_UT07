package repasando.e02Repasando;

public class Ciudad {

    private String nombre;
    private int poblacion;
    private int id_pais;

    public Ciudad(String nombre, int poblacion, int id_pais) {
        this.nombre = nombre;
        this.poblacion = poblacion;
        this.id_pais = id_pais;
    }

    public Ciudad() {
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(int poblacion) {
        this.poblacion = poblacion;
    }

    public int getId_pais() {
        return id_pais;
    }

    public void setId_pais(int id_pais) {
        this.id_pais = id_pais;
    }
}
