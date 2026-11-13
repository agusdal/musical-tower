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

public class MenuDerrota implements Disposable {

    public enum Accion {
        NINGUNA,
        REINICIAR,
        MENU_PRINCIPAL,
        SALIR
    }

    private static final String[] OPCIONES = {
        "REINICIAR PARTIDA",
        "MENU PRINCIPAL",
        "SALIR DEL JUEGO"
    };

    private final BitmapFont fuente;
    private final Rectangle[] botones = new Rectangle[3];
    private final MouseMenu mouseMenu = new MouseMenu();

    private Texture fondoTexture;
    private final TextureRegion[] regionesFondo = new TextureRegion[3];

    private int seleccion;

    public MenuDerrota(BitmapFont fuente) {
        this.fuente = fuente;

        // Cargamos la imagen vertical con las 3 variantes de derrota
        fondoTexture = cargarTextura("derrota.png");
        if (fondoTexture == null) {
            fondoTexture = cargarTextura("derrota.png");
        }

        if (fondoTexture != null) {
            // Cuadrícula de 1 columna x 3 filas (3 paneles verticales)
            int columnas = 1;
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

        // Definimos las zonas interactivas (hitboxes) para que el mouse y las flechas detecten los botones
        for (int i = 0; i < botones.length; i++) {
            botones[i] = new Rectangle(
                455f,
                365f - (i * 65f),
                370f,
                50f
            );
        }
    }

    private Texture cargarTextura(String ruta) {
        if (Gdx.files.internal(ruta).exists()) {
            return new Texture(Gdx.files.internal(ruta));
        }
        return null;
    }

    public void reiniciarSeleccion() {
        seleccion = 0;
    }

    public Accion actualizar(Viewport viewport) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccion = (seleccion + OPCIONES.length - 1) % OPCIONES.length;

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = (seleccion + 1) % OPCIONES.length;
        }

        int bajoMouse = mouseMenu.obtenerOpcion(
            viewport,
            botones,
            botones.length
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
            return Accion.NINGUNA;
        }

        switch (seleccion) {
            case 0:
                return Accion.REINICIAR;

            case 1:
                return Accion.MENU_PRINCIPAL;

            default:
                return Accion.SALIR;
        }
    }

    public void dibujar(
        SpriteBatch batch,
        ShapeRenderer formas) {

        batch.setColor(Color.WHITE);
        batch.begin();

        // Dibujamos únicamente el fondo dinámico correspondiente a la opción seleccionada (0, 1 o 2)
        if (regionesFondo[0] != null) {
            int indiceFondo = Math.min(seleccion, regionesFondo.length - 1);
            TextureRegion fondoActual = regionesFondo[indiceFondo];

            if (fondoActual != null) {
                batch.draw(fondoActual, 0f, 0f, 1280f, 720f);
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        if (fondoTexture != null) {
            fondoTexture.dispose();
        }
    }
}
