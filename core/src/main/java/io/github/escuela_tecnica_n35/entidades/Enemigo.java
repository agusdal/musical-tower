package io.github.escuela_tecnica_n35.entidades;

import java.util.Random;

public class Enemigo extends Entidad {
	
    private int monedasMinimas;
    private int monedasMaximas;
    
    private Hitbox hitbox;
    
    private float velocidadY = 0f;
    
    private EstadoEnemigo estado;
    
    private AtaqueEnemigo ataqueNormal;
    private boolean ataqueHaciaArriba;
    
    private float tiempoPreparacionAtaque = 0.5f;
    private float tiempoRecuperacionAtaque = 0.25f;

    private float tiempoFaseAtaque;
    private boolean golpeEjecutado;
	
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
	        150f,
	        100f,
	        1f
	    );

	    agregarAtaque(ataqueNormal);
	    
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
    
    public void actualizarGravedad(
            float delta,
            float gravedad) {

        velocidadY += gravedad * delta;

        getPosicion().moverY(
            velocidadY * delta
        );
    }

    public float getVelocidadY() {
        return velocidadY;
    }

    public void detenerCaida() {
        velocidadY = 0f;
    }
    
    public EstadoEnemigo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnemigo estado) {
        this.estado = estado;
    }
    
    public AtaqueEnemigo getAtaqueNormal() {
        return ataqueNormal;
    }

    public boolean esAtaqueHaciaArriba() {
        return ataqueHaciaArriba;
    }
    
    
    public void iniciarAtaque(boolean haciaArriba) {

        if (estado == EstadoEnemigo.ATACAR
                || !ataqueNormal.estaDisponible()) {
            return;
        }

        estado = EstadoEnemigo.ATACAR;
        ataqueHaciaArriba = haciaArriba;	
        golpeEjecutado = false;
        tiempoFaseAtaque = tiempoPreparacionAtaque;
    }


    // Devuelve true solamente en el momento
    // en que hay que ejecutar el golpe.
    public boolean actualizarSecuenciaAtaque(float delta) {

        if (estado != EstadoEnemigo.ATACAR) {
            return false;
        }

        tiempoFaseAtaque -= delta;

        if (tiempoFaseAtaque > 0f) {
            return false;
        }

        // Terminó la preparación: golpeamos una vez.
        if (!golpeEjecutado) {

            golpeEjecutado = true;

            tiempoFaseAtaque =
                tiempoRecuperacionAtaque;

            return true;
        }

        // Terminó la recuperación.
        estado = EstadoEnemigo.PERSEGUIR;

        return false;
    }
    
}