package io.github.escuela_tecnica_n35.etapas;

import com.badlogic.gdx.math.Rectangle;

public class Plataforma {

    private static final float ANCHO_ASSET = 1024f;
    private static final float ALTO_ASSET = 576f;

    private static final float INICIO_CUERPO_X = 164f;
    private static final float INICIO_CUERPO_Y = 188f;
    private static final float ANCHO_CUERPO = 729f;
    private static final float ALTO_CUERPO = 222f;

    private static final float ALTO_VISUAL = 140f;
    private static final float DESPLAZAMIENTO_VISUAL_Y = 60f;

    private float x;
    private float y;
    private float ancho;
    private float alto;

    public Plataforma(
            float x,
            float y,
            float ancho,
            float alto) {

        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }

    public float getYVisual() {
        return y + alto
            - ALTO_VISUAL
            + DESPLAZAMIENTO_VISUAL_Y;
    }

    public float getAltoVisual() {
        return ALTO_VISUAL;
    }

    public Rectangle getHitbox() {
        float escalaX = ancho / ANCHO_ASSET;
        float escalaY = ALTO_VISUAL / ALTO_ASSET;

        return new Rectangle(
            x + INICIO_CUERPO_X * escalaX,

            getYVisual()
                + (ALTO_ASSET
                    - INICIO_CUERPO_Y
                    - ALTO_CUERPO) * escalaY,

            ANCHO_CUERPO * escalaX,
            ALTO_CUERPO * escalaY
        );
    }

    public float getSuperficieY() {
        Rectangle cuerpo = getHitbox();

        return cuerpo.y + cuerpo.height;
    }

    public float getCentroX() {
        Rectangle cuerpo = getHitbox();

        return cuerpo.x + cuerpo.width / 2f;
    }
}