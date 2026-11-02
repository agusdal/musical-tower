package io.github.escuela_tecnica_n35.entidades;

import java.util.Random;

public class Enemigo extends Entidad {
	
    private int monedasMinimas;
    private int monedasMaximas;
    
    private Hitbox hitbox;
    
    private EstadoEnemigo estado;
    
    private Ataque ataqueNormal;
    
	// Tiempo que necesita el enemigo para preparar
	// su primer ataque al entrar en rango.
	private float tiempoPreparacionAtaque;
	
	// Tiempo que todavía le falta esperar.
	private float tiempoPreparacionRestante;
	
	public Enemigo(
	        String nombre,
	        Posicion posicion,
	        int vidaMax,
	        float velocidad,
	        int daño,
	        int monedasMinimas,
	        int monedasMaximas,
	        Hitbox hitbox) {

	    super(nombre, posicion, vidaMax, velocidad, daño);

	    this.monedasMinimas = monedasMinimas;
	    this.monedasMaximas = monedasMaximas;

	    this.hitbox = hitbox;

	    this.estado = EstadoEnemigo.QUIETO;

	    this.ataqueNormal = new AtaqueEnemigo(
	        "Ataque enemigo",
	        daño,
	        100f,
	        100f,
	        1f
	    );

	    agregarAtaque(ataqueNormal);

	    this.tiempoPreparacionAtaque = 0.5f;
	    this.tiempoPreparacionRestante =
	        tiempoPreparacionAtaque;
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
    
    public Hitbox getHitbox() {
        return hitbox;
    }
    
    public EstadoEnemigo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnemigo estado) {
        this.estado = estado;
    }
    
    public Ataque getAtaqueNormal() {
        return ataqueNormal;
    }
    
    
    public void actualizarPreparacionAtaque(
            float delta,
            boolean dentroDelRango) {

        // Si el jugador salió de la zona de ataque,
        // reiniciamos la preparación.
        if (!dentroDelRango) {

            tiempoPreparacionRestante =
                tiempoPreparacionAtaque;

            return;
        }


        // Mientras permanezca dentro del rango,
        // el tiempo va bajando.
        if (tiempoPreparacionRestante > 0) {

            tiempoPreparacionRestante -= delta;

            if (tiempoPreparacionRestante < 0) {
                tiempoPreparacionRestante = 0;
            }
        }
    }


    public boolean ataquePreparado() {

        return tiempoPreparacionRestante <= 0;
    }
    
}