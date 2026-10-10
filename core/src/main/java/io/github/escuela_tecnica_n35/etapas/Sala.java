package io.github.escuela_tecnica_n35.etapas;

import java.util.ArrayList;

import io.github.escuela_tecnica_n35.entidades.Enemigo;
import io.github.escuela_tecnica_n35.entidades.Item;

public class Sala {

    private int fila;
    private int columna;
    private TipoSala tipo;
    
    // Una sala puede tener varias plataformas
    private ArrayList<Plataforma> plataformas;

    private ArrayList<Enemigo> enemigos;

    // NUEVO:
    // Guarda cuántas salas hay que recorrer desde la sala inicial
    // para llegar hasta esta sala.
    private int distanciaInicial;
    
    private Item item;

    public Sala(int fila, int columna, TipoSala tipo) {
    	
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;
        
        this.plataformas = new ArrayList<Plataforma>();
        
        this.enemigos = new ArrayList<Enemigo>();
        
        this.distanciaInicial = 0;
        
        this.item = null;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    // NUEVO:
    // Lo vamos a usar para convertir una sala ENEMIGOS
    // en una sala JEFE.
    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }
    
    public void agregarPlataforma(
            Plataforma plataforma) {

        plataformas.add(
            plataforma
        );
    }


    public ArrayList<Plataforma> getPlataformas() {

        return plataformas;
    }

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }

    public void agregarEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
    }
    
    public void eliminarEnemigo(Enemigo enemigo) {
        enemigos.remove(enemigo);
    }

    // -------------------- DISTANCIA DESDE INICIAL --------------------

    public int getDistanciaInicial() {
        return distanciaInicial;
    }

    public void setDistanciaInicial(int distanciaInicial) {
        this.distanciaInicial = distanciaInicial;
    }
    
    // Devuelve true solamente cuando no queda ningún enemigo vivo.
    //
    // Por ahora, como las salas están vacías,
    // este método devolverá true automáticamente.
    public boolean puedeSalir() {

        for (Enemigo enemigo : enemigos) {

            if (enemigo.estaVivo()) {
                return false;
            }
        }

        return true;
    }
    
    public Item getItem() {
        return item;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public void eliminarItem() {
        this.item = null;
    }


    public boolean tieneItem() {
        return item != null;
    }
}