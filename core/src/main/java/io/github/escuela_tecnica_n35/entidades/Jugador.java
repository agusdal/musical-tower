package io.github.escuela_tecnica_n35.entidades;

// herramientas que se encargan de "dibujar", recortar y mostrar los frames
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Jugador extends Entidad {
	
	private int monedas;
	private Inventario inventario;
	private Item objetoActivo;
	
	// Variables para la animación de LibGDX
		private Animation<TextureRegion> caminataAnimacion;
		private float stateTime = 0f;
	
	public Jugador(String nombre, Posicion posicion, int vidaMax, float velocidad, int daño) {
		
        super(nombre, posicion, vidaMax, velocidad, daño);
        this.monedas = 0;
        this.inventario = new Inventario();
        
     // Cargar y configurar la animación del Spritesheet
        Texture walkSheet = new Texture("frames.png");
        int FRAME_COLS = 17;
        int FRAME_ROWS = 1;

        TextureRegion[][] tmp = TextureRegion.split(
            walkSheet, 
            walkSheet.getWidth() / FRAME_COLS, 
            walkSheet.getHeight() / FRAME_ROWS
        );

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS];
        for (int j = 0; j < FRAME_COLS; j++) {
            walkFrames[j] = tmp[0][j];
        }

        // Se asigna la animación (0.05 segundos por frame)
        this.caminataAnimacion = new Animation<TextureRegion>(0.05f, walkFrames);
    }
	
	// Método para dibujar al jugador animado en pantalla
	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		TextureRegion frameActual = caminataAnimacion.getKeyFrame(stateTime, true);
		
		// Usa las coordenadas X e Y guardadas en el objeto Posicion
		batch.draw(frameActual, getPosicion().getX(), getPosicion().getY());
	}
        
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
		
		if(monedas >= monedasPerdidas) {
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