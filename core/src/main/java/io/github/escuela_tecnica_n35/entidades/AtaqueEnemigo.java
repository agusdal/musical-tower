package io.github.escuela_tecnica_n35.entidades;

import com.badlogic.gdx.math.Rectangle;

public class AtaqueEnemigo extends Ataque {

	public AtaqueEnemigo(
	        String nombre,
	        int daño,
	        float ancho,
	        float alto,
	        float cooldown) {

	    super(nombre, daño, ancho, alto, cooldown);
	}

    public Rectangle obtenerHitboxCuerpoACuerpo(
            Rectangle cuerpo,
            Rectangle objetivo) {

        float centroEnemigoX =
            cuerpo.x + cuerpo.width / 2f;

        float centroObjetivoX =
            objetivo.x + objetivo.width / 2f;

        boolean objetivoALaDerecha =
            centroObjetivoX >= centroEnemigoX;

        float origenX;

        if (objetivoALaDerecha) {
            origenX = cuerpo.x + cuerpo.width;
        } else {
            origenX = cuerpo.x - getAncho();
        }

        float origenY =
            cuerpo.y + (cuerpo.height - getAlto()) / 2f;

        return getHitbox().obtenerRectangulo(
            new Posicion(origenX, origenY)
        );
    }
    
    public Rectangle obtenerHitboxHaciaArriba(Rectangle cuerpo) {

        float x = cuerpo.x + (cuerpo.width - getAncho()) / 2f;
        float y = cuerpo.y + cuerpo.height;

        return getHitbox().obtenerRectangulo(new Posicion(x, y));
    }

    public Proyectil crearProyectil(
            Rectangle cuerpo,
            Rectangle objetivo,
            float velocidad) {

        float origenX =
            cuerpo.x + cuerpo.width / 2f
            - getAncho() / 2f;

        float origenY =
            cuerpo.y + cuerpo.height / 2f
            - getAlto() / 2f;

        Rectangle zonaInicial =
            getHitbox().obtenerRectangulo(
                new Posicion(origenX, origenY)
            );

        return new Proyectil(
            new Posicion(zonaInicial.x, zonaInicial.y),
            objetivo.x + objetivo.width / 2f,
            objetivo.y + objetivo.height / 2f,
            velocidad,
            getDaño(),
            zonaInicial.width,
            zonaInicial.height
        );
    }
}