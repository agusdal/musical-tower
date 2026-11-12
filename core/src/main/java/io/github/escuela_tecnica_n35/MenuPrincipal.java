package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    private final Vector2 raton = new Vector2();

    private final Texture fondo;
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

        fondo = cargarTextura("menu/fondo.png");
        logo = cargarTextura("menu/logo.png");
        selector = cargarTextura("menu/selector.png");

        for (int i = 0; i < botones.length; i++) {
            botones[i] = new Rectangle(
                90f,
                365f - i * 45f,
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
                    "CONFIGURACION",
                    "CREDITOS",
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

    // Devuelve true cuando se elige Jugar.
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

        boolean aceptar =
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            float mouseX = Gdx.input.getX();

            float mouseY =
                Gdx.graphics.getHeight() - Gdx.input.getY();

            boolean dentroDelViewport =
                mouseX >= viewport.getScreenX()
                && mouseX < viewport.getScreenX()
                    + viewport.getScreenWidth()
                && mouseY >= viewport.getScreenY()
                && mouseY < viewport.getScreenY()
                    + viewport.getScreenHeight();

            if (dentroDelViewport) {

                viewport.unproject(
                    raton.set(
                        Gdx.input.getX(),
                        Gdx.input.getY()
                    )
                );

                for (int i = 0; i < cantidad; i++) {
                    if (botones[i].contains(raton)) {
                        seleccion = i;
                        aceptar = true;
                        break;
                    }
                }
            }
        }

        if (!aceptar) {
            return false;
        }

        switch (pantalla) {
            case PRINCIPAL:

                switch (seleccion) {
                    case 0:
                        return true;

                    case 1:
                        cambiarPantalla(Pantalla.CONFIGURACION);
                        break;

                    case 2:
                        cambiarPantalla(Pantalla.CREDITOS);
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

        // Fondo provisional hasta incorporar la ilustración.
        if (fondo == null) {
            formas.begin(ShapeRenderer.ShapeType.Filled);
            formas.setColor(0.06f, 0.05f, 0.10f, 1f);
            formas.rect(0f, 0f, 480f, 720f);
            formas.end();
        }

        float escalaX = fuente.getData().scaleX;
        float escalaY = fuente.getData().scaleY;

        Color colorAnterior = new Color(fuente.getColor());

        batch.setColor(Color.WHITE);
        batch.begin();

        if (fondo != null) {
            batch.draw(fondo, 0f, 0f, 1280f, 720f);
        }

        if (pantalla == Pantalla.PRINCIPAL && logo != null) {
            batch.draw(logo, 65f, 435f, 350f, 245f);

        } else {
            fuente.getData().setScale(2.5f);

            String titulo = pantalla == Pantalla.PRINCIPAL
                ? "MUSIC TOWER"
                : pantalla.name();

            fuente.setColor(1f, 0.90f, 0.69f, 1f);
            fuente.draw(batch, titulo, 65f, 635f);
        }

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
                    325f - i * 28f
                );
            }

        } else if (pantalla == Pantalla.CREDITOS) {

            // Agregar aquí los nombres y funciones del equipo.
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
        if (fondo != null) {
            fondo.dispose();
        }

        if (logo != null) {
            logo.dispose();
        }

        if (selector != null) {
            selector.dispose();
        }
    }
}