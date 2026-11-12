package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.escuela_tecnica_n35.entidades.Jugador;
import io.github.escuela_tecnica_n35.entidades.Enemigo;
import io.github.escuela_tecnica_n35.entidades.EnemigoVolador;
import io.github.escuela_tecnica_n35.entidades.Jefe;
import io.github.escuela_tecnica_n35.entidades.Hitbox;
import io.github.escuela_tecnica_n35.entidades.Ataque;
import io.github.escuela_tecnica_n35.entidades.Proyectil;
import io.github.escuela_tecnica_n35.entidades.Posicion;
import io.github.escuela_tecnica_n35.entidades.Item;
import io.github.escuela_tecnica_n35.etapas.Plataforma;
import io.github.escuela_tecnica_n35.entidades.OfertaTienda;
import io.github.escuela_tecnica_n35.entidades.EstadoEnemigo;
import io.github.escuela_tecnica_n35.juego.Juego;
import io.github.escuela_tecnica_n35.etapas.GeneracionEtapa;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.escuela_tecnica_n35.etapas.Sala;
import io.github.escuela_tecnica_n35.etapas.TipoSala;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Main_game extends ApplicationAdapter {

    private static final float ANCHO_MUNDO = 1280f;
    private static final float ALTO_MUNDO = 720f;

    private boolean gameOver;

    private OrthographicCamera camara;
    private Viewport viewport;

    private SpriteBatch batch;
    private Jugador jugador;
    private Juego juego;

    private float tiempoInvulnerable;
    private float duracionInvulnerabilidad;

    private BitmapFont font;
    private GlyphLayout layout;

    private Texture texturaMoneda;
    private Texture texturaBicho; // <--- TEXTURA DEL ENEMIGO (Bicho)

    private Texture texturaPlataforma;
    private static final float ALTO_PLATAFORMA_VISUAL = 140f;
    private static final float ALTO_PLATAFORMA = 140f;

    private float velocidadX;
    private float velocidadY;

    private float desaceleracionX;

    private float aceleracionCaidaRapida;
    private float gravedad;
    private float pisoY;

    private Rectangle hitboxAtaqueEnemigoDebug;
    private float tiempoHitboxAtaqueEnemigoDebug;

    private float fuerzaSalto;
    private int saltosDisponibles;
    private float gravedadSaltoCorto;

    private boolean haciendoDash;
    private float tiempoDashRestante;
    private float tiempoCooldownDash;
    private int direccionDash;
    private int dashDisponibleAire;
    private float duracionDash;
    private float cooldownDash;

    private boolean mirandoIzquierda = false;
    private ShapeRenderer shapeRenderer;

    private static final float ANCHO_JUGADOR = 80;
    private static final float ALTO_JUGADOR = 80;

    private static final float ANCHO_HITBOX_JUGADOR = 80f;
    private static final float ALTO_HITBOX_JUGADOR = 150f;

    private static final float ANCHO_HURTBOX_JUGADOR = 60f;
    private static final float ALTO_HURTBOX_JUGADOR = 130f;

    private static final float ANCHO_ENEMIGO = 500f;
    private static final float ALTO_ENEMIGO = 200f;

    private static final float ALCANCE_ATAQUE_JUGADOR = 130f;
    private static final float ALTO_ATAQUE_JUGADOR = 140f;

    private static final float ANCHO_POGO_JUGADOR = 100f;
    private static final float ALTO_POGO_JUGADOR = 80f;

    private static final float FUERZA_RETROCESO_X = 400f;

    private Rectangle hitboxAtaqueDebug;
    private float tiempoHitboxAtaqueDebug;

    private Rectangle puertaIzquierda;
    private Rectangle puertaDerecha;
    private Rectangle puertaArriba;
    private Rectangle puertaAbajo;

    private boolean transicionBloqueada;

    private static final String[] OPCIONES_PAUSA = {
        "Continuar",
        "Ver controles",
        "Reiniciar partida"
    };

    private boolean pausado;
    private boolean viendoControles;
    private int opcionPausa;

    @Override
    public void create() {
        gameOver = false;

        batch = new SpriteBatch();

        Posicion posicionInicial = new Posicion(140, 50);

        jugador = new Jugador("KMD", posicionInicial, 100, 250, 999);

        hitboxAtaqueDebug = new Rectangle();
        tiempoHitboxAtaqueDebug = 0;

        hitboxAtaqueEnemigoDebug = new Rectangle();
        tiempoHitboxAtaqueEnemigoDebug = 0;

        texturaMoneda = new Texture("moneda.png");
        texturaBicho = new Texture("bicho.jpg"); // <--- CARGAMOS LA IMAGEN DEL BICHO
        texturaPlataforma = new Texture("plataforma.jpg");

        tiempoInvulnerable = 0;
        duracionInvulnerabilidad = 0.7f;

        juego = new Juego(jugador);
        juego.iniciarJuego();

        font = new BitmapFont();
        font.getData().setScale(1.2f);

        layout = new GlyphLayout();
        shapeRenderer = new ShapeRenderer();

        camara = new OrthographicCamera();
        viewport = new FitViewport(ANCHO_MUNDO, ALTO_MUNDO, camara);

        camara.position.set(ANCHO_MUNDO / 2, ALTO_MUNDO / 2, 0);
        camara.update();

        batch.setProjectionMatrix(camara.combined);
        shapeRenderer.setProjectionMatrix(camara.combined);

        puertaIzquierda = new Rectangle();
        puertaDerecha = new Rectangle();
        puertaArriba = new Rectangle();
        puertaAbajo = new Rectangle();

        transicionBloqueada = false;

        velocidadY = 0;
        velocidadX = 0;
        desaceleracionX = 1800f;

        aceleracionCaidaRapida = 1400;
        gravedad = -800;
        pisoY = 50;

        fuerzaSalto = 500;
        saltosDisponibles = 2;
        gravedadSaltoCorto = -1600;

        haciendoDash = false;
        dashDisponibleAire = 1;
        tiempoDashRestante = 0;
        tiempoCooldownDash = 0;
        duracionDash = 0.15f;
        cooldownDash = 0.45f;
        direccionDash = 1;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.apply();
        camara.update();

        batch.setProjectionMatrix(camara.combined);
        shapeRenderer.setProjectionMatrix(camara.combined);

        if (gameOver) {
            dibujarGameOver();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                reiniciarPartida();
            }
            return;
        }

        if (pausado || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            actualizarMenuPausa();
            dibujarEscenaPausada();

            if (pausado) {
                dibujarMenuPausa();
            }
            return;
        }

        jugador.actualizarCooldowns(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            jugador.usarPocima();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && juego.avanzarEtapa()) {
            jugador.getPosicion().setX(140f);
            jugador.getPosicion().setY(pisoY);

            velocidadX = 0f;
            velocidadY = 0f;

            haciendoDash = false;
            tiempoDashRestante = 0f;
            dashDisponibleAire = 1;
            saltosDisponibles = 2;

            transicionBloqueada = false;
            tiempoHitboxAtaqueDebug = 0f;
            tiempoHitboxAtaqueEnemigoDebug = 0f;
        }

        boolean seMueve = false;

        if (tiempoCooldownDash > 0) {
            tiempoCooldownDash -= delta;
        }

        if (botonDashRecienPresionado()) {
            iniciarDash();
        }

        Rectangle hitboxJugadorAntesX = obtenerHitboxJugador();

        if (haciendoDash) {
            jugador.getPosicion().moverX(jugador.getVelocidadDash() * direccionDash * delta);
            seMueve = true;

            if (direccionDash == -1) {
                mirandoIzquierda = true;
            } else {
                mirandoIzquierda = false;
            }

            tiempoDashRestante -= delta;

            if (tiempoDashRestante <= 0) {
                haciendoDash = false;
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                jugador.getPosicion().moverX(jugador.getVelocidad() * delta);
                seMueve = true;
                mirandoIzquierda = false;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                jugador.getPosicion().moverX(-jugador.getVelocidad() * delta);
                seMueve = true;
                mirandoIzquierda = true;
            }
        }

        if (!haciendoDash && velocidadX != 0) {
            jugador.getPosicion().moverX(velocidadX * delta);

            if (velocidadX > 0) {
                velocidadX -= desaceleracionX * delta;
                if (velocidadX < 0) {
                    velocidadX = 0;
                }
            } else {
                velocidadX += desaceleracionX * delta;
                if (velocidadX > 0) {
                    velocidadX = 0;
                }
            }
        }

        comprobarColisionHorizontalPlataformas(hitboxJugadorAntesX);

        if (botonSaltoRecienPresionado() && saltosDisponibles > 0) {
            velocidadY = fuerzaSalto;
            saltosDisponibles--;
        }

        float velocidadActualY = velocidadY;
        boolean enElPiso = false;

        if (!haciendoDash) {
            Rectangle hitboxJugadorAnterior = obtenerHitboxJugador();

            if (velocidadY > 0 && !botonSaltoPresionado()) {
                velocidadY += gravedadSaltoCorto * delta;
            }

            velocidadY += gravedad * delta;
            jugador.getPosicion().moverY(velocidadY * delta);

            enElPiso = comprobarColisionPlataformas(hitboxJugadorAnterior);

            if (enElPiso) {
                velocidadActualY = 0;
            }
        }

        if (jugador.getPosicion().getY() <= pisoY) {
            jugador.getPosicion().setY(pisoY);
            velocidadY = 0;
            velocidadActualY = 0;
            enElPiso = true;
        }

        if (enElPiso) {
            saltosDisponibles = 2;
            dashDisponibleAire = 1;
        }

        jugador.setEstado(seMueve, mirandoIzquierda, enElPiso, velocidadActualY);

        comprobarAtaqueJugador();
        comprobarRecogerItem();
        comprobarCompraTienda();
        actualizarPuertas();
        limitarJugadorAHabitacion();
        comprobarCambioSala();

        dibujarHabitacion();
        dibujarEnemigos(); // <--- Dibuja los enemigos con la textura del bicho
        dibujarItems();
        dibujarPlataformas();

        dibujarHitboxAtaque(delta);
        dibujarHitboxAtaqueEnemigo(delta);

        comprobarDañoEnemigos(delta);
        actualizarMovimientoEnemigos(delta);

        actualizarAtaqueJefe();
        actualizarProyectiles(delta);
        dibujarProyectiles();

        batch.begin();
        jugador.render(batch, delta);
        batch.end();

        dibujarHitboxJugador();

        dibujarHUD();
        dibujarAvisoAvanzarEtapa();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
        texturaMoneda.dispose();
        texturaBicho.dispose(); // <--- LIBERAMOS LA TEXTURA DEL BICHO
        texturaPlataforma.dispose();
    }

    private boolean botonSaltoPresionado() {
        return Gdx.input.isKeyPressed(Input.Keys.Z);
    }

    private boolean botonSaltoRecienPresionado() {
        return Gdx.input.isKeyJustPressed(Input.Keys.Z);
    }

    private boolean botonDashRecienPresionado() {
        return Gdx.input.isKeyJustPressed(Input.Keys.C);
    }

    private void iniciarDash() {
        if (tiempoCooldownDash > 0 || haciendoDash) {
            return;
        }

        boolean estaEnElAire = jugador.getPosicion().getY() > pisoY;

        if (estaEnElAire && dashDisponibleAire <= 0) {
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direccionDash = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direccionDash = 1;
        } else {
            if (mirandoIzquierda) {
                direccionDash = -1;
            } else {
                direccionDash = 1;
            }
        }

        haciendoDash = true;
        tiempoDashRestante = duracionDash;
        tiempoCooldownDash = cooldownDash;
        velocidadX = 0;
        velocidadY = 0;

        if (estaEnElAire) {
            dashDisponibleAire--;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private Color obtenerColorSala(Sala sala) {
        if (sala == null) return Color.DARK_GRAY;
        switch (sala.getTipo()) {
            case INICIAL: return Color.GREEN;
            case ENEMIGOS: return Color.LIGHT_GRAY;
            case ITEM: return Color.YELLOW;
            case TIENDA: return Color.BROWN;
            case JEFE: return Color.RED;
        }
        return Color.WHITE;
    }

    private void dibujarNombreEtapa() {
        String nombreEtapa = juego.getEtapaActual().getNombre();
        layout.setText(font, nombreEtapa);
        float posicionX = (ANCHO_MUNDO - layout.width) / 2;
        float posicionY = ALTO_MUNDO - 20;

        batch.begin();
        font.draw(batch, layout, posicionX, posicionY);
        batch.end();
    }

    private void dibujarPocimaHUD() {
        int cargas = jugador.getCargasPocima();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.13f, 0.14f, 0.19f, 1f);
        shapeRenderer.rect(20f, 8f, 250f, 34f);
        shapeRenderer.end();

        batch.begin();
        font.setColor(cargas > 0 ? Color.ORANGE : Color.LIGHT_GRAY);
        font.draw(batch, "[A] Pócima  " + cargas + "/3", 32f, 34f);
        font.setColor(Color.WHITE);
        batch.end();
    }

    private void dibujarBarraVida() {
        float barraX = 20;
        float barraY = ALTO_MUNDO - 35;
        float barraAncho = 240;
        float barraAlto = 18;

        float porcentajeVida = (float) jugador.getVidaActual() / jugador.getVidaMax();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barraX, barraY, barraAncho, barraAlto);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barraX, barraY, barraAncho * porcentajeVida, barraAlto);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(barraX, barraY, barraAncho, barraAlto);
        shapeRenderer.end();
    }

    private void dibujarMonedas() {
        float monedaX = 20;
        float monedaY = ALTO_MUNDO - 80;
        float tamañoMoneda = 40;

        batch.begin();
        batch.draw(texturaMoneda, monedaX, monedaY, tamañoMoneda, tamañoMoneda);
        font.draw(batch, "" + jugador.getMonedas(), monedaX + tamañoMoneda + 8, monedaY + 25);
        batch.end();
    }

    private void dibujarMiniMapa() {
        Sala[][] mapa = juego.getEtapaActual().getMapa();
        Sala salaActual = juego.getSalaActual();

        float tamañoCelda = 16;
        float separacion = 4;
        float anchoMiniMapa = mapa[0].length * (tamañoCelda + separacion);
        float inicioX = ANCHO_MUNDO - anchoMiniMapa - 30;
        float inicioY = ALTO_MUNDO - 30;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int fila = 0; fila < mapa.length; fila++) {
            for (int columna = 0; columna < mapa[fila].length; columna++) {
                Sala sala = mapa[fila][columna];
                if (sala != null) {
                    float x = inicioX + columna * (tamañoCelda + separacion);
                    float y = inicioY - (fila + 1) * (tamañoCelda + separacion);

                    if (sala == salaActual) {
                        shapeRenderer.setColor(Color.WHITE);
                    } else {
                        shapeRenderer.setColor(obtenerColorSala(sala));
                    }
                    shapeRenderer.rect(x, y, tamañoCelda, tamañoCelda);
                }
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int fila = 0; fila < mapa.length; fila++) {
            for (int columna = 0; columna < mapa[fila].length; columna++) {
                Sala sala = mapa[fila][columna];
                if (sala != null) {
                    float x = inicioX + columna * (tamañoCelda + separacion);
                    float y = inicioY - (fila + 1) * (tamañoCelda + separacion);
                    shapeRenderer.rect(x, y, tamañoCelda, tamañoCelda);
                }
            }
        }
        shapeRenderer.end();
    }

    private void dibujarTextoHUD() {
        Sala salaActual = juego.getSalaActual();

        batch.begin();
        font.draw(batch, "Vida: " + jugador.getVidaActual() + "/" + jugador.getVidaMax(), 20, ALTO_MUNDO - 19);
        font.draw(batch, "Sala: " + salaActual.getTipo(), 20, ALTO_MUNDO - 95);
        font.draw(batch, "Posicion: [" + salaActual.getFila() + "][" + salaActual.getColumna() + "]", 20, ALTO_MUNDO - 125);
        font.draw(batch, "Mapa", ANCHO_MUNDO - 110, ALTO_MUNDO - 10);
        batch.end();
    }

    private void dibujarHUD() {
        dibujarBarraVida();
        dibujarMonedas();
        dibujarMiniMapa();
        dibujarTextoHUD();
        dibujarNombreEtapa();
        dibujarPocimaHUD();
    }

    private void dibujarAvisoAvanzarEtapa() {
        if (gameOver || !juego.puedeAvanzarEtapa()) {
            return;
        }

        String mensaje = "Presionar ENTER para avanzar de etapa";
        font.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout(font, mensaje);
        float x = (ANCHO_MUNDO - layout.width) / 2f;

        batch.begin();
        font.draw(batch, layout, x, 40f);
        batch.end();
    }

    private void actualizarMenuPausa() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (viendoControles) {
                viendoControles = false;
            } else {
                pausado = !pausado;
                opcionPausa = 0;
            }
            return;
        }

        if (viendoControles) return;

        int cantidad = OPCIONES_PAUSA.length;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionPausa = (opcionPausa + cantidad - 1) % cantidad;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionPausa = (opcionPausa + 1) % cantidad;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (opcionPausa) {
                case 0: pausado = false; break;
                case 1: viendoControles = true; break;
                case 2: reiniciarPartida(); break;
            }
        }
    }

    private void dibujarEscenaPausada() {
        dibujarHabitacion();
        dibujarEnemigos();
        dibujarItems();
        dibujarPlataformas();
        dibujarProyectiles();

        batch.begin();
        jugador.render(batch, 0f);
        batch.end();

        dibujarHUD();
    }

    private void dibujarMenuPausa() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.55f);
        shapeRenderer.rect(0f, 0f, ANCHO_MUNDO, ALTO_MUNDO);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.08f, 0.09f, 0.13f, 1f);
        shapeRenderer.rect(360f, 155f, 560f, 410f);
        shapeRenderer.end();

        batch.begin();

        if (viendoControles) {
            dibujarTextoCentrado("CONTROLES", 525f, Color.ORANGE);
            dibujarTextoCentrado("Flechas izquierda y derecha: mover", 475f, Color.WHITE);
            dibujarTextoCentrado("Z: saltar / doble salto", 445f, Color.WHITE);
            dibujarTextoCentrado("C: dash", 415f, Color.WHITE);
            dibujarTextoCentrado("X: ataque normal", 385f, Color.WHITE);
            dibujarTextoCentrado("Arriba + X: ataque hacia arriba", 355f, Color.WHITE);
            dibujarTextoCentrado("Abajo + X en el aire: pogo", 325f, Color.WHITE);
            dibujarTextoCentrado("F: usar puertas verticales", 295f, Color.WHITE);
            dibujarTextoCentrado("Q: usar la pócima", 265f, Color.WHITE);
            dibujarTextoCentrado("ENTER: avanzar tras derrotar al jefe", 235f, Color.WHITE);
            dibujarTextoCentrado("ESC: volver al menú", 185f, Color.LIGHT_GRAY);
        } else {
            dibujarTextoCentrado("PAUSA", 505f, Color.ORANGE);

            for (int i = 0; i < OPCIONES_PAUSA.length; i++) {
                boolean seleccionada = i == opcionPausa;
                String texto = seleccionada ? "> " + OPCIONES_PAUSA[i] : OPCIONES_PAUSA[i];
                dibujarTextoCentrado(texto, 420f - i * 55f, seleccionada ? Color.ORANGE : Color.WHITE);
            }

            dibujarTextoCentrado("Arriba / abajo para elegir - ENTER para aceptar", 205f, Color.LIGHT_GRAY);
        }

        font.setColor(Color.WHITE);
        batch.end();
    }

    private void dibujarTextoCentrado(String texto, float y, Color color) {
        font.setColor(color);
        layout.setText(font, texto);
        font.draw(batch, layout, (ANCHO_MUNDO - layout.width) / 2f, y);
    }

    private void actualizarPuertas() {
        float anchoPantalla = ANCHO_MUNDO;
        float anchoPuertaLateral = 80;
        float altoPuerta = 170;

        puertaIzquierda.set(0, pisoY, anchoPuertaLateral, altoPuerta);
        puertaDerecha.set(anchoPantalla - anchoPuertaLateral, pisoY, anchoPuertaLateral, altoPuerta);

        float anchoPuertaCentral = 80;
        float separacion = 30;
        float centroPantalla = anchoPantalla / 2;

        puertaArriba.set(centroPantalla - anchoPuertaCentral - separacion, pisoY, anchoPuertaCentral, altoPuerta);
        puertaAbajo.set(centroPantalla + separacion, pisoY, anchoPuertaCentral, altoPuerta);
    }

    private boolean jugadorEstaEnPuerta(Rectangle puerta) {
        Rectangle cuerpo = obtenerHitboxJugador();
        float centroJugadorX = cuerpo.x + cuerpo.width / 2f;
        float centroJugadorY = cuerpo.y + cuerpo.height / 2f;
        return puerta.contains(centroJugadorX, centroJugadorY);
    }

    private boolean estaEnPuertaLateralActiva() {
        if (juego.haySalaEnDireccion(0, -1) && jugadorEstaEnPuerta(puertaIzquierda)) return true;
        if (juego.haySalaEnDireccion(0, 1) && jugadorEstaEnPuerta(puertaDerecha)) return true;
        return false;
    }

    private void comprobarCambioSala() {
        if (transicionBloqueada) {
            if (!estaEnPuertaLateralActiva()) {
                transicionBloqueada = false;
            }
            return;
        }

        if (juego.haySalaEnDireccion(0, -1) && jugadorEstaEnPuerta(puertaIzquierda)) {
            if (juego.cambiarSala(0, -1)) {
                colocarJugadorTrasCambio(0, -1);
                transicionBloqueada = true;
            }
            return;
        }

        if (juego.haySalaEnDireccion(0, 1) && jugadorEstaEnPuerta(puertaDerecha)) {
            if (juego.cambiarSala(0, 1)) {
                colocarJugadorTrasCambio(0, 1);
                transicionBloqueada = true;
            }
            return;
        }

        if (juego.haySalaEnDireccion(-1, 0) && jugadorEstaEnPuerta(puertaArriba) && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (juego.cambiarSala(-1, 0)) {
                colocarJugadorTrasCambio(-1, 0);
            }
            return;
        }

        if (juego.haySalaEnDireccion(1, 0) && jugadorEstaEnPuerta(puertaAbajo) && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (juego.cambiarSala(1, 0)) {
                colocarJugadorTrasCambio(1, 0);
            }
        }
    }

    private void colocarJugadorTrasCambio(int cambioFila, int cambioColumna) {
        if (cambioColumna == -1) {
            jugador.getPosicion().setX(ANCHO_MUNDO - jugador.getAnchoSprite());
        } else if (cambioColumna == 1) {
            jugador.getPosicion().setX(0f);
        } else if (cambioFila == -1) {
            jugador.getPosicion().setX(puertaAbajo.x + puertaAbajo.width / 2f - jugador.getAnchoSprite() / 2f);
        } else if (cambioFila == 1) {
            jugador.getPosicion().setX(puertaArriba.x + puertaArriba.width / 2f - jugador.getAnchoSprite() / 2f);
        }

        jugador.getPosicion().setY(pisoY);
        velocidadY = 0;
    }

    private void limitarJugadorAHabitacion() {
        float minimoX = 0f;
        float maximoX = ANCHO_MUNDO - jugador.getAnchoSprite();
        float posicionX = jugador.getPosicion().getX();

        if (posicionX < minimoX) {
            jugador.getPosicion().setX(minimoX);
        } else if (posicionX > maximoX) {
            jugador.getPosicion().setX(maximoX);
        }
    }

    private void dibujarHabitacion() {
        float anchoPantalla = ANCHO_MUNDO;
        float altoPantalla = ALTO_MUNDO;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        shapeRenderer.rect(1, pisoY, anchoPantalla - 2, altoPantalla - pisoY - 2);

        if (juego.haySalaEnDireccion(0, -1)) {
            shapeRenderer.rect(puertaIzquierda.x, puertaIzquierda.y, puertaIzquierda.width, puertaIzquierda.height);
        }

        if (juego.haySalaEnDireccion(0, 1)) {
            shapeRenderer.rect(puertaDerecha.x, puertaDerecha.y, puertaDerecha.width, puertaDerecha.height);
        }

        if (juego.haySalaEnDireccion(-1, 0)) {
            shapeRenderer.rect(puertaArriba.x, puertaArriba.y, puertaArriba.width, puertaArriba.height);
            dibujarFlechaArriba(puertaArriba);
        }

        if (juego.haySalaEnDireccion(1, 0)) {
            shapeRenderer.rect(puertaAbajo.x, puertaAbajo.y, puertaAbajo.width, puertaAbajo.height);
            dibujarFlechaAbajo(puertaAbajo);
        }

        shapeRenderer.end();
    }

    private void dibujarFlechaArriba(Rectangle puerta) {
        float centroX = puerta.x + puerta.width / 2;
        float abajo = puerta.y + 25;
        float arriba = puerta.y + puerta.height - 25;

        shapeRenderer.line(centroX, abajo, centroX, arriba);
        shapeRenderer.line(centroX, arriba, centroX - 10, arriba - 15);
        shapeRenderer.line(centroX, arriba, centroX + 10, arriba - 15);
    }

    private void dibujarFlechaAbajo(Rectangle puerta) {
        float centroX = puerta.x + puerta.width / 2;
        float arriba = puerta.y + puerta.height - 25;
        float abajo = puerta.y + 25;

        shapeRenderer.line(centroX, arriba, centroX, abajo);
        shapeRenderer.line(centroX, abajo, centroX - 10, abajo + 15);
        shapeRenderer.line(centroX, abajo, centroX + 10, abajo + 15);
    }

    private Rectangle obtenerHitboxAtaqueJugador() {
        Ataque ataque = jugador.getAtaqueNormal();
        float ataqueX;
        float separacionCentral = 15;
        Rectangle hitboxJugador = obtenerHitboxJugador();
        float centroJugadorX = hitboxJugador.x + hitboxJugador.width / 2;
        float ataqueY = hitboxJugador.y;

        if (mirandoIzquierda) {
            ataqueX = centroJugadorX - ataque.getAncho() - separacionCentral;
        } else {
            ataqueX = centroJugadorX + separacionCentral;
        }

        return new Rectangle(ataqueX, ataqueY, ataque.getAncho(), ataque.getAlto());
    }

    private void actualizarGravedadEnemigo(Enemigo enemigo, float delta) {
        Rectangle cuerpoAnterior = obtenerHitboxEnemigo(enemigo);
        enemigo.actualizarGravedad(delta, gravedad);

        if (enemigo.getVelocidadY() > 0f) {
            return;
        }

        Rectangle cuerpoActual = obtenerHitboxEnemigo(enemigo);
        float superficieApoyo = pisoY;

        for (Plataforma plataforma : juego.getSalaActual().getPlataformas()) {
            float superficiePlataforma = plataforma.getY() + plataforma.getAlto();
            boolean coincideEnX = cuerpoActual.x < plataforma.getX() + plataforma.getAncho()
                && cuerpoActual.x + cuerpoActual.width > plataforma.getX();
            boolean cruzaSuperficie = cuerpoAnterior.y >= superficiePlataforma && cuerpoActual.y <= superficiePlataforma;

            if (coincideEnX && cruzaSuperficie) {
                superficieApoyo = Math.max(superficieApoyo, superficiePlataforma);
            }
        }

        if (cuerpoActual.y <= superficieApoyo) {
            enemigo.getPosicion().moverY(superficieApoyo - cuerpoActual.y);
            enemigo.detenerCaida();
        }
    }

    private Rectangle obtenerHitboxAtaqueEnemigo(Enemigo enemigo) {
        Rectangle cuerpo = obtenerHitboxEnemigo(enemigo);
        if (enemigo.esAtaqueHaciaArriba()) {
            return enemigo.getAtaqueNormal().obtenerHitboxHaciaArriba(cuerpo);
        }
        return enemigo.getAtaqueNormal().obtenerHitboxCuerpoACuerpo(cuerpo, obtenerHitboxJugador());
    }

    private Rectangle obtenerHitboxProyectil(Proyectil proyectil) {
        return new Rectangle(proyectil.getPosicion().getX(), proyectil.getPosicion().getY(), proyectil.getAncho(), proyectil.getAlto());
    }

    private void intentarAtaqueEnemigo(Enemigo enemigo) {
        Ataque ataque = enemigo.getAtaqueNormal();
        if (!ataque.estaDisponible()) {
            return;
        }

        Rectangle hitboxAtaque = obtenerHitboxAtaqueEnemigo(enemigo);
        hitboxAtaqueEnemigoDebug.set(hitboxAtaque.x, hitboxAtaque.y, hitboxAtaque.width, hitboxAtaque.height);
        tiempoHitboxAtaqueEnemigoDebug = 0.15f;

        Rectangle hitboxJugador = obtenerHitboxJugador();
        ataque.iniciarCooldown();

        if (!rectanguloTocaHurtboxJugador(hitboxAtaque)) {
            return;
        }

        if (tiempoInvulnerable > 0) {
            return;
        }

        jugador.recibirDaño(ataque.getDaño());
        tiempoInvulnerable = duracionInvulnerabilidad;

        if (!jugador.estaVivo()) {
            gameOver = true;
        }
    }

    private void actualizarAtaqueJefe() {
        Sala salaActual = juego.getSalaActual();
        if (salaActual.getTipo() != TipoSala.JEFE) {
            return;
        }

        for (Enemigo enemigo : salaActual.getEnemigos()) {
            if (!(enemigo instanceof Jefe)) {
                continue;
            }

            Jefe jefe = (Jefe) enemigo;
            if (!jefe.estaVivo()) {
                continue;
            }

            if (jefe.getEstado() == EstadoEnemigo.ATACAR) {
                continue;
            }

            Ataque ataque = jefe.getAtaqueDistancia();
            if (!ataque.estaDisponible()) {
                continue;
            }

            Rectangle hitboxJefe = obtenerHitboxEnemigo(jefe);
            float origenX = hitboxJefe.x + hitboxJefe.width / 2f - ataque.getAncho() / 2f;
            float origenY = hitboxJefe.y + hitboxJefe.height / 2f - ataque.getAlto() / 2f;

            Rectangle hitboxJugador = obtenerHitboxJugador();
            float objetivoX = hitboxJugador.x + hitboxJugador.width / 2f;
            float objetivoY = hitboxJugador.y + hitboxJugador.height / 2f;

            Proyectil proyectil = new Proyectil(
                new Posicion(origenX, origenY),
                objetivoX, objetivoY,
                300f, ataque.getDaño(), ataque.getAncho(), ataque.getAlto()
            );

            salaActual.agregarProyectil(proyectil);
            ataque.iniciarCooldown();
        }
    }

    private Rectangle obtenerHitboxJugador() {
        float hitboxX = jugador.getSpriteX() + (jugador.getAnchoSprite() - ANCHO_HITBOX_JUGADOR) / 2f;
        float hitboxY = jugador.getSpriteY() + (jugador.getAltoSprite() - ALTO_HITBOX_JUGADOR) / 2f;

        return new Rectangle(hitboxX, hitboxY, ANCHO_HITBOX_JUGADOR, ALTO_HITBOX_JUGADOR);
    }

    private Ellipse obtenerHurtboxJugador() {
        Rectangle hitboxFisica = obtenerHitboxJugador();
        float hurtboxX = hitboxFisica.x + (hitboxFisica.width - ANCHO_HURTBOX_JUGADOR) / 2f;
        float hurtboxY = hitboxFisica.y + (hitboxFisica.height - ALTO_HURTBOX_JUGADOR) / 2f;

        return new Ellipse(hurtboxX, hurtboxY, ANCHO_HURTBOX_JUGADOR, ALTO_HURTBOX_JUGADOR);
    }

    private boolean rectanguloTocaHurtboxJugador(Rectangle rectangulo) {
        Ellipse hurtbox = obtenerHurtboxJugador();
        float centroX = hurtbox.x + hurtbox.width / 2f;
        float centroY = hurtbox.y + hurtbox.height / 2f;
        float radioX = hurtbox.width / 2f;
        float radioY = hurtbox.height / 2f;

        float puntoX = Math.max(rectangulo.x, Math.min(centroX, rectangulo.x + rectangulo.width));
        float puntoY = Math.max(rectangulo.y, Math.min(centroY, rectangulo.y + rectangulo.height));

        float distanciaX = (puntoX - centroX) / radioX;
        float distanciaY = (puntoY - centroY) / radioY;

        return (distanciaX * distanciaX + distanciaY * distanciaY) <= 1f;
    }

    private Rectangle obtenerHitboxEnemigo(Enemigo enemigo) {
        return enemigo.getHitbox().obtenerRectangulo(enemigo.getPosicion());
    }

    private Rectangle obtenerHitboxItem(Item item) {
        return new Rectangle(item.getPosicion().getX(), item.getPosicion().getY(), 40, 40);
    }



    private Enemigo buscarEnemigoGolpeado(Sala sala, Rectangle hitboxAtaque) {
        for (Enemigo enemigo : sala.getEnemigos()) {
            Rectangle hitboxEnemigo = obtenerHitboxEnemigo(enemigo);
            if (hitboxAtaque.overlaps(hitboxEnemigo)) {
                return enemigo;
            }
        }
        return null;
    }

    private Rectangle obtenerHitboxPogoJugador() {
        Ataque pogo = jugador.getAtaquePogo();
        Rectangle hitboxJugador = obtenerHitboxJugador();
        float pogoX = hitboxJugador.x + (hitboxJugador.width - pogo.getAncho()) / 2f;
        float pogoY = hitboxJugador.y - pogo.getAlto();

        return new Rectangle(pogoX, pogoY, pogo.getAncho(), pogo.getAlto());
    }

    private Rectangle obtenerHitboxAtaqueArribaJugador() {
        Ataque ataque = jugador.getAtaqueNormal();
        Rectangle hitboxJugador = obtenerHitboxJugador();
        float ataqueX = hitboxJugador.x + (hitboxJugador.width - ataque.getAncho()) / 2f;
        float ataqueY = hitboxJugador.y + hitboxJugador.height;

        return new Rectangle(ataqueX, ataqueY, ataque.getAncho(), ataque.getAlto());
    }

    private void comprobarAtaqueJugador() {
        if (!Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            return;
        }

        Sala salaActual = juego.getSalaActual();

        if (jugador.getPosicion().getY() > pisoY && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            Ataque pogo = jugador.getAtaquePogo();
            if (!pogo.estaDisponible()) {
                return;
            }
            pogo.iniciarCooldown();

            Rectangle hitboxPogo = obtenerHitboxPogoJugador();
            hitboxAtaqueDebug.set(hitboxPogo.x, hitboxPogo.y, hitboxPogo.width, hitboxPogo.height);
            tiempoHitboxAtaqueDebug = 0.15f;

            Enemigo objetivo = buscarEnemigoGolpeado(salaActual, hitboxPogo);

            if (objetivo != null) {
                saltosDisponibles = 1;
                dashDisponibleAire = 1;
                jugador.atacar(objetivo, jugador.getAtaquePogo());
                velocidadY = jugador.getFuerzaPogo();

                if (!objetivo.estaVivo()) {
                    int monedas = objetivo.generarMonedasDrop();
                    jugador.agarrarMonedas(monedas);
                    salaActual.eliminarEnemigo(objetivo);
                }
            }
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            Ataque ataqueArriba = jugador.getAtaqueNormal();
            if (!ataqueArriba.estaDisponible()) {
                return;
            }

            ataqueArriba.iniciarCooldown();
            Rectangle hitboxAtaqueArriba = obtenerHitboxAtaqueArribaJugador();
            hitboxAtaqueDebug.set(hitboxAtaqueArriba.x, hitboxAtaqueArriba.y, hitboxAtaqueArriba.width, hitboxAtaqueArriba.height);
            tiempoHitboxAtaqueDebug = 0.15f;

            Enemigo objetivo = buscarEnemigoGolpeado(salaActual, hitboxAtaqueArriba);
            if (objetivo == null) {
                return;
            }

            jugador.atacar(objetivo, ataqueArriba);

            if (!objetivo.estaVivo()) {
                int monedas = objetivo.generarMonedasDrop();
                jugador.agarrarMonedas(monedas);
                salaActual.eliminarEnemigo(objetivo);
            }
            return;
        }

        Ataque ataqueNormal = jugador.getAtaqueNormal();
        if (!ataqueNormal.estaDisponible()) {
            return;
        }

        ataqueNormal.iniciarCooldown();
        Rectangle hitboxAtaque = obtenerHitboxAtaqueJugador();

        hitboxAtaqueDebug.set(hitboxAtaque.x, hitboxAtaque.y, hitboxAtaque.width, hitboxAtaque.height);
        tiempoHitboxAtaqueDebug = 0.15f;

        Enemigo objetivo = buscarEnemigoGolpeado(salaActual, hitboxAtaque);
        if (objetivo == null) {
            return;
        }

        jugador.atacar(objetivo, jugador.getAtaqueNormal());
        aplicarRetrocesoAtaque();

        if (!objetivo.estaVivo()) {
            int monedas = objetivo.generarMonedasDrop();
            jugador.agarrarMonedas(monedas);
            salaActual.eliminarEnemigo(objetivo);
        }
    }

    private void aplicarRetrocesoAtaque() {
        if (mirandoIzquierda) {
            velocidadX = FUERZA_RETROCESO_X;
        } else {
            velocidadX = -FUERZA_RETROCESO_X;
        }
    }

    private boolean comprobarColisionPlataformas(Rectangle hitboxAnterior) {
        Rectangle hitboxActual = obtenerHitboxJugador();

        for (Plataforma plataforma : juego.getSalaActual().getPlataformas()) {
            Rectangle hitboxPlataforma = obtenerHitboxPlataforma(plataforma);

            boolean coincideEnX = hitboxActual.x + hitboxActual.width > hitboxPlataforma.x
                && hitboxActual.x < hitboxPlataforma.x + hitboxPlataforma.width;

            if (!coincideEnX) {
                continue;
            }

            float piesAntes = hitboxAnterior.y;
            float cabezaAntes = hitboxAnterior.y + hitboxAnterior.height;
            float piesAhora = hitboxActual.y;
            float cabezaAhora = hitboxActual.y + hitboxActual.height;

            float arribaPlataforma = plataforma.getY() + plataforma.getAlto();
            float abajoPlataforma = plataforma.getY();

            if (velocidadY <= 0 && piesAntes >= arribaPlataforma && piesAhora <= arribaPlataforma) {
                float correccionY = arribaPlataforma - piesAhora;
                jugador.getPosicion().moverY(correccionY);
                velocidadY = 0;
                return true;
            }

            if (velocidadY > 0 && cabezaAntes <= abajoPlataforma && cabezaAhora >= abajoPlataforma) {
                float correccionY = abajoPlataforma - cabezaAhora;
                jugador.getPosicion().moverY(correccionY);
                velocidadY = 0;
                return false;
            }
        }
        return false;
    }

    private void comprobarColisionHorizontalPlataformas(Rectangle hitboxAnterior) {
        Rectangle hitboxActual = obtenerHitboxJugador();

        for (Plataforma plataforma : juego.getSalaActual().getPlataformas()) {
            Rectangle hitboxPlataforma = obtenerHitboxPlataforma(plataforma);

            if (!hitboxActual.overlaps(hitboxPlataforma)) {
                continue;
            }

            boolean veniaDesdeIzquierda = hitboxAnterior.x + hitboxAnterior.width <= hitboxPlataforma.x;

            if (veniaDesdeIzquierda) {
                float correccionX = hitboxPlataforma.x - (hitboxActual.x + hitboxActual.width);
                jugador.getPosicion().moverX(correccionX);
                velocidadX = 0;
                haciendoDash = false;
                tiempoDashRestante = 0;
                return;
            }

            boolean veniaDesdeDerecha = hitboxAnterior.x >= hitboxPlataforma.x + hitboxPlataforma.width;

            if (veniaDesdeDerecha) {
                float correccionX = (hitboxPlataforma.x + hitboxPlataforma.width) - hitboxActual.x;
                jugador.getPosicion().moverX(correccionX);
                velocidadX = 0;
                haciendoDash = false;
                tiempoDashRestante = 0;
                return;
            }
        }
    }

    private void dibujarHitboxJugador() {
        Rectangle hitboxJugador = obtenerHitboxJugador();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(hitboxJugador.x, hitboxJugador.y, hitboxJugador.width, hitboxJugador.height);
        shapeRenderer.end();
    }

    private void dibujarHurtboxJugador() {
        Ellipse hurtbox = obtenerHurtboxJugador();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.ellipse(hurtbox.x, hurtbox.y, hurtbox.width, hurtbox.height);
        shapeRenderer.end();
    }

    // --- NUEVO MÉTODO PARA DIBUJAR AL BICHO USANDO SU SPRITE SHEET ---
    private void dibujarEnemigos() {
        Sala salaActual = juego.getSalaActual();

        batch.begin();

        for (Enemigo enemigo : salaActual.getEnemigos()) {
            Rectangle hitboxEnemigo = obtenerHitboxEnemigo(enemigo);
            Rectangle hitboxJugador = obtenerHitboxJugador();

            boolean jugadorDerecha = hitboxJugador.x >= hitboxEnemigo.x;
            enemigo.actualizarAnimacionYFrame(Gdx.graphics.getDeltaTime(), jugadorDerecha);

            int anchoImg = texturaBicho.getWidth();
            int altoImg = texturaBicho.getHeight();

            // Centramos horizontalmente respecto a la hitbox
            float offsetX = (ANCHO_ENEMIGO - hitboxEnemigo.width) / 2f;

            // Alineamos la parte inferior del sprite exactamente con los pies de la hitbox
            float drawX = hitboxEnemigo.x - offsetX;
            float drawY = hitboxEnemigo.y; // Los pies quedan apoyados en la base física

            batch.draw(
                texturaBicho,
                drawX,
                drawY,
                ANCHO_ENEMIGO,
                ALTO_ENEMIGO,
                enemigo.getSourceX(anchoImg),
                enemigo.getSourceY(altoImg),
                enemigo.getAnchoFrame(anchoImg),
                enemigo.getAltoFrame(altoImg),
                false,
                false
            );
        }

        batch.end();
    }

    private void dibujarItems() {
        Sala salaActual = juego.getSalaActual();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);

        if (salaActual.tieneItem()) {
            Item item = salaActual.getItem();
            Rectangle hitboxItem = obtenerHitboxItem(item);
            shapeRenderer.rect(hitboxItem.x, hitboxItem.y, hitboxItem.width, hitboxItem.height);
        }

        for (OfertaTienda oferta : salaActual.getOfertasTienda()) {
            Item item = oferta.getItem();
            Rectangle hitboxItem = obtenerHitboxItem(item);
            shapeRenderer.rect(hitboxItem.x, hitboxItem.y, hitboxItem.width, hitboxItem.height);
        }

        shapeRenderer.end();
    }

    private Rectangle obtenerHitboxPlataforma(Plataforma plataforma) {
        float altoFisico = 15f;
        float ySuperficie = plataforma.getY() + plataforma.getAlto();

        // --- OFFSET VERTICAL UNIFICADO ---
        // Sube o baja este número para mover TODO (hitbox e imagen juntos)
        // hasta que la superficie de la piedra toque los pies del personaje.
        float offsetY = 60f; // <--- Sube o baja este valor según necesites

        float yFisica = (ySuperficie - altoFisico) + offsetY;

        return new Rectangle(
            plataforma.getX(),
            yFisica,
            plataforma.getAncho(),
            altoFisico
        );
    }

    private void dibujarPlataformas() {
        batch.begin();

        for (Plataforma plataforma : juego.getSalaActual().getPlataformas()) {
            float ySuperficie = plataforma.getY() + plataforma.getAlto();
            float altoVisual = 140f;
            float anchoVisual = plataforma.getAncho();

            // Usamos EXACTAMENTE el mismo offsetY que la física para que vayan siempre pegadas
            float offsetY = 60f;

            float yVisual = (ySuperficie - altoVisual) + offsetY;

            batch.draw(
                texturaPlataforma,
                plataforma.getX(),
                yVisual,
                anchoVisual,
                altoVisual
            );
        }

        batch.end();
    }

    private void dibujarHitboxAtaque(float delta) {
        if (tiempoHitboxAtaqueDebug <= 0) {
            return;
        }

        tiempoHitboxAtaqueDebug -= delta;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.7f, 1f, 0.25f);
        shapeRenderer.rect(hitboxAtaqueDebug.x, hitboxAtaqueDebug.y, hitboxAtaqueDebug.width, hitboxAtaqueDebug.height);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void dibujarHitboxAtaqueEnemigo(float delta) {
        if (tiempoHitboxAtaqueEnemigoDebug <= 0) {
            return;
        }

        tiempoHitboxAtaqueEnemigoDebug -= delta;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hitboxAtaqueEnemigoDebug.x, hitboxAtaqueEnemigoDebug.y, hitboxAtaqueEnemigoDebug.width, hitboxAtaqueEnemigoDebug.height);
        shapeRenderer.end();
    }

    private void dibujarProyectiles() {
        Sala salaActual = juego.getSalaActual();
        if (salaActual.getProyectiles().isEmpty()) {
            return;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE);

        for (Proyectil proyectil : salaActual.getProyectiles()) {
            Rectangle hitbox = obtenerHitboxProyectil(proyectil);
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        shapeRenderer.end();
    }

    private boolean jugadorTocaEnemigo(Enemigo enemigo) {
        Rectangle hitboxEnemigo = obtenerHitboxEnemigo(enemigo);
        return rectanguloTocaHurtboxJugador(hitboxEnemigo);
    }

    private void comprobarDañoEnemigos(float delta) {
        if (tiempoInvulnerable > 0) {
            tiempoInvulnerable -= delta;
        }

        if (tiempoInvulnerable > 0) {
            return;
        }

        Sala salaActual = juego.getSalaActual();

        for (Enemigo enemigo : salaActual.getEnemigos()) {
            if (jugadorTocaEnemigo(enemigo)) {
                jugador.recibirDaño(enemigo.getDaño());

                if (!jugador.estaVivo()) {
                    gameOver = true;
                }

                tiempoInvulnerable = duracionInvulnerabilidad;
                break;
            }
        }
    }

    private void actualizarMovimientoEnemigos(float delta) {
        Sala salaActual = juego.getSalaActual();

        for (Enemigo enemigo : salaActual.getEnemigos()) {
            enemigo.actualizarCooldowns(delta);

            if (!enemigo.estaVivo()) {
                continue;
            }

            if (enemigo instanceof EnemigoVolador) {
                EnemigoVolador enemigoVolador = (EnemigoVolador) enemigo;
                enemigoVolador.perseguir(jugador.getPosicion(), delta);
                continue;
            }

            Rectangle hitboxJugador = obtenerHitboxJugador();
            Rectangle hitboxEnemigo = obtenerHitboxEnemigo(enemigo);

            float margenAlturaAtaque = 20f;
            boolean mismaAltura = Math.abs(hitboxJugador.y - hitboxEnemigo.y) <= margenAlturaAtaque;

            float centroJugadorX = hitboxJugador.x + hitboxJugador.width / 2f;
            float centroEnemigoX = hitboxEnemigo.x + hitboxEnemigo.width / 2f;
            boolean jugadorALaDerecha = centroJugadorX >= centroEnemigoX;

            float distanciaX;
            if (jugadorALaDerecha) {
                distanciaX = hitboxJugador.x - (hitboxEnemigo.x + hitboxEnemigo.width);
            } else {
                distanciaX = hitboxEnemigo.x - (hitboxJugador.x + hitboxJugador.width);
            }

            if (distanciaX < 0) {
                distanciaX = 0;
            }

            float rangoDeteccion = 500f;
            float rangoPreparacion = enemigo.getAtaqueNormal().getAncho();
            float margenAtaque = 20f;
            float rangoAtaqueEfectivo = Math.max(0f, rangoPreparacion - margenAtaque);

            if (enemigo.getEstado() != EstadoEnemigo.ATACAR) {
                boolean esJefeFinal = juego.getEtapaActual().getNumeroPiso() == GeneracionEtapa.ULTIMO_PISO
                    && enemigo == juego.getEtapaActual().getJefe();

                boolean puedeAtacarHaciaArriba = false;

                if (esJefeFinal) {
                    Rectangle golpeArriba = enemigo.getAtaqueNormal().obtenerHitboxHaciaArriba(hitboxEnemigo);
                    boolean encimaDelCuerpo = hitboxJugador.x < hitboxEnemigo.x + hitboxEnemigo.width
                        && hitboxJugador.x + hitboxJugador.width > hitboxEnemigo.x;
                    float alturaCabeza = hitboxEnemigo.y + hitboxEnemigo.height;

                    puedeAtacarHaciaArriba = encimaDelCuerpo
                        && hitboxJugador.y >= alturaCabeza - margenAlturaAtaque
                        && golpeArriba.overlaps(hitboxJugador);
                }

                boolean puedeAtacarDeCostado = mismaAltura && distanciaX <= rangoAtaqueEfectivo;

                if (puedeAtacarHaciaArriba || puedeAtacarDeCostado) {
                    if (enemigo.getAtaqueNormal().estaDisponible()) {
                        enemigo.iniciarAtaque(puedeAtacarHaciaArriba);
                    } else {
                        enemigo.setEstado(EstadoEnemigo.QUIETO);
                    }
                } else if (distanciaX <= rangoDeteccion) {
                    enemigo.setEstado(EstadoEnemigo.PERSEGUIR);
                } else {
                    enemigo.setEstado(EstadoEnemigo.QUIETO);
                }
            }

            switch (enemigo.getEstado()) {
                case QUIETO:
                    break;

                case PERSEGUIR:
                    if (jugadorALaDerecha) {
                        enemigo.getPosicion().moverX(enemigo.getVelocidad() * delta);
                    } else {
                        enemigo.getPosicion().moverX(-enemigo.getVelocidad() * delta);
                    }
                    break;

                case ATACAR:
                    if (enemigo.actualizarSecuenciaAtaque(delta)) {
                        intentarAtaqueEnemigo(enemigo);
                    }
                    break;
            }

            actualizarGravedadEnemigo(enemigo, delta);
        }
    }

    private void actualizarProyectiles(float delta) {
        Sala salaActual = juego.getSalaActual();
        ArrayList<Proyectil> proyectiles = salaActual.getProyectiles();

        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            Proyectil proyectil = proyectiles.get(i);
            proyectil.actualizar(delta);

            Rectangle hitboxProyectil = obtenerHitboxProyectil(proyectil);

            boolean fueraDePantalla = hitboxProyectil.x + hitboxProyectil.width < 0
                || hitboxProyectil.x > ANCHO_MUNDO
                || hitboxProyectil.y + hitboxProyectil.height < 0
                || hitboxProyectil.y > ALTO_MUNDO;

            if (fueraDePantalla || !proyectil.estaActivo()) {
                proyectiles.remove(i);
                continue;
            }

            Rectangle hitboxJugador = obtenerHitboxJugador();

            if (!rectanguloTocaHurtboxJugador(hitboxProyectil)) {
                continue;
            }

            proyectil.destruir();
            proyectiles.remove(i);

            if (tiempoInvulnerable > 0) {
                continue;
            }

            jugador.recibirDaño(proyectil.getDaño());
            tiempoInvulnerable = duracionInvulnerabilidad;

            if (!jugador.estaVivo()) {
                gameOver = true;
            }
        }
    }

    private void comprobarRecogerItem() {
        Sala salaActual = juego.getSalaActual();
        if (!salaActual.tieneItem()) {
            return;
        }

        Item item = salaActual.getItem();
        Rectangle hitboxJugador = obtenerHitboxJugador();
        Rectangle hitboxItem = obtenerHitboxItem(item);

        boolean jugadorALaAlturaDelItem = hitboxJugador.y >= hitboxItem.y - 5;

        if (hitboxJugador.overlaps(hitboxItem) && jugadorALaAlturaDelItem) {
            jugador.recogerItem(item);
            salaActual.eliminarItem();
        }
    }

    private void comprobarCompraTienda() {
        Sala salaActual = juego.getSalaActual();
        if (salaActual.getTipo() != TipoSala.TIENDA) {
            return;
        }

        Rectangle hitboxJugador = obtenerHitboxJugador();
        ArrayList<OfertaTienda> ofertas = salaActual.getOfertasTienda();

        for (int i = 0; i < ofertas.size(); i++) {
            OfertaTienda oferta = ofertas.get(i);
            Item item = oferta.getItem();
            Rectangle hitboxItem = obtenerHitboxItem(item);

            if (!hitboxJugador.overlaps(hitboxItem)) {
                continue;
            }

            boolean pudoComprar = jugador.gastarMonedas(oferta.getPrecio());
            if (!pudoComprar) {
                return;
            }

            jugador.recogerItem(item);
            ofertas.remove(i);
            return;
        }
    }

    private void dibujarGameOver() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, ANCHO_MUNDO, ALTO_MUNDO);
        shapeRenderer.end();

        batch.begin();
        font.getData().setScale(3f);
        layout.setText(font, "GAME OVER");
        float gameOverX = (ANCHO_MUNDO - layout.width) / 2;
        float gameOverY = ALTO_MUNDO / 2 + 50;
        font.draw(batch, layout, gameOverX, gameOverY);

        font.getData().setScale(1.5f);
        layout.setText(font, "Presiona ENTER para volver a jugar");
        float reiniciarX = (ANCHO_MUNDO - layout.width) / 2;
        float reiniciarY = ALTO_MUNDO / 2 - 30;
        font.draw(batch, layout, reiniciarX, reiniciarY);

        font.getData().setScale(1.2f);
        batch.end();
    }

    private void reiniciarPartida() {
        jugador.reiniciarJugador(140, pisoY, 100);

        juego = new Juego(jugador);
        juego.iniciarJuego();

        velocidadX = 0;
        velocidadY = 0;
        tiempoInvulnerable = 0;
        saltosDisponibles = 2;
        haciendoDash = false;
        dashDisponibleAire = 1;
        tiempoDashRestante = 0;
        tiempoCooldownDash = 0;
        direccionDash = 1;
        mirandoIzquierda = false;
        transicionBloqueada = false;
        gameOver = false;
        pausado = false;
        viendoControles = false;
        opcionPausa = 0;
    }
}
