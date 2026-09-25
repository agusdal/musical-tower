package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.escuela_tecnica_n35.entidades.Jugador;
import io.github.escuela_tecnica_n35.entidades.Posicion;
import io.github.escuela_tecnica_n35.juego.Juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.graphics.Texture;

public class Main_game extends ApplicationAdapter {
    
    private SpriteBatch batch;
    
    private Jugador jugador;
    private Juego juego;
    
    private float velocidadY;
    private float aceleracionCaidaRapida;
    private float gravedad;
    private float pisoY;
    
    private boolean mirandoIzquierda = false;
    
    private Texture image;
    
    private ShapeRenderer shapeRenderer;

	 // Tamaño temporal del personaje.
	 private static final float ANCHO_JUGADOR = 80;
	 private static final float ALTO_JUGADOR = 80;
	
	 // Puertas laterales.
	 private Rectangle puertaIzquierda;
	 private Rectangle puertaDerecha;
	
	 // Las dos puertas centrales de tu dibujo.
	 private Rectangle puertaArriba;
	 private Rectangle puertaAbajo;
	
	 // Evita que al entrar a una habitación
	 // volvamos instantáneamente a la anterior.
	 private boolean transicionBloqueada;
    
    @Override
    public void create() {

        batch = new SpriteBatch();

        Posicion posicionInicial = new Posicion(140, 50);

        jugador = new Jugador(
            "KMD",
            posicionInicial,
            100,
            200,
            7
        );
        
        image = new Texture("KMD.jpeg");
        
        juego = new Juego(jugador);
        juego.iniciarJuego();
        
        
        shapeRenderer = new ShapeRenderer();

        puertaIzquierda = new Rectangle(); // Cracion de la puerta izquierda
        puertaDerecha = new Rectangle(); // Cracion de la puerta derecha

        puertaArriba = new Rectangle(); // Cracion de la puerta que sube
        puertaAbajo = new Rectangle(); // Cracion de la puerta que baja
        
        transicionBloqueada = false;
        
        
        velocidadY = 0;
        aceleracionCaidaRapida = 1400;
        gravedad = -800;

        pisoY = 50;
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        boolean seMueve = false;

        // MOVIMIENTO HORIZONTAL
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            jugador.getPosicion().moverX((float) (jugador.getVelocidad() * delta));
            seMueve = true;
            mirandoIzquierda = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            jugador.getPosicion().moverX((float) (-jugador.getVelocidad() * delta));
            seMueve = true;
            mirandoIzquierda = true;
        }
        
