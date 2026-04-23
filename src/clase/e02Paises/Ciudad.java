package clase.e02Paises;

public class Ciudad {
    private int id;
    private String nombre;
    private int idPais;
    private int poblacion;

    public Ciudad() {
    }

    public Ciudad(int id, String nombre, int idPais, int poblacion) {
        this.id = id;
        this.nombre = nombre;
        this.idPais = idPais;
        this.poblacion = poblacion;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public int getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(int poblacion) {
        this.poblacion = poblacion;
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", idPais=" + idPais +
                ", poblacion=" + poblacion +
                '}';
    }
}
