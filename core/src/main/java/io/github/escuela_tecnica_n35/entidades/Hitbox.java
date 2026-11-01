package io.github.escuela_tecnica_n35.entidades;

import com.badlogic.gdx.math.Rectangle;

public class Hitbox {

    private float ancho;
    private float alto;

    private float desplazamientoX;
    private float desplazamientoY;


    public Hitbox(
            float ancho,
            float alto,
            float desplazamientoX,
            float desplazamientoY) {

        this.ancho = ancho;
        this.alto = alto;

        this.desplazamientoX =
            desplazamientoX;

        this.desplazamientoY =
            desplazamientoY;
    }


    public Rectangle obtenerRectangulo(
            Posicion posicion) {

        return new Rectangle(
            posicion.getX()
                + desplazamientoX,

            posicion.getY()
                + desplazamientoY,

            ancho,
            alto
        );
    }


    public float getAncho() {
        return ancho;
    }


    public float getAlto() {
        return alto;
    }
}