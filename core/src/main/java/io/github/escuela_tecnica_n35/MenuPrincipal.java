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

    public MenuPrincipal(BitmapFont fuente) {
        this.fuente = fuente;

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
                    "JUGAR",
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
                        // PARTIDAS (puedes enlazarlo o dejarlo temporal)
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

        // 1. DIBUJAR EL FONDO DINÁMICO
        if (regionesFondo[0] != null) {
            TextureRegion fondoActual;

            if (pantalla == Pantalla.PRINCIPAL) {
                // Cambia entre las primeras 5 imágenes según la opción seleccionada (índices 0 al 4)
                int indiceFondo = Math.min(seleccion, 4);
                fondoActual = regionesFondo[indiceFondo];
            } else {
                // En submenús usa la 6ª imagen (la que NO tiene letras, índice 4 o 5 dependiendo del orden,
                // por tu imagen la 6ª celda corresponde al índice 4 o 5 de la matriz 2x3. Probemos con la 4 o 5).
                // Como es la última de la grilla de 6 elementos (fila 3, columna 1), su índice es 4 (en base 0: 0,1,2,3,4).
                fondoActual = regionesFondo[4];
            }

            if (fondoActual != null) {
                batch.draw(fondoActual, 0f, 0f, 1280f, 720f);
            }
        }

        // 2. TÍTULO / LOGO (Solo se dibuja en submenús, ya que en el menú principal ya viene en el fondo)
        if (pantalla != Pantalla.PRINCIPAL) {
            fuente.getData().setScale(2.5f);
            String titulo = pantalla.name();
            fuente.setColor(1f, 0.90f, 0.69f, 1f);
            fuente.draw(batch, titulo, 65f, 635f);
        }

        fuente.getData().setScale(1.5f);
        String[] textos = opciones();

        // 3. BOTONES Y TEXTOS
        for (int i = 0; i < textos.length; i++) {
            Rectangle boton = botones[i];

            // Selector gráfico si lo hay
            if (i == seleccion && selector != null) {
                batch.draw(
                    selector,
                    boton.x,
                    boton.y,
                    boton.width,
                    boton.height
                );
            }

            // SOLO dibujamos las letras por código si ESTAMOS EN SUBMENÚS.
            // En el menú principal el fondo ya trae las letras dibujadas.
            if (pantalla != Pantalla.PRINCIPAL) {
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

                if (selector == null && i == seleccion) {
                    fuente.draw(
                        batch,
                        ">",
                        boton.x + 5f,
                        boton.y + 28f
                    );
                }
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
        cambiarPantalla(Pantalla.PRINCIPAL);
    }
}
