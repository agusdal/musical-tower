package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuVictoria implements Disposable {

    public enum Accion {
        NINGUNA,
        REINICIAR,
        MENU_PRINCIPAL,
        SALIR
    }

    private static final String[] OPCIONES = {
        "VOLVER A JUGAR",
        "MENU PRINCIPAL",
        "SALIR DEL JUEGO"
    };

    private final BitmapFont fuente;
    private final GlyphLayout layout = new GlyphLayout();
    private final MouseMenu mouse = new MouseMenu();

    private final Rectangle[] botones = new Rectangle[3];
    private final TextureRegion[] fondos = new TextureRegion[3];
    private final Texture textura;

    private int seleccion;

    public MenuVictoria(BitmapFont fuente) {
        this.fuente = fuente;

        textura = Gdx.files.internal("victoria.png").exists()
            ? new Texture("victoria.png")
            : null;

        if (textura != null) {
            int alto = textura.getHeight() / 3;

            for (int i = 0; i < fondos.length; i++) {
                fondos[i] = new TextureRegion(
                    textura,
                    0,
                    i * alto,
                    textura.getWidth(),
                    alto
                );
            }
        }

        for (int i = 0; i < botones.length; i++) {
            botones[i] = new Rectangle(
                455f,
                365f - i * 65f,
                370f,
                50f
            );
        }
    }

    public void reiniciarSeleccion() {
        seleccion = 0;
    }

    public Accion actualizar(Viewport viewport) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccion = (seleccion + 2) % 3;

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = (seleccion + 1) % 3;
        }

        int bajoMouse = mouse.obtenerOpcion(
            viewport,
            botones,
            botones.length
        );

        boolean clic =
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        if (bajoMouse >= 0 && (mouse.seMovio() || clic)) {
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

        if (textura == null) {
            formas.begin(ShapeRenderer.ShapeType.Filled);

            formas.setColor(0.035f, 0.02f, 0.06f, 1f);
            formas.rect(0f, 0f, 1280f, 720f);

            for (int i = 0; i < botones.length; i++) {
                Rectangle boton = botones[i];

                if (i == seleccion) {
                    formas.setColor(0.40f, 0.025f, 0.08f, 1f);
                } else {
                    formas.setColor(0.12f, 0.06f, 0.15f, 1f);
                }

                formas.rect(
                    boton.x,
                    boton.y,
                    boton.width,
                    boton.height
                );
            }

            formas.end();
        }

        float escalaX = fuente.getData().scaleX;
        float escalaY = fuente.getData().scaleY;

        Color colorAnterior = new Color(fuente.getColor());

        batch.setColor(Color.WHITE);
        batch.begin();

        if (textura != null) {
            batch.draw(
                fondos[seleccion],
                0f,
                0f,
                1280f,
                720f
            );

        } else {
            fuente.getData().setScale(3f);
            fuente.setColor(1f, 0.35f, 0.30f, 1f);

            textoCentrado(
                batch,
                "HAS VENCIDO",
                610f
            );

            fuente.getData().setScale(1.5f);
            fuente.setColor(1f, 0.75f, 0.70f, 1f);

            textoCentrado(
                batch,
                "¡No pudieron seguirte el ritmo!",
                495f
            );

            for (int i = 0; i < botones.length; i++) {
                fuente.setColor(
                    i == seleccion
                        ? Color.ORANGE
                        : Color.WHITE
                );

                textoCentrado(
                    batch,
                    OPCIONES[i],
                    botones[i].y + 34f
                );
            }
        }

        fuente.getData().setScale(1.5f);
        fuente.setColor(Color.ORANGE);

        fuente.draw(
            batch,
            ">",
            botones[seleccion].x - 24f,
            botones[seleccion].y + 34f
        );

        batch.end();

        fuente.getData().setScale(escalaX, escalaY);
        fuente.setColor(colorAnterior);
    }

    private void textoCentrado(
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
        if (textura != null) {
            textura.dispose();
        }
    }
}