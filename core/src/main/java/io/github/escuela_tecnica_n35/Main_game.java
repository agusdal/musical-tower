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

import com.badlogic.gdx.graphics.g2d.BitmapFont; // IMPORTA MAPA
import io.github.escuela_tecnica_n35.etapas.Sala;
import io.github.escuela_tecnica_n35.etapas.TipoSala; // IMPORTA PARA EL MAPA

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport; // IMPORTS PARA LA PANTALLA
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Main_game extends ApplicationAdapter {
    
	private static final float ANCHO_MUNDO = 1280f;
	private static final float ALTO_MUNDO = 720f;

	private OrthographicCamera camara;
	private Viewport viewport;
	
	
    private SpriteBatch batch;
    
    private Jugador jugador;
    private Juego juego;
    private BitmapFont font; // MAPA
    private GlyphLayout layout;
    
    private float velocidadY;
    private float aceleracionCaidaRapida;
    private float gravedad;
    private float pisoY;
    
	// -----------------------------------------
	// SALTO
	// -----------------------------------------
	
	// Velocidad inicial de cada salto.
	private float fuerzaSalto;
	
	// Cantidad de saltos disponibles.
	// 2 = salto normal + doble salto.
	private int saltosDisponibles;
	
	// Gravedad extra que se aplica cuando
	// soltamos el botón antes de llegar al punto máximo.
	private float gravedadSaltoCorto;
	
	
	// -----------------------------------------
	// DASH
	// -----------------------------------------
	
	private boolean haciendoDash;
	
	private float tiempoDashRestante;
	private float tiempoCooldownDash;
	
	private int direccionDash;
	
	// Solo puede dashear una vez en el aire
	private int dashDisponibleAire;
	
	private float velocidadDash;
	private float duracionDash;
	private float cooldownDash;
    
    
    private boolean mirandoIzquierda = false;
    
    
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
            250,
            7
        );
        
        juego = new Juego(jugador);
        juego.iniciarJuego();
        
        font = new BitmapFont();
        font.getData().setScale(1.2f); // Hace el texto del mapa un poco más grande
        
        layout = new GlyphLayout();
        
        
        shapeRenderer = new ShapeRenderer();
        
	     // --------------------------------------------------
	     // CÁMARA Y RESOLUCIÓN VIRTUAL
	     // --------------------------------------------------
	
	     camara = new OrthographicCamera();
	
	     viewport = new FitViewport(
	         ANCHO_MUNDO,
	         ALTO_MUNDO,
	         camara
	     );
	
	     // Centramos la cámara en nuestro mundo.
	     camara.position.set(
	         ANCHO_MUNDO / 2,
	         ALTO_MUNDO / 2,
	         0
	     );
	
	     camara.update();
	
	     batch.setProjectionMatrix(camara.combined);
	     shapeRenderer.setProjectionMatrix(camara.combined);
	     
	     // --------------------------------------------------
	     // CÁMARA Y RESOLUCIÓN VIRTUAL
	     // --------------------------------------------------

        puertaIzquierda = new Rectangle(); // Cracion de la puerta izquierda
        puertaDerecha = new Rectangle(); // Cracion de la puerta derecha

        puertaArriba = new Rectangle(); // Cracion de la puerta que sube
        puertaAbajo = new Rectangle(); // Cracion de la puerta que baja
        
        transicionBloqueada = false;
        
        
        velocidadY = 0;
        aceleracionCaidaRapida = 1400;
        gravedad = -800;

        pisoY = 50;
        
	    // -----------------------------------------
	    // SALTO
	    // -----------------------------------------
	
	    fuerzaSalto = 500;
	
	    // Al comenzar tenemos salto normal
	    // y doble salto disponibles.
	    saltosDisponibles = 2;
	
	    // PROVISIONAL:
	    // Hace que al soltar el botón durante la subida,
	    // el personaje pierda velocidad vertical rápidamente.
	    gravedadSaltoCorto = -1600;
	
	
	    // -----------------------------------------
	    // DASH
	    // -----------------------------------------
	
	    haciendoDash = false;
	    
	    dashDisponibleAire = 1;
	    
	    tiempoDashRestante = 0;
	    tiempoCooldownDash = 0;
	
	    // PROVISIONALES:
	    // Después podemos ajustar estos valores
	    // según cómo se sienta el movimiento.
	    velocidadDash = 900;
	    duracionDash = 0.15f;
	    cooldownDash = 0.45f;
	
	    direccionDash = 1;
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        // Aplicamos la resolución virtual.
        viewport.apply();

        camara.update();

        batch.setProjectionMatrix(
            camara.combined
        );

        shapeRenderer.setProjectionMatrix(
            camara.combined
        );
        

        boolean seMueve = false;

	    // --------------------------------------------------
	    // DASH
	    // --------------------------------------------------
	
	    // Reducimos el cooldown con el paso del tiempo.
	    if (tiempoCooldownDash > 0) {
	
	        tiempoCooldownDash -= delta;
	    }
	    
		// Intentamos iniciar el dash.
		// El propio método comprobará si está permitido.
		if (botonDashRecienPresionado()) {
	
		    iniciarDash();
		}
	    
	    // --------------------------------------------------
	    // MOVIMIENTO HORIZONTAL
	    // --------------------------------------------------
	
	    if (haciendoDash) {
	
	        // Durante el dash ignoramos momentáneamente
	        // la velocidad normal del jugador.
	        jugador.getPosicion().moverX(
	            velocidadDash
	            * direccionDash
	            * delta
	        );
	
	        seMueve = true;
	
	
	        // Actualizamos hacia dónde mira.
	        if (direccionDash == -1) {
	            mirandoIzquierda = true;
	        }
	        else {
	            mirandoIzquierda = false;
	        }
	
	
	        // Restamos el tiempo que dura el dash.
	        tiempoDashRestante -= delta;
	
	
	        // Cuando se acaba el tiempo,
	        // volvemos al movimiento normal.
	        if (tiempoDashRestante <= 0) {
	
	            haciendoDash = false;
	        }
	    }
	    else {
	
	        // -----------------------------------------
	        // MOVIMIENTO NORMAL
	        // -----------------------------------------
	
	        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
	
	            jugador.getPosicion().moverX(
	                jugador.getVelocidad() * delta
	            );
	
	            seMueve = true;
	
	            mirandoIzquierda = false;
	        }
	
	
	        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
	
	            jugador.getPosicion().moverX(
	                -jugador.getVelocidad() * delta
	            );
	
	            seMueve = true;
	
	            mirandoIzquierda = true;
	        }
	    }
        
	    // --------------------------------------------------
	    // SALTO + DOBLE SALTO
	    // --------------------------------------------------
	
	    if (botonSaltoRecienPresionado()
	            && saltosDisponibles > 0) {
	
	        // Cada salto vuelve a darle al personaje
	        // una velocidad vertical hacia arriba.
	        velocidadY = fuerzaSalto;
	
	        // Consumimos uno de los saltos.
	        saltosDisponibles--;
	    }
	    
		// --------------------------------------------------
		// SALTO VARIABLE
		// --------------------------------------------------
	    
	    float velocidadActualY = velocidadY;
	    
	    // Si NO estamos haciendo el dash... (para que sea estatico durante el mismo)
	    if (!haciendoDash) {
	    
			// Si todavía estamos subiendo...
			if (velocidadY > 0
		
			        // ...pero el jugador ya soltó W/SPACE...
			        && !botonSaltoPresionado()) {
		
			    // ...aplicamos gravedad adicional.
			    //
			    // Esto corta antes la subida y genera
			    // un salto más bajo.
			    velocidadY += gravedadSaltoCorto * delta;
			}
			
	        // CAÍDA RÁPIDA
	        if (Gdx.input.isKeyPressed(Input.Keys.S) && jugador.getPosicion().getY() > pisoY) {
	            velocidadY -= aceleracionCaidaRapida * delta;
	        }
	        
	        // GRAVEDAD
	        velocidadY += gravedad * delta;
	
	        // MOVER POSICIÓN
	        jugador.getPosicion().moverY(
	            velocidadY * delta
	        );
        
	    }

        // COLISIÓN CON EL PISO
        boolean enElPiso = false;
        if (jugador.getPosicion().getY() <= pisoY) {

            jugador.getPosicion().setY(pisoY);

            velocidadY = 0;
            velocidadActualY = 0;

            enElPiso = true;

            // Recuperamos salto normal + doble salto
            // al volver a tocar el piso.
            saltosDisponibles = 2;
            
            //Recuperamos el dash en el aire
            dashDisponibleAire = 1;
        }

        // EVALUAR Y ACTUALIZAR ESTADO
        jugador.setEstado(seMueve, mirandoIzquierda, enElPiso, velocidadActualY);

	     // --------------------------------------------------
	     // ACTUALIZAMOS LAS PUERTAS
	     // --------------------------------------------------
	
	     actualizarPuertas();
	
	
	     // --------------------------------------------------
	     // LIMITAMOS AL JUGADOR A LA HABITACIÓN
	     // --------------------------------------------------
	
	     limitarJugadorAHabitacion();
	
	
	     // --------------------------------------------------
	     // COMPROBAMOS CAMBIO DE SALA
	     // --------------------------------------------------
	
	     comprobarCambioSala();
	
	
	     // --------------------------------------------------
	     // DIBUJAMOS LA HABITACIÓN PRIMERO
	     // --------------------------------------------------
	
	     dibujarHabitacion();
	
	
	     // --------------------------------------------------
	     // DIBUJAMOS EL PERSONAJE ANIMADO
	     // --------------------------------------------------
	
	     batch.begin();
	
	     jugador.render(batch, delta);
	
	     batch.end();
	
	
	     // --------------------------------------------------
	     // DIBUJAMOS EL HUD AL FINAL
	     //
	     // De esta forma siempre queda por encima
	     // del escenario y del personaje.
	     // --------------------------------------------------
	
	     dibujarHUD();
	     
    }

    @Override
    public void dispose() {

        batch.dispose();
        
        font.dispose();
        
        shapeRenderer.dispose();
    }
    
    private boolean botonSaltoPresionado() {

        return Gdx.input.isKeyPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyPressed(Input.Keys.W);
    }


    private boolean botonSaltoRecienPresionado() {

        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyJustPressed(Input.Keys.W);
    }
    
    private boolean botonDashRecienPresionado() {

        return Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT);
    }
    
    private void iniciarDash() {

        // --------------------------------------------------
        // COMPROBACIONES
        // --------------------------------------------------

        // Si todavía está en cooldown,
        // no podemos hacer otro dash.
        if (tiempoCooldownDash > 0) {
            return;
        }

        // Si ya estamos haciendo dash,
        // tampoco comenzamos otro.
        if (haciendoDash) {
            return;
        }


        // Comprobamos si estamos en el aire.
        boolean estaEnElAire =
            jugador.getPosicion().getY() > pisoY;


        // Si estamos en el aire y ya usamos
        // nuestro único dash aéreo, no hacemos nada.
        if (estaEnElAire && dashDisponibleAire <= 0) {
            return;
        }


        // --------------------------------------------------
        // DIRECCIÓN DEL DASH
        // --------------------------------------------------

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {

            direccionDash = -1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            direccionDash = 1;
        }
        else {

            if (mirandoIzquierda) {
                direccionDash = -1;
            }
            else {
                direccionDash = 1;
            }
        }


        // --------------------------------------------------
        // INICIAMOS EL DASH
        // --------------------------------------------------

        haciendoDash = true;

        tiempoDashRestante = duracionDash;

        tiempoCooldownDash = cooldownDash;

        // El dash detiene momentáneamente
        // el movimiento vertical.
        velocidadY = 0;


        // --------------------------------------------------
        // CONSUMIMOS EL DASH AÉREO
        // --------------------------------------------------

        // Solamente gastamos el dash aéreo
        // si realmente estamos en el aire.
        if (estaEnElAire) {

            dashDisponibleAire--;
        }
    }
    
    // ---------------------------------------- TAMAÑO PANTALLA -----------------------------------------------
    
    @Override
    public void resize(int width, int height) {

        // Adapta nuestro mundo 1280x720
        // al tamaño real de la ventana.
        viewport.update(
            width,
            height,
            true
        );
    }
    
    // ---------------------------------------- TAMAÑO PANTALLA -----------------------------------------------
    
    // ----------------------------------------- ELEMENTOS DEL HUD --------------------------------------------
    
    private Color obtenerColorSala(Sala sala) {

        if (sala == null) {
            return Color.DARK_GRAY;
        }

        switch (sala.getTipo()) {

            case INICIAL:
                return Color.GREEN;

            case ENEMIGOS:
                return Color.LIGHT_GRAY; // COLORES PREDETERMINADOS PARA CADA SALA EN EL MAPA

            case ITEM:
                return Color.YELLOW;

            case TIENDA:
                return Color.BROWN;

            case JEFE:
                return Color.RED;
        }

        return Color.WHITE;
    }
    
    private void dibujarNombreEtapa() {

        // Obtenemos el nombre de la etapa que estamos jugando actualmente.
        String nombreEtapa =
            juego.getEtapaActual().getNombre();

        // Calculamos cuánto ocupa el texto.
        layout.setText(
            font,
            nombreEtapa
        );

        // Calculamos la posición X necesaria
        // para que quede perfectamente centrado.
        float posicionX =
            (ANCHO_MUNDO - layout.width) / 2;

        // Lo colocamos cerca del borde superior.
        float posicionY =
            ALTO_MUNDO - 20;

        batch.begin();

        font.draw(
            batch,
            layout,
            posicionX,
            posicionY
        );

        batch.end();
    }
    
    private void dibujarBarraVida() {

        float barraX = 20;
        float barraY = ALTO_MUNDO - 35;
        float barraAncho = 180;
        float barraAlto = 18;

        float porcentajeVida =
            (float) jugador.getVidaActual() / jugador.getVidaMax();

        // Fondo de la barra
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(
            barraX,
            barraY,
            barraAncho,
            barraAlto
        );

        // Vida actual
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(
            barraX,
            barraY,
            barraAncho * porcentajeVida,
            barraAlto
        );

        shapeRenderer.end();

        // Borde
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
            barraX,
            barraY,
            barraAncho,
            barraAlto
        );

        shapeRenderer.end();
    }
    
    private void dibujarMiniMapa() {

        Sala[][] mapa = juego.getEtapaActual().getMapa();
        Sala salaActual = juego.getSalaActual();

        float tamañoCelda = 16;
        float separacion = 4;

        float anchoMiniMapa =
            mapa[0].length * (tamañoCelda + separacion);

        float inicioX =
        	ANCHO_MUNDO - anchoMiniMapa - 30;

        float inicioY =
            ALTO_MUNDO - 30;

        // ---------------------------
        // CUADRADOS DEL MAPA
        // ---------------------------
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int fila = 0; fila < mapa.length; fila++) {

            for (int columna = 0; columna < mapa[fila].length; columna++) {

                Sala sala = mapa[fila][columna];

                if (sala != null) {

                    float x =
                        inicioX + columna * (tamañoCelda + separacion);

                    float y =
                        inicioY - (fila + 1) * (tamañoCelda + separacion);

                    // Si es la sala actual del jugador,
                    // la pintamos blanca para destacar su posición.
                    if (sala == salaActual) {
                        shapeRenderer.setColor(Color.WHITE);
                    } else {
                        shapeRenderer.setColor(obtenerColorSala(sala));
                    }

                    shapeRenderer.rect(
                        x,
                        y,
                        tamañoCelda,
                        tamañoCelda
                    );
                }
            }
        }

        shapeRenderer.end();

        // ---------------------------
        // BORDES DEL MAPA
        // ---------------------------
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.BLACK);

        for (int fila = 0; fila < mapa.length; fila++) {

            for (int columna = 0; columna < mapa[fila].length; columna++) {

                Sala sala = mapa[fila][columna];

                if (sala != null) {

                    float x =
                        inicioX + columna * (tamañoCelda + separacion);

                    float y =
                        inicioY - (fila + 1) * (tamañoCelda + separacion);

                    shapeRenderer.rect(
                        x,
                        y,
                        tamañoCelda,
                        tamañoCelda
                    );
                }
            }
        }

        shapeRenderer.end();
    }
    
    private void dibujarTextoHUD() {

        Sala salaActual = juego.getSalaActual();

        batch.begin();

        // Vida numérica
        font.draw(
            batch,
            "Vida: " + jugador.getVidaActual() + "/" + jugador.getVidaMax(),
            20,
            ALTO_MUNDO - 10
        );

        // Tipo de sala actual
        font.draw(
            batch,
            "Sala: " + salaActual.getTipo(),
            20,
            ALTO_MUNDO - 50
        );

        // Posición en el mapa
        font.draw(
            batch,
            "Posicion: [" + salaActual.getFila() + "][" + salaActual.getColumna() + "]",
            20,
            ALTO_MUNDO - 80
        );

        // Título del minimapa
        font.draw(
            batch,
            "Mapa",
            ANCHO_MUNDO - 110,
            ALTO_MUNDO - 10
        );

        batch.end();
    }
    
    private void dibujarHUD() {

        dibujarBarraVida();
        dibujarMiniMapa();
        dibujarTextoHUD();
        dibujarNombreEtapa();
    }
    
    // ----------------------------------------- ELEMENTOS DEL HUD --------------------------------------------
    
    private void actualizarPuertas() {

    	float anchoPantalla =
    		    ANCHO_MUNDO;

        // -----------------------------
        // PUERTAS LATERALES
        // -----------------------------

        float anchoPuertaLateral = 80;
        float altoPuerta = 170;


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

        float anchoPuertaCentral = 80;

        float separacion = 30;

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

		 	float anchoPantalla = ANCHO_MUNDO;
		 	
		 	
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

		 	float anchoPantalla = ANCHO_MUNDO;
		 	
		 	
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
				    ANCHO_MUNDO;

			float altoPantalla =
				    ALTO_MUNDO;


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