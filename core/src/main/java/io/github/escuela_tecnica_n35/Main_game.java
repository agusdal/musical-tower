package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

//Importamos las clases de otros paquetes
import io.github.escuela_tecnica_n35.entidades.Jugador;
import io.github.escuela_tecnica_n35.entidades.Posicion;
import io.github.escuela_tecnica_n35.juego.Juego;

public class Main_game extends ApplicationAdapter {
	
    private SpriteBatch batch;
    private Texture image;
    
    private Jugador jugador;
    private Juego juego;
    
    private float velocidadY;
    private float aceleracionCaidaRapida;
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
        
        juego = new Juego(jugador);
        juego.iniciarJuego();

        velocidadY = 0;
        aceleracionCaidaRapida = 1400;
        gravedad = -800;

        pisoY = 50;
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // "Limpia la pantalla despues de cada frame"

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
        
        // CAÍDA RAPIDA
        
        if (Gdx.input.isKeyPressed(Input.Keys.S)
                && jugador.getPosicion().getY() > pisoY) {

            velocidadY -= aceleracionCaidaRapida * delta;
        }
        
        // SALTO

        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
        	     Gdx.input.isKeyJustPressed(Input.Keys.W))
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
            140
        );

        batch.end();
    }

    @Override
    public void dispose() {

        batch.dispose();
        image.dispose();
    }
}