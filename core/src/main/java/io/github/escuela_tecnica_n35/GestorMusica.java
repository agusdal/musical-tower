package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;
import io.github.escuela_tecnica_n35.etapas.Etapa;

public class GestorMusica implements Disposable {

    private final Preferences preferencias;

    private Music cancion;
    private String rutaActual;
    private int volumen;

    public GestorMusica() {
        preferencias =
            Gdx.app.getPreferences("musical-tower-audio");

        volumen = Math.max(
            0,
            Math.min(10, preferencias.getInteger("volumen", 5))
        );
    }

    public int getVolumen() {
        return volumen;
    }

    public void cambiarVolumen(int puntos) {
        int nuevo = Math.max(
            0,
            Math.min(10, volumen + puntos)
        );

        if (nuevo == volumen) {
            return;
        }

        volumen = nuevo;

        if (cancion != null) {
            cancion.setVolume(volumen / 10f);
        }

        preferencias.putInteger("volumen", volumen);
        preferencias.flush();
    }

    public void actualizar(Etapa etapa) {
        String ruta = etapa == null
            ? "musica/menu.mp3"
            : rutaEtapa(etapa.getNombre());

        if (ruta.equals(rutaActual)) {
            return;
        }

        if (cancion != null) {
            cancion.dispose();
            cancion = null;
        }

        rutaActual = ruta;

        if (!Gdx.files.internal(ruta).exists()) {
            Gdx.app.error(
                "Musica",
                "No se encontro el archivo: " + ruta
            );
            return;
        }

        cancion = Gdx.audio.newMusic(
            Gdx.files.internal(ruta)
        );

        cancion.setLooping(true);
        cancion.setVolume(volumen / 10f);
        cancion.play();
    }

    private String rutaEtapa(String nombre) {
        switch (nombre) {
            case "DISCOTERÍA":
                return "musica/disco.mp3";

            case "ROCK AND FALL":
                return "musica/rock.mp3";

            case "FIESTA SHOCKEANTE":
                return "musica/electro.mp3";

            case "FLOTANDO EN NUBES":
                return "musica/reggae.mp3";

            case "BASE Y COMPÁS":
                return "musica/rap.mp3";

            case "WHIPLASH":
                return "musica/jazz.mp3";

            case "PISO FINAL":
                return "musica/final.mp3";

            default:
                throw new IllegalArgumentException(
                    "Etapa sin cancion asignada: " + nombre
                );
        }
    }

    @Override
    public void dispose() {
        if (cancion != null) {
            cancion.dispose();
            cancion = null;
        }

        rutaActual = null;
    }
}