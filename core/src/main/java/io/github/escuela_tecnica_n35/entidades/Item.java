package entidades;

public class Item {

    private String nombre;
    private Posicion posicion;
    private String descripcion;
    private int tier;
    private TipoItem tipo;

    public Item(String nombre, Posicion posicion, String descripcion,
                int tier, TipoItem tipo) {

        this.nombre = nombre;
        this.posicion = posicion;
        this.descripcion = descripcion;
        this.tier = tier;
        this.tipo = tipo;
    }
    
    public String getNombre() {
        return nombre;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTier() {
        return tier;
    }

    public TipoItem getTipo() {
        return tipo;
    }
    
}