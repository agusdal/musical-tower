package io.github.escuela_tecnica_n35.entidades;

public class OfertaTienda {

    private Item item;
    private int precio;


    public OfertaTienda(Item item, int precio) {

        this.item = item;
        this.precio = precio;
    }


    public Item getItem() {
        return item;
    }

    public int getPrecio() {
        return precio;
    }
}