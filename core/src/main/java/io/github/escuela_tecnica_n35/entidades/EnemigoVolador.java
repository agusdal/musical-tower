package io.github.escuela_tecnica_n35.entidades;

public class EnemigoVolador extends Enemigo {

    public EnemigoVolador(
            String nombre,
            Posicion posicion,
            int vidaMax,
            float velocidad,
            int daño,
            int monedasMinimas,
            int monedasMaximas) {

    	super(
    	    nombre,
    	    posicion,
    	    vidaMax,
    	    velocidad,
    	    daño,
    	    monedasMinimas,
    	    monedasMaximas,
    	    new Hitbox(
    	        70f,
    	        70f,
    	        0f,
    	        0f
    	    )
    	);
    }


    // --------------------------------------------------
    // PERSEGUIR AL JUGADOR
    // --------------------------------------------------

    public void perseguir(
            Posicion objetivo,
            float delta) {

        float distanciaX =
            objetivo.getX()
            - getPosicion().getX();

        float distanciaY =
            objetivo.getY()
            - getPosicion().getY();


        // Distancia total hasta el jugador.
        float distancia =
            (float) Math.sqrt(
                distanciaX * distanciaX
                + distanciaY * distanciaY
            );


        if (distancia == 0) {
            return;
        }


        // Normalizamos la dirección.
        float direccionX =
            distanciaX / distancia;

        float direccionY =
            distanciaY / distancia;


        // Nos movemos hacia el jugador
        // manteniendo siempre la misma velocidad.
        getPosicion().moverX(
            direccionX
            * getVelocidad()
            * delta
        );

        getPosicion().moverY(
            direccionY
            * getVelocidad()
            * delta
        );
    }
    
}