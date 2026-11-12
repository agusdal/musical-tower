package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MouseMenu {

    private final Vector2 posicion = new Vector2();

    private int ultimoX = Integer.MIN_VALUE;
    private int ultimoY = Integer.MIN_VALUE;

    private boolean movido;

    public boolean seMovio() {
        return movido;
    }

    public int obtenerOpcion(
            Viewport viewport,
            Rectangle[] botones,
            int cantidad) {

        int x = Gdx.input.getX();
        int y = Gdx.input.getY();

        movido = x != ultimoX || y != ultimoY;

        ultimoX = x;
        ultimoY = y;

        float yDesdeAbajo =
            Gdx.graphics.getHeight() - y;

        boolean dentro =
            x >= viewport.getScreenX()
            && x < viewport.getScreenX()
                + viewport.getScreenWidth()
            && yDesdeAbajo >= viewport.getScreenY()
            && yDesdeAbajo < viewport.getScreenY()
                + viewport.getScreenHeight();

        if (!dentro) {
            return -1;
        }

        viewport.unproject(posicion.set(x, y));

        for (int i = 0; i < cantidad; i++) {
            if (botones[i].contains(posicion)) {
                return i;
            }
        }

        return -1;
    }
}