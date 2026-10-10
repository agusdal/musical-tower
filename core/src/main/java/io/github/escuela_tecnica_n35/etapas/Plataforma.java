package io.github.escuela_tecnica_n35.etapas;

public class Plataforma {

    private float x;
    private float y;
    private float ancho;
    private float alto;

    public Plataforma(
            float x,
            float y,
            float ancho,
            float alto) {

        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }
}