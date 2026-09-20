package io.github.escuela_tecnica_n35.entidades;

import java.util.ArrayList;

public class Inventario {

    private ArrayList<Item> items;

    public Inventario() {
        items = new ArrayList<Item>();
    }

    public void agregarItem(Item item) {
        items.add(item);
    }

    public void eliminarItem(Item item) {
        items.remove(item);
    }

    public boolean tieneItem(Item item) {
        return items.contains(item);
    }

    public int cantidadItems() {
        return items.size();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}