package io.github.escuela_tecnica_n35.entidades;

public class Item {

    private String nombre;
    private Posicion posicion;
    private String descripcion;
    private int tier;

    // Qué clase de objeto es.
    private TipoItem tipo;

    // Qué estadística modifica.
    private TipoModificadorItem modificador;

    // Cuánto la modifica.
    private float valor;
    
    // Ruta de la imagen dentro de assets.
    //private String imagen;

    public Item(String nombre, Posicion posicion, String descripcion,
            int tier, TipoItem tipo, TipoModificadorItem modificador, float valor/*, String imagen*/) {

        this.nombre = nombre;
        this.posicion = posicion;
        this.descripcion = descripcion;
        this.tier = tier;
        this.tipo = tipo;
        this.modificador = modificador;
        this.valor = valor;
        //this.imagen = imagen;
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

    public TipoModificadorItem getModificador() {
        return modificador;
    }

    public float getValor() {
        return valor;
    }
    
    /*public String getImagen() {
        return imagen;
    }*/
    
}