package io.github.escuela_tecnica_n35.entidades;

import java.util.ArrayList;

public class Entidad {

    private String nombre;
    protected Posicion posicion;
    private int vidaActual;
    private int vidaMax;
    private float velocidad;
    private int daño;
    
    // Lista de ataques disponibles para esta entidad.
    protected ArrayList<Ataque> ataques;
    
    
    public Entidad(String nombre, Posicion posicion, int vidaMax, float velocidad, int daño) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.vidaMax = vidaMax;
        this.vidaActual = vidaMax;
        this.velocidad = velocidad;
        this.daño = daño;
        
        this.ataques = new ArrayList<Ataque>();
    }
    
    public Posicion getPosicion() {
        return posicion;
    }
    
    public int getDaño() {
    	
        return daño;
    }
	    
	// --------------------------------------------------
	// ATAQUE
	// --------------------------------------------------
	
	public void atacar(
	        Entidad objetivo,
	        Ataque ataque) {
	
	    objetivo.recibirDaño(
	        ataque.getDaño()
	    );
		}
    
    public int getVidaMax() {
    	
        return vidaMax;
    }
    
    public int getVidaActual() {
    	
        return vidaActual;
    }
    
    public void aumentarVidaMax(int aumento) {

        if (aumento > 0) {

            vidaMax += aumento;

            // También aumentamos la vida actual
            // en la misma cantidad.
            vidaActual += aumento;
        }
    }
    
    public void reiniciarVida(int vidaMaxInicial) {

        this.vidaMax = vidaMaxInicial;
        this.vidaActual = vidaMaxInicial;
    }
    
    public float getVelocidad() {
        return velocidad;
    }
    
    public void recibirDaño(int cantidadDaño) {
    	
    	if(cantidadDaño > this.vidaActual) {
    		this.vidaActual = 0;
    	} else {
    		
    		this.vidaActual -= cantidadDaño;
    	}
    }
    
    public boolean estaVivo() {
    	
    	boolean vivo;
    	
    	if(this.vidaActual <= 0) {
    		
    		vivo = false;
    		
    	} else {
    		
    		vivo = true;
    		
    	}
    	return vivo;
    }
    
    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }
    
	// --------------------------------------------------
	// ATAQUES
	// --------------------------------------------------
	
	public void agregarAtaque(Ataque ataque) {
	
	    ataques.add(ataque);
	}
	
	
	public ArrayList<Ataque> getAtaques() {
	
	    return ataques;
	}
	
	
	public Ataque getAtaque(int indice) {
	
	    return ataques.get(indice);
	}
	
	
	public void actualizarCooldowns(float delta) {

	    for (Ataque ataque : ataques) {

	        ataque.actualizarCooldown(delta);
	    }
	}
}