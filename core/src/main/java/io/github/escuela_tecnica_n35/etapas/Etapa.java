package etapas;

import java.util.ArrayList;
import entidades.Enemigo;
import entidades.Jefe;

public class Etapa {

    private String nombre;
    private int dificultad;
    private int numeroPiso;
    private ArrayList<Enemigo> enemigos;
    private Jefe jefe;

    public Etapa(String nombre, int dificultad, int numeroPiso,
                 ArrayList<Enemigo> enemigos, Jefe jefe) {

        this.nombre = nombre;
        this.dificultad = dificultad;
        this.numeroPiso = numeroPiso;
        this.enemigos = enemigos;
        this.jefe = jefe;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDificultad() {
        return dificultad;
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }

    public Jefe getJefe() {
        return jefe;
    }
}