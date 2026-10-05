package io.github.escuela_tecnica_n35.entidades;

public class Ataque {

    private String nombre;

    private int daño;

    // Tamaño de la hitbox del ataque.
    private float ancho;
    private float alto;

    // Tiempo TOTAL de la
    // recuperación del ataque.
    private float cooldown;
    
	// Tiempo que falta para poder volver
	// a utilizar este ataque.
	private float cooldownRestante;


    public Ataque(
            String nombre,
            int daño,
            float ancho,
            float alto,
            float cooldown) {

        this.nombre = nombre;
        this.daño = daño;
        this.ancho = ancho;
        this.alto = alto;
        this.cooldown = cooldown;
        
        // Al crearse, el ataque está disponible.
        this.cooldownRestante = 0;
    }


    public String getNombre() {
        return nombre;
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

    public float getCooldown() {
        return cooldown;
    }
    
	// --------------------------------------------------
	// COOLDOWN
	// --------------------------------------------------
	
	public boolean estaDisponible() {
	
	    return cooldownRestante <= 0;
	}
	
	
	public void iniciarCooldown() {
	
	    cooldownRestante = cooldown;
	}
	
	
	public void actualizarCooldown(float delta) {
	
	    if (cooldownRestante > 0) {
	
	        cooldownRestante -= delta;
	
	        // Evitamos valores negativos.
	        if (cooldownRestante < 0) {
	
	            cooldownRestante = 0;
	        }
	    }
	}
	
	
	public float getCooldownRestante() {
	
	    return cooldownRestante;
	}
}