package entidades;

public class Entidad {

    private String nombre;
    protected Posicion posicion;
    private int vidaActual;
    private int vidaMax;
    private double velocidad;
    private int daño;
    
    
    
    public Entidad(String nombre, Posicion posicion, int vidaMax, double velocidad, int daño) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.vidaMax = vidaMax;
        this.vidaActual = vidaMax;
        this.velocidad = velocidad;
        this.daño = daño;
        
    }
    
    public Posicion getPosicion() {
        return posicion;
    }
    
    public int getDaño() {
    	
        return daño;
    }
    
    public int getVidaMax() {
    	
        return vidaMax;
    }
    
    public double getVelocidad() {
        return velocidad;
    }
    
    public int getVidaActual() {
    	
        return vidaActual;
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
    
    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }
}