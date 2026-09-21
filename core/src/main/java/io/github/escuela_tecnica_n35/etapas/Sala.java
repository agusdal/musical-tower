package io.github.escuela_tecnica_n35.etapas;

import java.util.ArrayList;

import io.github.escuela_tecnica_n35.entidades.Enemigo;

public class Sala {

    private int fila;
    private int columna;
    private TipoSala tipo;

    private ArrayList<Enemigo> enemigos;

    public Sala(int fila, int columna, TipoSala tipo) {

        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;

        this.enemigos = new ArrayList<Enemigo>();
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

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }

    public void agregarEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
    }
}