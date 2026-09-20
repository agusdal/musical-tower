package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.escuela_tecnica_n35.entidades.Jugador;
import io.github.escuela_tecnica_n35.entidades.Posicion;

public class Main_game extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture image;

    private Jugador jugador;

    private float velocidadY;
    private float gravedad;
    private float pisoY;

    @Override
    public void create() {

        batch = new SpriteBatch();
        image = new Texture("KMD.jpeg");

        Posicion posicionInicial = new Posicion(140, 50);

        jugador = new Jugador(
            "KMD",
            posicionInicial,
            100,
            200,
            7
        );

        velocidadY = 0;
        gravedad = -800;

        pisoY = 50;
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // MOVIMIENTO HORIZONTAL

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            jugador.getPosicion().moverX(
                (float) (jugador.getVelocidad() * delta)
            );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {

            jugador.getPosicion().moverX(
                (float) (-jugador.getVelocidad() * delta)
            );
        }

        // SALTO

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                && jugador.getPosicion().getY() == pisoY) {

            velocidadY = 400;
        }

        // GRAVEDAD

        velocidadY += gravedad * delta;

        jugador.getPosicion().moverY(
            velocidadY * delta
        );

        // COLISIÓN CON EL PISO

        if (jugador.getPosicion().getY() < pisoY) {

            jugador.getPosicion().setY(pisoY);

            velocidadY = 0;
        }

        // DIBUJAR

        batch.begin();

        batch.draw(
            image,
            jugador.getPosicion().getX(),
            jugador.getPosicion().getY(),
            80,
            80
        );

        batch.end();
    }

    @Override
    public void dispose() {

        batch.dispose();
        image.dispose();
    }
}