package io.github.escuela_tecnica_n35.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Jugador extends Entidad {

    private int monedas;
    private Inventario inventario;
    private Item objetoActivo;

    // Frames individuales
    private TextureRegion frameQuieto;
    
    private TextureRegion frameSaltoDerecha;
    private TextureRegion frameCaidaDerechaInicio;
    private TextureRegion frameCaidaDerechaEstatico;

    private TextureRegion frameSaltoIzquierda;
    private TextureRegion frameCaidaIzquierdaInicio;
    private TextureRegion frameCaidaIzquierdaEstatico;

    // Animaciones
    private Animation<TextureRegion> animCaminarDerecha;
    private Animation<TextureRegion> animCaminarIzquierda;

    private float stateTime = 0f;
    private float tiempoCaida = 0f; // Para controlar la transición durante la caída

    // Estados
    private boolean moviendose = false;
    private boolean mirandoIzquierda = false;
    private boolean enElPiso = true;
    private float velocidadY = 0f;
    
    

    public Jugador(String nombre, Posicion posicion, int vidaMax, float velocidad, int daño) { // CONSTRUCTOR
        super(nombre, posicion, vidaMax, velocidad, daño);
        this.monedas = 0;
        this.inventario = new Inventario();

        Texture walkSheet = new Texture("frames.png");
        int FRAME_COLS = 17;
        int FRAME_ROWS = 1;

        TextureRegion[][] tmp = TextureRegion.split(
            walkSheet, 
            walkSheet.getWidth() / FRAME_COLS, 
            walkSheet.getHeight() / FRAME_ROWS
        );

        // Frame 0: Quieto
        frameQuieto = tmp[0][0];

        // Frames 1 a 5: Caminata Derecha (5 frames)
        TextureRegion[] framesDerecha = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            framesDerecha[i] = tmp[0][i + 1];
        }
        animCaminarDerecha = new Animation<TextureRegion>(0.09f, framesDerecha);

        // Frame 6: Salto Derecha
        frameSaltoDerecha = tmp[0][6];

        // Frames 7 y 8: Caída Derecha (Instante inicial vs Estático)
        frameCaidaDerechaInicio = tmp[0][7];
        frameCaidaDerechaEstatico = tmp[0][8];

        // Frames 9 a 13: Caminata Izquierda (5 frames)
        TextureRegion[] framesIzquierda = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            framesIzquierda[i] = tmp[0][i + 9];
        }
        animCaminarIzquierda = new Animation<TextureRegion>(0.09f, framesIzquierda);

        // Frame 14: Salto Izquierda
        frameSaltoIzquierda = tmp[0][14];

        // Frames 15 y 16: Caída Izquierda (Instante inicial vs Estático)
        frameCaidaIzquierdaInicio = tmp[0][15];
        frameCaidaIzquierdaEstatico = tmp[0][16];
    }

    public void setEstado(boolean moviendose, boolean mirandoIzquierda, boolean enElPiso, float velocidadY) {
        if (this.enElPiso != enElPiso || this.mirandoIzquierda != mirandoIzquierda) {
            stateTime = 0f;
            tiempoCaida = 0f;
        }
        
        this.moviendose = moviendose;
        this.mirandoIzquierda = mirandoIzquierda;
        this.enElPiso = enElPiso;
        this.velocidadY = velocidadY;
    }

    public void render(SpriteBatch batch, float delta) {
        TextureRegion frameActual;

        if (!enElPiso) {
            // EN EL AIRE
            if (velocidadY >= 0) {
                // Impulso hacia arriba: Frame de Salto
                tiempoCaida = 0f;
                frameActual = mirandoIzquierda ? frameSaltoIzquierda : frameSaltoDerecha;
            } else {
                // Descendiendo
                tiempoCaida += delta;

                // Sostiene el primer frame de caída por 0.12 segundos y luego pasa al estático
                if (tiempoCaida < 0.3f) {
                    frameActual = mirandoIzquierda ? frameCaidaIzquierdaInicio : frameCaidaDerechaInicio;
                } else {
                    frameActual = mirandoIzquierda ? frameCaidaIzquierdaEstatico : frameCaidaDerechaEstatico;
                }
            }
        } else if (moviendose) {
            // EN EL PISO CAMINANDO
            stateTime += delta;
            if (mirandoIzquierda) {
                frameActual = animCaminarIzquierda.getKeyFrame(stateTime, true);
            } else {
                frameActual = animCaminarDerecha.getKeyFrame(stateTime, true);
            }
        } else {
            // EN EL PISO QUIETO
            stateTime = 0f;
            tiempoCaida = 0f;
            frameActual = frameQuieto;
        }

        batch.draw(frameActual, getPosicion().getX(), getPosicion().getY());
    }

	//----------------------------\/EFECTOS A VIDA\/-----------------------------
	
	public void atacar(Entidad objetivo) {
		objetivo.recibirDaño(getDaño());
	}
	
	public void aumentarVidaMax(Entidad objetivo) {
		objetivo.recibirDaño(getVidaMax());
	}
	
	//----------------------------/\EFECTOS A VIDA/\-----------------------------
	
	//----------------------------\/MONEDAS\/-----------------------------
	
	public int agarrarMonedas(int monedasGanadas) {
		return monedas += monedasGanadas;
	}
	
	public int restarMonedas(int monedasPerdidas) {
		if (monedas >= monedasPerdidas) {
			monedas -= monedasPerdidas;
		}
		return monedas;
	}
	
	public int getMonedas() {
		return monedas;
	}
	
	//----------------------------/\MONEDAS/\-----------------------------
	
	//----------------------------\/OBJETO ACTIVO\/-----------------------------
	
	public Item getObjetoActivo() {
		return objetoActivo;
	}
	
	//----------------------------/\OBJETO ACTIVO/\-----------------------------
	
	//----------------------------\/INVENTARIO\/-----------------------------
	
	public Inventario getInventario() {
		return inventario;
	}
	
	//----------------------------/\INVENTARIO/\-----------------------------
}