package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuVolumen {

    private final BitmapFont fuente;
    private final GestorMusica musica;

    private final MouseMenu mouse = new MouseMenu();
    private final GlyphLayout layout = new GlyphLayout();

    private final Rectangle[] botones = {
        new Rectangle(440f, 335f, 70f, 45f),
        new Rectangle(770f, 335f, 70f, 45f),
        new Rectangle(470f, 190f, 340f, 42f)
    };

    private final String[] textos = {
        "-",
        "+",
        "VOLVER"
    };

    private int seleccion = 2;

    public MenuVolumen(
            BitmapFont fuente,
            GestorMusica musica) {

        this.fuente = fuente;
        this.musica = musica;
    }

    public void restablecer() {
        seleccion = 2;
    }

    public boolean actualizar(Viewport viewport) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            return true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            musica.cambiarVolumen(-1);

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            musica.cambiarVolumen(1);
        }

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

        if (aceptar) {
            if (seleccion == 2) {
                return true;
            }

            musica.cambiarVolumen(
                seleccion == 0 ? -1 : 1
            );
        }

        return false;
    }

    public void dibujar(
            SpriteBatch batch,
            ShapeRenderer formas) {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(0f, 0f, 0f, 0.55f);
        formas.rect(0f, 0f, 1280f, 720f);
        formas.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(0.08f, 0.09f, 0.13f, 1f);
        formas.rect(360f, 155f, 560f, 350f);

        for (int i = 0; i < 10; i++) {
            formas.setColor(
                i < musica.getVolumen()
                    ? Color.ORANGE
                    : Color.DARK_GRAY
            );

            formas.rect(
                475f + i * 34f,
                285f,
                24f,
                20f
            );
        }

        formas.end();

        float escalaX = fuente.getData().scaleX;
        float escalaY = fuente.getData().scaleY;
        Color colorAnterior = new Color(fuente.getColor());

        fuente.getData().setScale(1.5f);

        batch.begin();

        fuente.setColor(Color.WHITE);

        centrado(
            batch,
            "VOLUMEN DE MUSICA",
            455f,
            640f
        );

        centrado(
            batch,
            musica.getVolumen() + " / 10",
            365f,
            640f
        );

        for (int i = 0; i < botones.length; i++) {
            Rectangle boton = botones[i];

            fuente.setColor(
                i == seleccion ? Color.ORANGE : Color.WHITE
            );

            centrado(
                batch,
                textos[i],
                boton.y + 30f,
                boton.x + boton.width / 2f
            );

            if (i == seleccion) {
                fuente.draw(
                    batch,
                    ">",
                    boton.x + 8f,
                    boton.y + 30f
                );
            }
        }

        batch.end();

        fuente.getData().setScale(escalaX, escalaY);
        fuente.setColor(colorAnterior);
    }

    private void centrado(
            SpriteBatch batch,
            String texto,
            float y,
            float x) {

        layout.setText(fuente, texto);

        fuente.draw(
            batch,
            layout,
            x - layout.width / 2f,
            y
        );
    }
}