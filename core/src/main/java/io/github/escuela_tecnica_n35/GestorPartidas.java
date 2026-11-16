package io.github.escuela_tecnica_n35;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.*;

import io.github.escuela_tecnica_n35.entidades.*;
import io.github.escuela_tecnica_n35.etapas.*;

import java.lang.reflect.Field;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.function.Function;

public class GestorPartidas {

    public static final int CANTIDAD = 3;

    private final FileHandle carpeta =
        Gdx.files.external(".musical-tower/partidas");

    private final HashMap<String, Bioma> biomas =
        new HashMap<String, Bioma>();

    public static class Resumen {

        public String etapa;
        public int monedas;
        public long fecha;
        public boolean error;
    }

    private FileHandle archivo(int slot) {

        if (slot < 0 || slot >= CANTIDAD) {
            throw new IllegalArgumentException(
                "Slot inválido."
            );
        }

        return carpeta.child(
            "partida-" + (slot + 1) + ".json"
        );
    }

    public boolean ocupado(int slot) {
        return archivo(slot).exists();
    }

    public int primerLibre() {

        for (int i = 0; i < CANTIDAD; i++) {

            if (!ocupado(i)) {
                return i;
            }
        }

        return -1;
    }

    public Resumen resumen(int slot) {

        if (!ocupado(slot)) {
            return null;
        }

        Resumen resumen = new Resumen();

        try {

            JsonValue raiz =
                new JsonReader().parse(archivo(slot));

            if (raiz.getInt("version", -1) != 1) {
                throw new IllegalStateException(
                    "Versión desconocida."
                );
            }

            resumen.etapa =
                raiz.getString("nombreEtapa");

            resumen.monedas =
                raiz.getInt("monedas");

            resumen.fecha =
                raiz.getLong("fecha");

        } catch (RuntimeException error) {

            resumen.error = true;

            resumen.etapa =
                "No se pudo leer la partida";
        }

        return resumen;
    }

    public int masReciente() {

        int elegido = -1;
        long fecha = Long.MIN_VALUE;

        for (int i = 0; i < CANTIDAD; i++) {

            Resumen resumen = resumen(i);

            if (resumen != null
                    && !resumen.error
                    && resumen.fecha > fecha) {

                fecha = resumen.fecha;
                elegido = i;
            }
        }

        return elegido;
    }

    public void eliminar(int slot) {

        FileHandle archivo = archivo(slot);

        if (archivo.exists() && !archivo.delete()) {

            throw new IllegalStateException(
                "No se pudo eliminar la partida."
            );
        }
    }

    public void guardar(
            int slot,
            DatosPartida datos) {

        FileHandle destino = archivo(slot);

        String contenido =
            crearJson(datos.jugador).toJson(datos);

        Path temporal =
            destino.sibling(
                destino.name() + ".tmp"
            ).file().toPath();

        try {

            Files.createDirectories(
                destino.file().toPath().getParent()
            );

            Files.write(
                temporal,
                contenido.getBytes(StandardCharsets.UTF_8)
            );

            try {

                Files.move(
                    temporal,
                    destino.file().toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
                );

            } catch (
                    AtomicMoveNotSupportedException error) {

                Files.move(
                    temporal,
                    destino.file().toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                );
            }

        } catch (java.io.IOException error) {

            throw new IllegalStateException(
                "No se pudo guardar la partida.",
                error
            );
        }
    }

    public DatosPartida cargar(
            int slot,
            Jugador jugador) {

        Resumen resumen = resumen(slot);

        if (resumen == null || resumen.error) {

            throw new IllegalStateException(
                "La partida no está disponible."
            );
        }

        DatosPartida datos =
            crearJson(jugador).fromJson(
                DatosPartida.class,
                archivo(slot)
            );

        if (datos.etapa == null
                || datos.jugador == null
                || datos.poolItems == null
                || datos.poolGenerador == null
                || datos.biomasDisponibles == null) {

            throw new IllegalStateException(
                "La partida está incompleta."
            );
        }

        Sala[][] mapa = datos.etapa.getMapa();

        if (mapa == null
                || datos.fila < 0
                || datos.fila >= mapa.length
                || datos.columna < 0
                || datos.columna >= mapa[datos.fila].length
                || mapa[datos.fila][datos.columna] == null) {

            throw new IllegalStateException(
                "La sala guardada no existe."
            );
        }

        return datos;
    }

    private <T> void registrar(
            Json json,
            Class<T> tipo,
            Function<JsonValue, T> crear) {

        json.setSerializer(
            tipo,
            new Json.Serializer<T>() {

                public void write(
                        Json json,
                        T objeto,
                        Class conocido) {

                    json.writeObjectStart(
                        objeto.getClass(),
                        conocido
                    );

                    json.writeFields(objeto);
                    json.writeObjectEnd();
                }

                public T read(
                        Json json,
                        JsonValue datos,
                        Class tipoLeido) {

                    if (datos == null || datos.isNull()) {
                        return null;
                    }

                    T objeto = crear.apply(datos);

                    json.readFields(objeto, datos);

                    if (objeto instanceof Entidad) {

                        Entidad entidad =
                            (Entidad) objeto;

                        entidad.getAtaques().clear();

                        if (objeto instanceof Jugador) {

                            Jugador jugador =
                                (Jugador) objeto;

                            entidad.agregarAtaque(
                                jugador.getAtaqueNormal()
                            );

                            entidad.agregarAtaque(
                                jugador.getAtaquePogo()
                            );

                        } else if (
                                objeto instanceof Enemigo) {

                            entidad.agregarAtaque(
                                ((Enemigo) objeto)
                                    .getAtaqueNormal()
                            );

                            if (objeto instanceof Jefe) {

                                entidad.agregarAtaque(
                                    ((Jefe) objeto)
                                        .getAtaqueDistancia()
                                );
                            }
                        }
                    }

                    if (objeto instanceof Etapa) {

                        ((Etapa) objeto)
                            .reconectarEntidadesGuardadas();
                    }

                    return objeto;
                }
            }
        );
    }

