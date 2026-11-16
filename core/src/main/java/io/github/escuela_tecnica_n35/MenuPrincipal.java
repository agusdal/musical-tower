package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuPrincipal implements Disposable {

    private enum Pantalla {
        PRINCIPAL,
        CONFIGURACION,
        CONTROLES,
        CREDITOS
    }

    private final BitmapFont fuente;

    private final Rectangle[] botones = new Rectangle[4];
    private final MouseMenu mouseMenu = new MouseMenu();

    private Texture fondoTexture;
    private final TextureRegion[] regionesFondo = new TextureRegion[6];
    private final Texture logo;
    private final Texture selector;

    private Pantalla pantalla = Pantalla.PRINCIPAL;
    private int seleccion;

    private int anchoVentana;
    private int altoVentana;

    private final MenuVolumen menuVolumen;
    private boolean viendoVolumen;

    private boolean solicitarPartidas;

    public MenuPrincipal(
            BitmapFont fuente,
            MenuVolumen menuVolumen) {
        this.fuente = fuente;

        this.menuVolumen = menuVolumen;

        anchoVentana = Gdx.graphics.isFullscreen()
            ? 960 : Gdx.graphics.getWidth();

        altoVentana = Gdx.graphics.isFullscreen()
            ? 540 : Gdx.graphics.getHeight();

        fondoTexture = cargarTextura("menu/menu.png");
        if (fondoTexture == null) {
            fondoTexture = cargarTextura("menu.png");
        }

        if (fondoTexture != null) {
            // Cuadrícula de 2 columnas x 3 filas (6 paneles)
            int columnas = 2;
            int filas = 3;
            int anchoFrame = fondoTexture.getWidth() / columnas;
            int altoFrame = fondoTexture.getHeight() / filas;

            TextureRegion[][] tmp = TextureRegion.split(fondoTexture, anchoFrame, altoFrame);
            int index = 0;
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    if (index < regionesFondo.length) {
                        regionesFondo[index++] = tmp[i][j];
                    }
                }
            }
        }

        logo = cargarTextura("menu/logo.png");
        selector = cargarTextura("menu/selector.png");

        for (int i = 0; i < botones.length; i++) {
            botones[i] = new Rectangle(
                90f,
                365f - (i * 45f),
                290f,
                38f
            );
        }
    }

    private Texture cargarTextura(String ruta) {
        if (Gdx.files.internal(ruta).exists()) {
            return new Texture(Gdx.files.internal(ruta));
        }
        return null;
    }

    private String[] opciones() {
        switch (pantalla) {
            case PRINCIPAL:
                return new String[] {
                    "CONTINUAR",
                    "PARTIDAS",
                    "CONFIGURACION",
                    "SALIR"
                };

            case CONFIGURACION:
                return new String[] {
                    Gdx.graphics.isFullscreen()
                        ? "MODO VENTANA"
                        : "PANTALLA COMPLETA",
                    "CONTROLES",
                    "MUSICA",
                    "VOLVER"
                };

            default:
                return new String[] {
                    "VOLVER"
                };
        }
    }

    private void cambiarPantalla(Pantalla nueva) {
        pantalla = nueva;
        seleccion = 0;
    }

    public boolean actualizar(Viewport viewport) {

    	if (viendoVolumen) {
    	    if (menuVolumen.actualizar(viewport)) {
    	        viendoVolumen = false;
    	    }

    	    return false;
    	}

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            cambiarPantalla(
                pantalla == Pantalla.CONTROLES
                    ? Pantalla.CONFIGURACION
                    : Pantalla.PRINCIPAL
            );

            return false;
        }

        int cantidad = opciones().length;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccion = (seleccion + cantidad - 1) % cantidad;

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = (seleccion + 1) % cantidad;
        }

        int bajoMouse = mouseMenu.obtenerOpcion(
            viewport,
            botones,
            cantidad
        );

        boolean clic =
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        if (bajoMouse >= 0 && (mouseMenu.seMovio() || clic)) {
            seleccion = bajoMouse;
        }

        boolean aceptar =
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || (clic && bajoMouse >= 0);

        if (!aceptar) {
            return false;
        }

        switch (pantalla) {
            case PRINCIPAL:

                switch (seleccion) {
                    case 0:
                        return true;

                    case 1:
                        solicitarPartidas = true;
                        break;

                    case 2:
                        cambiarPantalla(Pantalla.CONFIGURACION);
                        break;

                    case 3:
                        Gdx.app.exit();
                        break;
                }

                break;

            case CONFIGURACION:

                switch (seleccion) {
                    case 0:
                        alternarPantallaCompleta();
                        break;

                    case 1:
                        cambiarPantalla(Pantalla.CONTROLES);
                        break;

                    case 2:
                        menuVolumen.restablecer();
                        viendoVolumen = true;
                        break;

                    case 3:
                        cambiarPantalla(Pantalla.PRINCIPAL);
                        break;
                }

                break;

            default:

                cambiarPantalla(
                    pantalla == Pantalla.CONTROLES
                        ? Pantalla.CONFIGURACION
                        : Pantalla.PRINCIPAL
                );

                break;
        }

        return false;
    }

    private void alternarPantallaCompleta() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(
                anchoVentana,
                altoVentana
            );

        } else {
            anchoVentana = Gdx.graphics.getWidth();
            altoVentana = Gdx.graphics.getHeight();

            Gdx.graphics.setFullscreenMode(
                Gdx.graphics.getDisplayMode()
            );
        }
    }

    public void dibujar(
        SpriteBatch batch,
        ShapeRenderer formas) {

        float escalaX = fuente.getData().scaleX;
        float escalaY = fuente.getData().scaleY;
        Color colorAnterior = new Color(fuente.getColor());

        batch.setColor(Color.WHITE);
        batch.begin();

        if (regionesFondo != null && regionesFondo[0] != null) {
            TextureRegion fondoActual;

            if (pantalla == Pantalla.PRINCIPAL) {
                int indiceFondo = Math.min(seleccion, regionesFondo.length - 2);
                fondoActual = regionesFondo[indiceFondo];
            } else {
                // Último panel de la grilla (sin opciones pre-renderizadas)
                fondoActual = regionesFondo[regionesFondo.length - 1];
            }

            if (fondoActual != null) {
                batch.draw(fondoActual, 0f, 0f, 1280f, 720f);
            }
        }

        // En el menú principal el fondo ya trae el título y las opciones
        if (pantalla != Pantalla.PRINCIPAL) {
            fuente.getData().setScale(2.5f);
            fuente.setColor(1f, 0.90f, 0.69f, 1f);
            fuente.draw(batch, pantalla.name(), 65f, 635f);

            fuente.getData().setScale(1.5f);
            String[] textos = opciones();

            for (int i = 0; i < textos.length; i++) {
                Rectangle boton = botones[i];

                if (i == seleccion && selector != null) {
                    batch.draw(
                        selector,
                        boton.x,
                        boton.y,
                        boton.width,
                        boton.height
                    );
                }

                fuente.setColor(
                    i == seleccion
                        ? Color.ORANGE
                        : Color.LIGHT_GRAY
                );

                fuente.draw(
                    batch,
                    textos[i],
                    boton.x + 28f,
                    boton.y + 28f
                );

                if (i == seleccion && selector == null) {
                    fuente.draw(
                        batch,
                        ">",
                        boton.x + 5f,
                        boton.y + 28f
                    );
                }
            }

            fuente.getData().setScale(1.2f);
            fuente.setColor(Color.WHITE);

            if (pantalla == Pantalla.CONTROLES) {
                String[] controles = {
                    "Izquierda / derecha: mover",
                    "Z: salto / doble salto",
                    "C: dash",
                    "X: ataque",
                    "Arriba + X: golpe superior",
                    "Abajo + X en el aire: pogo",
                    "F: puertas verticales",
                    "A: pocima",
                    "ESC: pausa"
                };

                for (int i = 0; i < controles.length; i++) {
                    fuente.draw(
                        batch,
                        controles[i],
                        65f,
                        325f - (i * 28f)
                    );
                }
            } else if (pantalla == Pantalla.CREDITOS) {
                fuente.draw(
                    batch,
                    "MUSICAL TOWER",
                    65f,
                    320f
                );
            }
        }

        fuente.getData().setScale(1.2f);
        fuente.setColor(Color.LIGHT_GRAY);
        fuente.draw(
            batch,
            "Flechas y ENTER / clic izquierdo",
            65f,
            40f
        );

        batch.end();

        fuente.getData().setScale(escalaX, escalaY);
        fuente.setColor(colorAnterior);
    }

    @Override
    public void dispose() {
        if (fondoTexture != null) {
            fondoTexture.dispose();
        }
        if (logo != null) {
            logo.dispose();
        }
        if (selector != null) {
            selector.dispose();
        }
    }

    public void restablecer() {

    	viendoVolumen = false;

        cambiarPantalla(Pantalla.PRINCIPAL);
    }

    public boolean consumirSolicitudPartidas() {

        boolean solicitud = solicitarPartidas;

        solicitarPartidas = false;

        return solicitud;
    }
}
