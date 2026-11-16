package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuPartidas {

    public enum Accion {
        NINGUNA,
        NUEVA,
        CONTINUAR,
        VOLVER
    }

    private final GestorPartidas gestor;
    private final BitmapFont fuente;

    private final MouseMenu mouse =
        new MouseMenu();

    private final Rectangle[] botones =
        new Rectangle[4];

    private final String[] acciones = {
        "NUEVA PARTIDA",
        "CONTINUAR",
        "ELIMINAR PARTIDA",
        "VOLVER"
    };

    private int seleccion;
    private int slotElegido;

    private boolean viendoAcciones;

    private String mensaje = "";

    public MenuPartidas(
            BitmapFont fuente,
            GestorPartidas gestor) {

        this.fuente = fuente;
        this.gestor = gestor;

        for (int i = 0; i < botones.length; i++) {

            botones[i] = new Rectangle(
                130,
                470 - i * 100,
                1020,
                80
            );
        }
    }

    public void abrir() {
        seleccion = 0;
        viendoAcciones = false;
        mensaje = "";
    }

    public void mostrarMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getSlotElegido() {
        return slotElegido;
    }

    public Accion actualizar(Viewport viewport) {

        if (Gdx.input.isKeyJustPressed(
                Input.Keys.ESCAPE)) {

            if (viendoAcciones) {

                viendoAcciones = false;
                seleccion = slotElegido;

                return Accion.NINGUNA;
            }

            return Accion.VOLVER;
        }

        if (Gdx.input.isKeyJustPressed(
                Input.Keys.UP)) {

            seleccion = (seleccion + 3) % 4;

        } else if (Gdx.input.isKeyJustPressed(
                Input.Keys.DOWN)) {

            seleccion = (seleccion + 1) % 4;
        }

        int bajoMouse =
            mouse.obtenerOpcion(
                viewport,
                botones,
                4
            );

        boolean clic =
            Gdx.input.isButtonJustPressed(
                Input.Buttons.LEFT
            );

        if (bajoMouse >= 0
                && (mouse.seMovio() || clic)) {

            seleccion = bajoMouse;
        }

        boolean aceptar =
            Gdx.input.isKeyJustPressed(
                Input.Keys.ENTER
            ) || (clic && bajoMouse >= 0);

        if (!aceptar) {
            return Accion.NINGUNA;
        }

        if (!viendoAcciones) {

            if (seleccion == 3) {
                return Accion.VOLVER;
            }

            slotElegido = seleccion;

            seleccion =
                gestor.ocupado(slotElegido) ? 1 : 0;

            viendoAcciones = true;
            mensaje = "";

            return Accion.NINGUNA;
        }

        switch (seleccion) {

            case 0:

                int libre = gestor.primerLibre();

                if (libre < 0) {

                    mensaje =
                        "Los tres slots están ocupados. "
                        + "Eliminá uno para empezar.";

                    return Accion.NINGUNA;
                }

                slotElegido = libre;

                return Accion.NUEVA;

            case 1:

                GestorPartidas.Resumen resumen =
                    gestor.resumen(slotElegido);

                if (resumen == null || resumen.error) {

                    mensaje =
                        "Este slot no tiene una "
                        + "partida disponible.";

                    return Accion.NINGUNA;
                }

                return Accion.CONTINUAR;

            case 2:

                try {

                    gestor.eliminar(slotElegido);

                    mensaje =
                        "Slot "
                        + (slotElegido + 1)
                        + " eliminado.";

                    viendoAcciones = false;
                    seleccion = slotElegido;

                } catch (RuntimeException error) {

                    mensaje =
                        "No se pudo eliminar la partida.";
                }

                return Accion.NINGUNA;

            default:

                viendoAcciones = false;
                seleccion = slotElegido;
                mensaje = "";

                return Accion.NINGUNA;
        }
    }

    public void dibujar(
            SpriteBatch batch,
            ShapeRenderer formas) {

        Gdx.gl.glEnable(GL20.GL_BLEND);

        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        formas.begin(
            ShapeRenderer.ShapeType.Filled
        );

        formas.setColor(
            0.04f, 0.04f, 0.08f, 0.98f
        );

        formas.rect(
            70, 70, 1140, 590
        );

        for (int i = 0; i < botones.length; i++) {

            formas.setColor(
                i == seleccion
                    ? new Color(
                        0.35f, 0.18f, 0.12f, 1f)
                    : new Color(
                        0.12f, 0.12f, 0.18f, 1f)
            );

            Rectangle boton = botones[i];

            formas.rect(
                boton.x,
                boton.y,
                boton.width,
                boton.height
            );
        }

        formas.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        float escalaX =
            fuente.getData().scaleX;

        float escalaY =
            fuente.getData().scaleY;

        Color color =
            new Color(fuente.getColor());

        batch.begin();

        fuente.setColor(Color.GOLD);
        fuente.getData().setScale(1.8f);

        fuente.draw(
            batch,
            viendoAcciones
                ? "PARTIDA " + (slotElegido + 1)
                : "PARTIDAS",
            130,
            620
        );

        fuente.getData().setScale(1.2f);

        for (int i = 0; i < botones.length; i++) {

            Rectangle boton = botones[i];

            fuente.setColor(
                i == seleccion
                    ? Color.ORANGE
                    : Color.WHITE
            );

            fuente.draw(
                batch,
                i == seleccion ? ">" : "",
                boton.x + 12,
                boton.y + 51
            );

            if (viendoAcciones) {

                fuente.draw(
                    batch,
                    acciones[i],
                    boton.x + 42,
                    boton.y + 51
                );

            } else if (i == 3) {

                fuente.draw(
                    batch,
                    "VOLVER",
                    boton.x + 42,
                    boton.y + 51
                );

            } else {

                GestorPartidas.Resumen resumen =
                    gestor.resumen(i);

                fuente.draw(
                    batch,
                    "SLOT " + (i + 1)
                        + (resumen == null
                            ? " - VACÍO"
                            : " - " + resumen.etapa),
                    boton.x + 42,
                    boton.y + 58
                );

                if (resumen != null
                        && !resumen.error) {

                    fuente.draw(
                        batch,
                        "Monedas: " + resumen.monedas,
                        boton.x + 42,
                        boton.y + 28
                    );
                }
            }
        }

        fuente.setColor(Color.LIGHT_GRAY);
        fuente.getData().setScale(1f);

        fuente.draw(
            batch,
            mensaje,
            130,
            135
        );

        fuente.draw(
            batch,
            "Flechas y ENTER / mouse | ESC: volver",
            130,
            100
        );

        batch.end();

        fuente.getData().setScale(
            escalaX,
            escalaY
        );

        fuente.setColor(color);
    }
}