    private Json crearJson(Jugador jugador) {

        Json json = new Json();

        json.setOutputType(
            JsonWriter.OutputType.json
        );

        json.setUsePrototypes(false);
        json.setIgnoreDeprecated(true);
        json.setReadDeprecated(false);

        Class<?>[] actores = {
            Jugador.class,
            Enemigo.class,
            EnemigoVolador.class,
            Jefe.class
        };

        for (Class<?> actor : actores) {

            json.setDeprecated(
                actor,
                "ataques",
                true
            );
        }

        json.setDeprecated(
            Etapa.class,
            "enemigos",
            true
        );

        for (Field campo :
                Jugador.class.getDeclaredFields()) {

            if (TextureRegion.class.isAssignableFrom(
                        campo.getType())
                    || Animation.class.isAssignableFrom(
                        campo.getType())) {

                json.setDeprecated(
                    Jugador.class,
                    campo.getName(),
                    true
                );
            }
        }

        registrar(
            json,
            Posicion.class,
            d -> new Posicion(0, 0)
        );

        registrar(
            json,
            Hitbox.class,
            d -> new Hitbox(1, 1, 0, 0)
        );

        registrar(
            json,
            Ataque.class,
            d -> new Ataque("", 0, 1, 1, 0)
        );

        registrar(
            json,
            AtaqueEnemigo.class,
            d -> new AtaqueEnemigo("", 0, 1, 1, 0)
        );

        registrar(
            json,
            Jugador.class,
            d -> jugador
        );

        registrar(
            json,
            Enemigo.class,
            d -> new Enemigo(
                "",
                new Posicion(0, 0),
                1,
                0,
                0,
                d.getInt("monedasMinimas", 0),
                d.getInt("monedasMaximas", 0),
                new Hitbox(1, 1, 0, 0)
            )
        );

        registrar(
            json,
            EnemigoVolador.class,
            d -> new EnemigoVolador(
                "",
                new Posicion(0, 0),
                1,
                0,
                0,
                d.getInt("monedasMinimas", 0),
                d.getInt("monedasMaximas", 0)
            )
        );

        registrar(
            json,
            Jefe.class,
            d -> new Jefe(
                "",
                new Posicion(0, 0),
                1,
                0,
                0,
                d.getInt("monedasMinimas", 0),
                d.getInt("monedasMaximas", 0),
                1,
                1
            )
        );

        registrar(
            json,
            Item.class,
            d -> new Item(
                "",
                new Posicion(0, 0),
                "",
                1,
                TipoItem.PASIVO,
                TipoModificadorItem.VIDA_MAX,
                0,
                ""
            )
        );

        registrar(
            json,
            OfertaTienda.class,
            d -> new OfertaTienda(null, 20)
        );

        registrar(
            json,
            Proyectil.class,
            d -> new Proyectil(
                new Posicion(0, 0),
                1, 0, 1, 0, 1, 1
            )
        );

        registrar(
            json,
            Plataforma.class,
            d -> new Plataforma(0, 0, 1, 1)
        );

        registrar(
            json,
            Sala.class,
            d -> new Sala(
                0,
                0,
                TipoSala.INICIAL
            )
        );

        registrar(
            json,
            Etapa.class,
            d -> new Etapa(
                "",
                1,
                1,
                new java.util.ArrayList<Enemigo>(),
                null,
                new Sala[0][]
            )
        );

        json.setSerializer(
            Bioma.class,
            new Json.Serializer<Bioma>() {

                public void write(
                        Json json,
                        Bioma bioma,
                        Class conocido) {

                    json.writeObjectStart();

                    json.writeValue(
                        "nombre",
                        bioma.getNombre()
                    );

                    json.writeValue(
                        "ruta",
                        bioma.getRutaImagen()
                    );

                    json.writeObjectEnd();
                }

                public Bioma read(
                        Json json,
                        JsonValue datos,
                        Class tipo) {

                    if (datos == null || datos.isNull()) {
                        return null;
                    }

                    String nombre =
                        datos.getString("nombre");

                    String ruta =
                        datos.getString("ruta");

                    String clave =
                        nombre + "|" + ruta;

                    if (!biomas.containsKey(clave)) {

                        biomas.put(
                            clave,
                            new Bioma(nombre, ruta)
                        );
                    }

                    return biomas.get(clave);
                }
            }
        );

        return json;
    }

    public void dispose() {

        for (Bioma bioma : biomas.values()) {
            bioma.dispose();
        }

        biomas.clear();
    }
}