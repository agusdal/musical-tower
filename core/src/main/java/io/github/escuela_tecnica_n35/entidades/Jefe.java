package entidades;

import java.util.Random;

public class Jefe extends Enemigo {
	
	private double velocidad;
	private boolean velocidadAumentada;
	private int monedasMinimas;
    private int monedasMaximas;
	
	public Jefe(String nombre, Posicion posicion, int vidaMax, double velocidad, int daño, int monedasMinimas, int monedasMaximas) {
		
        super(nombre, posicion, vidaMax, velocidad, daño, monedasMinimas, monedasMaximas);
        this.velocidad = velocidad;
        this.monedasMinimas = monedasMinimas;
        this.monedasMaximas = monedasMaximas;
        
        this.velocidadAumentada = false;
        
    }
	
	public void moverseDerecha() {
	    getPosicion().moverX(8);
	}
	
	public void moverseIzquierda() {
	    getPosicion().moverX(-8);
	}
	
	public void moverseArriba() {
	    getPosicion().moverY(15);
	}
	
	public void atacar(Entidad objetivo) {
        objetivo.recibirDaño(getDaño());
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
	        setVelocidad(getVelocidad() + (getVelocidad() / 2));
	    }
	}
	
}