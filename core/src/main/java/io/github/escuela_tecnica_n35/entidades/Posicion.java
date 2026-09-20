package io.github.escuela_tecnica_n35.entidades;

public class Posicion {

    protected float x;
    protected float y;

    public Posicion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void moverX(float cantidad) {
        this.x += cantidad;
    }

    public void moverY(float cantidad) {
        this.y += cantidad;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}