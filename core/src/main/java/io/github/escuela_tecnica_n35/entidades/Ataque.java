package io.github.escuela_tecnica_n35.entidades;

public class Ataque {

    private String nombre;

    private int daño;

    private float alcance;

    private float cooldown;


    public Ataque(
            String nombre,
            int daño,
            float alcance,
            float cooldown) {

        this.nombre = nombre;

        this.daño = daño;

        this.alcance = alcance;

        this.cooldown = cooldown;
    }


    public String getNombre() {
        return nombre;
    }


    public int getDaño() {
        return daño;
    }


    public float getAlcance() {
        return alcance;
    }


    public float getCooldown() {
        return cooldown;
    }
}