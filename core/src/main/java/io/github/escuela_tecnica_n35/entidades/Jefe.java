package io.github.escuela_tecnica_n35.entidades;

import java.util.Random;
import java.util.ArrayList;

public class Jefe extends Enemigo {
	
	
	private float anchoHitbox;
    private float altoHitbox;
    
    int monedasMinimas;
    int monedasMaximas;
	
	private boolean velocidadAumentada;
	
	private Ataque ataqueDistancia;
	
	public Jefe(String nombre, Posicion posicion, int vidaMax,
			float velocidad, int daño, int monedasMinimas, int monedasMaximas,
			float anchoHitbox, float altoHitbox) {
		
        super(nombre, posicion, vidaMax, velocidad, daño, monedasMinimas, monedasMaximas);
        
        this.anchoHitbox = anchoHitbox;
        this.altoHitbox = altoHitbox;
        
        
        this.monedasMinimas = monedasMinimas;
        this.monedasMaximas = monedasMaximas;
        
        this.velocidadAumentada = false;
        
	    // --------------------------------------------------
	    // ATAQUE A DISTANCIA
	    // --------------------------------------------------
	
	    // PROVISIONAL:
	    // Este ataque se utilizará para crear proyectiles.
	    this.ataqueDistancia =
	        new Ataque(
	            "Disparo del jefe",
	            15,     // daño
	            30f,    // ancho del proyectil
	            30f,    // alto del proyectil
	            2f      // cooldown entre disparos
	        );
	
	
	    // Lo agregamos a la lista heredada de Entidad
	    // para que actualizarCooldowns() también lo actualice.
	    agregarAtaque(
	        ataqueDistancia
	    );
	
	
	     // Evitamos que dispare instantáneamente
	     // apenas entramos a la sala.
	     ataqueDistancia.iniciarCooldown();
        
    }
	
    public float getAnchoHitbox() {
        return anchoHitbox;
    }


    public float getAltoHitbox() {
        return altoHitbox;
    }
	
	public int generarMonedasDrop() {

        Random random = new Random();

        return random.nextInt(
            monedasMaximas - monedasMinimas + 1
        ) + monedasMinimas;
    }
	
	@Override
	public void recibirDaño(int cantidadDaño) {

	    super.recibirDaño(cantidadDaño);

	    if (!velocidadAumentada &&
	        getVidaActual() <= getVidaMax() * 0.30) { // Si la velocidadAumentada no fue aumentada y si tiene menos del 30% de su vida

	        velocidadAumentada = true; // Decimos que "ya se aumento la velocidad una vez"

	        // Aumentamos la velocidad un 50%
	        setVelocidad(getVelocidad() + (getVelocidad() / 2f));
	    }
	}
	
	public Ataque getAtaqueDistancia() {

	    return ataqueDistancia;
	}
	
}