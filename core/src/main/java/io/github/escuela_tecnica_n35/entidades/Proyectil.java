package io.github.escuela_tecnica_n35.entidades;

public class Proyectil {

    private Posicion posicion;

    private float direccionX;
    private float direccionY;

    private float velocidad;

    private int daño;

    private float ancho;
    private float alto;

    private boolean activo;


    public Proyectil(
            Posicion posicion,
            float objetivoX,
            float objetivoY,
            float velocidad,
            int daño,
            float ancho,
            float alto) {

        this.posicion = posicion;

        this.velocidad = velocidad;
        this.daño = daño;

        this.ancho = ancho;
        this.alto = alto;

        this.activo = true;


        // Dirección desde el proyectil
        // hasta el objetivo.
        float distanciaX =
            objetivoX - posicion.getX();

        float distanciaY =
            objetivoY - posicion.getY();


        // Calculamos la longitud del vector.
        float longitud =
            (float) Math.sqrt(
                distanciaX * distanciaX
                + distanciaY * distanciaY
            );


        // Normalizamos la dirección para que
        // la velocidad sea igual en cualquier ángulo.
        if (longitud != 0) {

            direccionX =
                distanciaX / longitud;

            direccionY =
                distanciaY / longitud;
        }
    }


    public void actualizar(
            float delta) {

        posicion.moverX(
            direccionX
            * velocidad
            * delta
        );

        posicion.moverY(
            direccionY
            * velocidad
            * delta
        );
    }


    public Posicion getPosicion() {
        return posicion;
    }


    public int getDaño() {
        return daño;
    }


    public float getAncho() {
        return ancho;
    }


    public float getAlto() {
        return alto;
    }


    public boolean estaActivo() {
        return activo;
    }


    public void destruir() {
        activo = false;
    }
}