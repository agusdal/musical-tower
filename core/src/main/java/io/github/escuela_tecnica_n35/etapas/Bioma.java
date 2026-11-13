package io.github.escuela_tecnica_n35.etapas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bioma {
    private String nombre;
    private Texture texturaCompleta;
    private TextureRegion texturaDosPuertas;
    private TextureRegion texturaPuertaIzquierda;
    private TextureRegion texturaPuertaDerecha;

    public Bioma(String nombre, String rutaImagen) {
        this.nombre = nombre;
        this.texturaCompleta = new Texture(rutaImagen);

        int ancho = texturaCompleta.getWidth();
        int altoTotal = texturaCompleta.getHeight();
        int altoTercio = altoTotal / 3;

        // Recortamos el archivo único en 3 secciones verticales:
        // 1. Tercio superior: Dos puertas
        // 2. Tercio medio: Puerta izquierda
        // 3. Tercio inferior: Puerta derecha
        this.texturaDosPuertas      = new TextureRegion(texturaCompleta, 0, 0, ancho, altoTercio);
        this.texturaPuertaIzquierda = new TextureRegion(texturaCompleta, 0, altoTercio, ancho, altoTercio);
        this.texturaPuertaDerecha   = new TextureRegion(texturaCompleta, 0, altoTercio * 2, ancho, altoTercio);
    }

    public String getNombre() {
        return nombre;
    }

    public TextureRegion getTexturaDosPuertas() {
        return texturaDosPuertas;
    }

    public TextureRegion getTexturaPuertaIzquierda() {
        return texturaPuertaIzquierda;
    }

    public TextureRegion getTexturaPuertaDerecha() {
        return texturaPuertaDerecha;
    }

    public void dispose() {
        if (texturaCompleta != null) {
            texturaCompleta.dispose();
        }
    }
}
