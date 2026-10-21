package io.github.escuela_tecnica_n35.entidades;

import java.util.Random;

public class Enemigo extends Entidad {
	
    private int monedasMinimas;
    private int monedasMaximas;
    
    private EstadoEnemigo estado;

    public Enemigo(String nombre, Posicion posicion, int vidaMax, float velocidad, 
    		int daño, int monedasMinimas, int monedasMaximas) {
    	
        super(nombre, posicion, vidaMax, velocidad, daño);
        
        this.monedasMinimas = monedasMinimas;
        this.monedasMaximas = monedasMaximas;
        
        this.estado = EstadoEnemigo.QUIETO;
    }

    public int generarMonedasDrop() {

        Random random = new Random();

        return random.nextInt(
            monedasMaximas - monedasMinimas + 1
        ) + monedasMinimas;
    }

    public int getMonedasMinimas() {
        return monedasMinimas;
    }

    public int getMonedasMaximas() {
        return monedasMaximas;
    }
    
    public EstadoEnemigo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnemigo estado) {
        this.estado = estado;
    }
    
}