        // SALTO
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.W))
                && jugador.getPosicion().getY() == pisoY) {
            velocidadY = 400;
        }

        float velocidadActualY = velocidadY;

        // CAÍDA RÁPIDA Y GRAVEDAD
        if (Gdx.input.isKeyPressed(Input.Keys.S) && jugador.getPosicion().getY() > pisoY) {
            velocidadY -= aceleracionCaidaRapida * delta;
        }

        velocidadY += gravedad * delta;

        // MOVER POSICIÓN
        jugador.getPosicion().moverY(
            velocidadY * delta
        );

        // COLISIÓN CON EL PISO
        boolean enElPiso = false;
        if (jugador.getPosicion().getY() <= pisoY) {
            jugador.getPosicion().setY(pisoY);
            velocidadY = 0;
            velocidadActualY = 0;
            enElPiso = true;
        }

        // EVALUAR Y ACTUALIZAR ESTADO
        jugador.setEstado(seMueve, mirandoIzquierda, enElPiso, velocidadActualY);

        // DIBUJAR
        batch.begin();
        jugador.render(batch, delta);
        batch.end();
        
	     // --------------------------------------------------
	     // ACTUALIZAMOS LAS PUERTAS DE LA HABITACIÓN
	     // --------------------------------------------------
	
	     actualizarPuertas();
	
	
	     // --------------------------------------------------
	     // EVITAMOS QUE EL JUGADOR SALGA DE LA PANTALLA
	     // --------------------------------------------------
	     
	     limitarJugadorAHabitacion();
	
	
	     // --------------------------------------------------
	     // COMPROBAMOS SI ENTRÓ EN UNA PUERTA
	     // --------------------------------------------------
	
	     comprobarCambioSala();
	     
	     dibujarHabitacion();
	     
	     
	     batch.begin();

	     batch.draw(
	         image,
	         jugador.getPosicion().getX(),
	         jugador.getPosicion().getY(),
	         ANCHO_JUGADOR,
	         ALTO_JUGADOR
	     );

	     batch.end();
    }

    @Override
    public void dispose() {

        batch.dispose();

        image.dispose();

        shapeRenderer.dispose();
    }
    
    private void actualizarPuertas() {

        float anchoPantalla =
            Gdx.graphics.getWidth();

        // -----------------------------
        // PUERTAS LATERALES
        // -----------------------------

        float anchoPuertaLateral = 60;
        float altoPuerta = 110;


        puertaIzquierda.set(
            0,
            pisoY,
            anchoPuertaLateral,
            altoPuerta
        );


        puertaDerecha.set(
            anchoPantalla - anchoPuertaLateral,
            pisoY,
            anchoPuertaLateral,
            altoPuerta
        );


        // -----------------------------
        // PUERTAS ARRIBA / ABAJO
        // -----------------------------

        float anchoPuertaCentral = 60;

        float separacion = 10;

        float centroPantalla =
            anchoPantalla / 2;


        // La puerta ARRIBA queda a la izquierda
        // de las dos puertas centrales.
        puertaArriba.set(
            centroPantalla
                - anchoPuertaCentral
                - separacion,

            pisoY,

            anchoPuertaCentral,
            altoPuerta
        );


        // La puerta ABAJO queda a la derecha.
        puertaAbajo.set(
            centroPantalla + separacion,

            pisoY,

            anchoPuertaCentral,
            altoPuerta
        );
    }
    
	 // Comprueba si el centro del jugador se encuentra
	 // dentro de la zona de una puerta.
	 private boolean jugadorEstaEnPuerta(Rectangle puerta) {
	
	     float centroJugadorX =
	         jugador.getPosicion().getX()
	         + ANCHO_JUGADOR / 2;
	
	     float centroJugadorY =
	         jugador.getPosicion().getY()
	         + ALTO_JUGADOR / 2;
	
	     return puerta.contains(
	         centroJugadorX,
	         centroJugadorY
	     );
	 }
	 
	 // Comprueba solamente las puertas laterales.
	 //
	 // Se usa para evitar que al entrar por una puerta lateral
	 // el jugador vuelva instantáneamente a la sala anterior.
	 private boolean estaEnPuertaLateralActiva() {

	     // Puerta izquierda
	     if (juego.haySalaEnDireccion(0, -1)
	         && jugadorEstaEnPuerta(puertaIzquierda)) {

	         return true;
	     }

	     // Puerta derecha
	     if (juego.haySalaEnDireccion(0, 1)
	         && jugadorEstaEnPuerta(puertaDerecha)) {

	         return true;
	     }

	     return false;
	 }
	 
	 private void comprobarCambioSala() {

		    // --------------------------------------------------
		    // BLOQUEO DE PUERTAS LATERALES
		    // --------------------------------------------------

		    // Si acabamos de cambiar por izquierda o derecha,
		    // esperamos a que el jugador se aleje de esa puerta
		    // antes de permitir otra transición lateral.
		    if (transicionBloqueada) {

		        if (!estaEnPuertaLateralActiva()) {
		            transicionBloqueada = false;
		        }

		        return;
		    }


		    // --------------------------------------------------
		    // IZQUIERDA
		    // Funciona simplemente tocando la puerta.
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(0, -1)
		        && jugadorEstaEnPuerta(puertaIzquierda)) {

		        if (juego.cambiarSala(0, -1)) {

		            colocarJugadorTrasCambio(
		                0,
		                -1
		            );

		            // Evitamos volver inmediatamente.
		            transicionBloqueada = true;
		        }

		        return;
		    }


		    // --------------------------------------------------
		    // DERECHA
		    // También funciona por contacto.
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(0, 1)
		        && jugadorEstaEnPuerta(puertaDerecha)) {

		        if (juego.cambiarSala(0, 1)) {

		            colocarJugadorTrasCambio(
		                0,
		                1
		            );

		            transicionBloqueada = true;
		        }

		        return;
		    }


		    // --------------------------------------------------
		    // ARRIBA
		    //
		    // Para usar esta puerta:
		    // 1. Debe existir una sala arriba.
		    // 2. El jugador debe estar frente a la puerta.
		    // 3. Debe presionar F.
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(-1, 0)
		        && jugadorEstaEnPuerta(puertaArriba)
		        && Gdx.input.isKeyJustPressed(Input.Keys.F)) {

		        if (juego.cambiarSala(-1, 0)) {

		            colocarJugadorTrasCambio(
		                -1,
		                0
		            );
		        }

		        return;
		    }


		    // --------------------------------------------------
		    // ABAJO
		    // Funciona igual que ARRIBA pero hacia fila + 1.
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(1, 0)
		        && jugadorEstaEnPuerta(puertaAbajo)
		        && Gdx.input.isKeyJustPressed(Input.Keys.F)) {

		        if (juego.cambiarSala(1, 0)) {

		            colocarJugadorTrasCambio(
		                1,
		                0
		            );
		        }
		    }
		}
	 
	 private void colocarJugadorTrasCambio(
		        int cambioFila,
		        int cambioColumna) {

		    float anchoPantalla =
		        Gdx.graphics.getWidth();


		    // --------------------------------------------------
		    // SALIMOS POR IZQUIERDA
		    //
		    // Entramos a la siguiente habitación
		    // desde su puerta DERECHA.
		    // --------------------------------------------------

		    if (cambioColumna == -1) {

		        jugador.getPosicion().setX(
		            anchoPantalla - ANCHO_JUGADOR
		        );
		    }


		    // --------------------------------------------------
		    // SALIMOS POR DERECHA
		    //
		    // Entramos desde la puerta IZQUIERDA.
		    // --------------------------------------------------

		    else if (cambioColumna == 1) {

		        jugador.getPosicion().setX(0);
		    }


		    // --------------------------------------------------
		    // FUIMOS HACIA ARRIBA
		    //
		    // En la nueva sala aparecemos frente
		    // a la puerta que lleva ABAJO.
		    // --------------------------------------------------

		    else if (cambioFila == -1) {

		        jugador.getPosicion().setX(
		            puertaAbajo.x
		            + puertaAbajo.width / 2
		            - ANCHO_JUGADOR / 2
		        );
		    }


		    // --------------------------------------------------
		    // FUIMOS HACIA ABAJO
		    //
		    // En la nueva sala aparecemos frente
		    // a la puerta que lleva ARRIBA.
		    // --------------------------------------------------

		    else if (cambioFila == 1) {

		        jugador.getPosicion().setX(
		            puertaArriba.x
		            + puertaArriba.width / 2
		            - ANCHO_JUGADOR / 2
		        );
		    }


		    // Por ahora todas las puertas están al nivel del piso.
		    jugador.getPosicion().setY(pisoY);

		    // Cancelamos cualquier salto o caída
		    // que tuviera el personaje.
		    velocidadY = 0;
	 }
	 
	 private void limitarJugadorAHabitacion() {

		    float anchoPantalla =
		        Gdx.graphics.getWidth();

		    float maximoX =
		        anchoPantalla - ANCHO_JUGADOR;


		    // Límite izquierdo
		    if (jugador.getPosicion().getX() < 0) {

		        jugador.getPosicion().setX(0);
		    }


		    // Límite derecho
		    if (jugador.getPosicion().getX() > maximoX) {

		        jugador.getPosicion().setX(maximoX);
		    }
	}
	 
	 private void dibujarHabitacion() {

		    float anchoPantalla =
		        Gdx.graphics.getWidth();

		    float altoPantalla =
		        Gdx.graphics.getHeight();


		    shapeRenderer.begin(
		        ShapeRenderer.ShapeType.Line
		    );

		    shapeRenderer.setColor(Color.WHITE);


		    // --------------------------------------------------
		    // CONTORNO TEMPORAL DE LA HABITACIÓN
		    // --------------------------------------------------

		    shapeRenderer.rect(
		        1,
		        pisoY,
		        anchoPantalla - 2,
		        altoPantalla - pisoY - 2
		    );


		    // --------------------------------------------------
		    // PUERTA IZQUIERDA
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(0, -1)) {

		        shapeRenderer.rect(
		            puertaIzquierda.x,
		            puertaIzquierda.y,
		            puertaIzquierda.width,
		            puertaIzquierda.height
		        );
		    }


		    // --------------------------------------------------
		    // PUERTA DERECHA
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(0, 1)) {

		        shapeRenderer.rect(
		            puertaDerecha.x,
		            puertaDerecha.y,
		            puertaDerecha.width,
		            puertaDerecha.height
		        );
		    }


		    // --------------------------------------------------
		    // PUERTA ARRIBA
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(-1, 0)) {

		        shapeRenderer.rect(
		            puertaArriba.x,
		            puertaArriba.y,
		            puertaArriba.width,
		            puertaArriba.height
		        );

		        dibujarFlechaArriba(
		            puertaArriba
		        );
		    }


		    // --------------------------------------------------
		    // PUERTA ABAJO
		    // --------------------------------------------------

		    if (juego.haySalaEnDireccion(1, 0)) {

		        shapeRenderer.rect(
		            puertaAbajo.x,
		            puertaAbajo.y,
		            puertaAbajo.width,
		            puertaAbajo.height
		        );

		        dibujarFlechaAbajo(
		            puertaAbajo
		        );
		    }


		    shapeRenderer.end();
		}
	 
	 private void dibujarFlechaArriba(
		        Rectangle puerta) {

		    float centroX =
		        puerta.x + puerta.width / 2;

		    float abajo =
		        puerta.y + 25;

		    float arriba =
		        puerta.y + puerta.height - 25;


		    // Línea vertical
		    shapeRenderer.line(
		        centroX,
		        abajo,
		        centroX,
		        arriba
		    );


		    // Punta izquierda
		    shapeRenderer.line(
		        centroX,
		        arriba,
		        centroX - 10,
		        arriba - 15
		    );


		    // Punta derecha
		    shapeRenderer.line(
		        centroX,
		        arriba,
		        centroX + 10,
		        arriba - 15
		    );
		}
	 
	 private void dibujarFlechaAbajo(
		        Rectangle puerta) {

		    float centroX =
		        puerta.x + puerta.width / 2;

		    float arriba =
		        puerta.y + puerta.height - 25;

		    float abajo =
		        puerta.y + 25;


		    // Línea vertical
		    shapeRenderer.line(
		        centroX,
		        arriba,
		        centroX,
		        abajo
		    );


		    // Punta izquierda
		    shapeRenderer.line(
		        centroX,
		        abajo,
		        centroX - 10,
		        abajo + 15
		    );


		    // Punta derecha
		    shapeRenderer.line(
		        centroX,
		        abajo,
		        centroX + 10,
		        abajo + 15
		    );
		}
}