package io.github.escuela_tecnica_n35.entidades;

import java.util.Random;
import java.util.ArrayList;

public class Jefe extends Enemigo {
	
	
	private float anchoHitbox;
    private float altoHitbox;
    
    int monedasMinimas;
    int monedasMaximas;
	
	private boolean velocidadAumentada;
	
	public Jefe(String nombre, Posicion posicion, int vidaMax,
			float velocidad, int daño, int monedasMinimas, int monedasMaximas,
			float anchoHitbox, float altoHitbox) {
		
        super(nombre, posicion, vidaMax, velocidad, daño, monedasMinimas, monedasMaximas);
        
        this.anchoHitbox = anchoHitbox;
        this.altoHitbox = altoHitbox;
        
        
        this.monedasMinimas = monedasMinimas;
        this.monedasMaximas = monedasMaximas;
        
        this.velocidadAumentada = false;
        
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
	
}