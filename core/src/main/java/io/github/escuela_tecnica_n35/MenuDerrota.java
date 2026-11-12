package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

    private static final Color COLOR_SELECCION =
        new Color(0.40f, 0.025f, 0.08f, 1f);

    private static final Color COLOR_BOTON =
        new Color(0.08f, 0.025f, 0.10f, 1f);

    private final BitmapFont fuente;
    private final GlyphLayout layout = new GlyphLayout();

    private final Rectangle[] botones = new Rectangle[3];
    private final Vector2 raton = new Vector2();

    private final Texture fondo;
    private final Texture titulo;
    private final Texture selector;

    private int seleccion;

    public MenuDerrota(BitmapFont fuente) {
        this.fuente = fuente;

        fondo = cargarTextura("menu/derrota_fondo.png");
        titulo = cargarTextura("menu/derrota_titulo.png");
        selector = cargarTextura("menu/derrota_selector.png");

        for (int i = 0; i < botones.length; i++) {
            botones[i] = new Rectangle(
                455f,
                365f - i * 65f,
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
            seleccion =
                (seleccion + OPCIONES.length - 1) % OPCIONES.length;

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = (seleccion + 1) % OPCIONES.length;
        }

        boolean aceptar =
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float x = Gdx.input.getX();

            float y =
                Gdx.graphics.getHeight() - Gdx.input.getY();

            boolean dentroDelViewport =
                x >= viewport.getScreenX()
                && x < viewport.getScreenX()
                    + viewport.getScreenWidth()
                && y >= viewport.getScreenY()
                && y < viewport.getScreenY()
                    + viewport.getScreenHeight();

            if (dentroDelViewport) {
                viewport.unproject(
                    raton.set(
                        Gdx.input.getX(),
                        Gdx.input.getY()
                    )
                );

                for (int i = 0; i < botones.length; i++) {
                    if (botones[i].contains(raton)) {
                        seleccion = i;
                        aceptar = true;
                        break;
                    }
                }
            }
        }

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

        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(0.035f, 0.02f, 0.06f, 1f);
        formas.rect(0f, 0f, 1280f, 720f);
        formas.end();

        batch.setColor(Color.WHITE);
        batch.begin();

        if (fondo != null) {
            batch.draw(fondo, 0f, 0f, 1280f, 720f);
        }

        batch.end();

        // Fondo de los botones.
        formas.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < botones.length; i++) {
            Rectangle boton = botones[i];

            formas.setColor(
                i == seleccion
                    ? COLOR_SELECCION
                    : COLOR_BOTON
            );

            formas.rect(
                boton.x,
                boton.y,
                boton.width,
                boton.height
            );
        }

        formas.end();

        // Bordes de los botones.
        formas.begin(ShapeRenderer.ShapeType.Line);
        formas.setColor(1f, 0.65f, 0.55f, 1f);

        for (Rectangle boton : botones) {
            formas.rect(
                boton.x,
                boton.y,
                boton.width,
                boton.height
            );
        }

        formas.end();

        float escalaX = fuente.getData().scaleX;
        float escalaY = fuente.getData().scaleY;
        Color colorAnterior = new Color(fuente.getColor());

        batch.begin();

        if (titulo != null) {
            batch.draw(titulo, 280f, 515f, 720f, 140f);

        } else {
            fuente.getData().setScale(3.5f);
            fuente.setColor(1f, 0.35f, 0.30f, 1f);

            dibujarTextoCentrado(
                batch,
                "HAS CAÍDO",
                610f
            );
        }

        fuente.getData().setScale(1.5f);
        fuente.setColor(1f, 0.75f, 0.70f, 1f);

        dibujarTextoCentrado(
            batch,
            "¿No pudiste seguir el ritmo?",
            495f
        );

        for (int i = 0; i < botones.length; i++) {
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

            dibujarTextoCentrado(
                batch,
                OPCIONES[i],
                boton.y + 34f
            );
        }

        fuente.getData().setScale(1.1f);
        fuente.setColor(Color.LIGHT_GRAY);

        dibujarTextoCentrado(
            batch,
            "Flechas y ENTER / clic izquierdo",
            45f
        );

        batch.end();

        fuente.getData().setScale(escalaX, escalaY);
        fuente.setColor(colorAnterior);
    }

    private void dibujarTextoCentrado(
            SpriteBatch batch,
            String texto,
            float y) {

        layout.setText(fuente, texto);

        fuente.draw(
            batch,
            layout,
            (1280f - layout.width) / 2f,
            y
        );
    }

    @Override
    public void dispose() {
        if (fondo != null) {
            fondo.dispose();
        }

        if (titulo != null) {
            titulo.dispose();
        }

        if (selector != null) {
            selector.dispose();
        }
    }
}