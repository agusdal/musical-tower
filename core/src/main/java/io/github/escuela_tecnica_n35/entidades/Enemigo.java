package io.github.escuela_tecnica_n35.entidades;

import java.util.Random;

public class Enemigo extends Entidad {

    private int monedasMinimas;
    private int monedasMaximas;

    private Hitbox hitbox;

    private float velocidadY = 0f;

    private EstadoEnemigo estado;

    private AtaqueEnemigo ataqueNormal;
    private boolean ataqueHaciaArriba;

    private float tiempoPreparacionAtaque = 0.5f;
    private float tiempoRecuperacionAtaque = 0.25f;

    private float tiempoFaseAtaque;
    private boolean golpeEjecutado;

    // --- VARIABLES DE ANIMACIÓN Y SPRITE SHEET ---
    private float tiempoAnimacion = 0f;
    private int frameActual = 1;
    private boolean mirandoDerecha = false;
    private float velocidadAnimacionCaminata = 0.1f;

    // Configuración de la cuadrícula del sprite sheet (3 columnas y 9 filas para los 26 frames)
    private int numColumnas = 3;
    private int numFilas = 9;

    public Enemigo(
        String nombre,
        Posicion posicion,
        int vidaMax,
        float velocidad,
        int daño,
        int monedasMinimas,
        int monedasMaximas,
        Hitbox hitbox) {

        super(nombre, posicion, vidaMax, velocidad, daño);

        this.monedasMinimas = monedasMinimas;
        this.monedasMaximas = monedasMaximas;

        this.hitbox = hitbox;

        this.estado = EstadoEnemigo.QUIETO;

        this.ataqueNormal = new AtaqueEnemigo(
            "Ataque enemigo",
            daño,
            150f,
            100f,
            1f
        );

        agregarAtaque(ataqueNormal);
    }

    public int generarMonedasDrop() {
        Random random = new Random();
        return random.nextInt(monedasMaximas - monedasMinimas + 1) + monedasMinimas;
    }

    public int getMonedasMinimas() {
        return monedasMinimas;
    }

    public int getMonedasMaximas() {
        return monedasMaximas;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void actualizarGravedad(float delta, float gravedad) {
        velocidadY += gravedad * delta;
        getPosicion().moverY(velocidadY * delta);
    }

    public float getVelocidadY() {
        return velocidadY;
    }

    public void detenerCaida() {
        velocidadY = 0f;
    }

    public EstadoEnemigo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnemigo estado) {
        this.estado = estado;
    }

    public AtaqueEnemigo getAtaqueNormal() {
        return ataqueNormal;
    }

    public boolean esAtaqueHaciaArriba() {
        return ataqueHaciaArriba;
    }

    public void iniciarAtaque(boolean haciaArriba) {
        if (estado == EstadoEnemigo.ATACAR || !ataqueNormal.estaDisponible()) {
            return;
        }

        estado = EstadoEnemigo.ATACAR;
        ataqueHaciaArriba = haciaArriba;
        golpeEjecutado = false;
        tiempoFaseAtaque = tiempoPreparacionAtaque;
        tiempoAnimacion = 0f;
    }

    public boolean actualizarSecuenciaAtaque(float delta) {
        if (estado != EstadoEnemigo.ATACAR) {
            return false;
        }

        tiempoFaseAtaque -= delta;

        if (tiempoFaseAtaque > 0f) {
            return false;
        }

        if (!golpeEjecutado) {
            golpeEjecutado = true;
            tiempoFaseAtaque = tiempoRecuperacionAtaque;
            return true;
        }

        estado = EstadoEnemigo.PERSEGUIR;
        return false;
    }

    // --- MÉTODOS DE ANIMACIÓN Y RECORTE DE `bicho.jpg` ---

    public void actualizarAnimacionYFrame(float delta, boolean jugadorALaDerecha) {
        this.mirandoDerecha = jugadorALaDerecha;
        tiempoAnimacion += delta;
        int baseOffset = mirandoDerecha ? 13 : 0;

        switch (estado) {
            case QUIETO:
                frameActual = 1 + baseOffset;
                break;

            case PERSEGUIR:
                if (tiempoAnimacion >= velocidadAnimacionCaminata) {
                    tiempoAnimacion = 0f;
                    frameActual++;
                    if (frameActual < 2 + baseOffset || frameActual > 6 + baseOffset) {
                        frameActual = 2 + baseOffset;
                    }
                }
                if (frameActual < 2 + baseOffset || frameActual > 6 + baseOffset) {
                    frameActual = 2 + baseOffset;
                }
                break;

            case ATACAR:
                float progresoAtaque = 1.0f - (tiempoFaseAtaque / (golpeEjecutado ? tiempoRecuperacionAtaque : tiempoPreparacionAtaque));
                progresoAtaque = Math.max(0f, Math.min(1.0f, progresoAtaque));

                if (!golpeEjecutado) {
                    frameActual = 7 + (int)(progresoAtaque * 3.99f);
                } else {
                    frameActual = 11 + (int)(progresoAtaque * 2.99f);
                    frameActual = Math.min(13, frameActual);
                }
                frameActual += baseOffset;
                break;

            default:
                frameActual = 1 + baseOffset;
                break;
        }
    }

    public int getSourceX(int anchoSpriteSheet) {
        int anchoFrame = anchoSpriteSheet / numColumnas;
        int indiceBase0 = frameActual - 1;
        int columna = indiceBase0 % numColumnas;
        return columna * anchoFrame;
    }

    public int getSourceY(int altoSpriteSheet) {
        int altoFrame = altoSpriteSheet / numFilas;
        int indiceBase0 = frameActual - 1;
        int fila = indiceBase0 / numColumnas;
        // Corrección: Recorte estándar desde la parte superior hacia abajo
        return fila * altoFrame;
    }

    public int getAnchoFrame(int anchoSpriteSheet) {
        return anchoSpriteSheet / numColumnas;
    }

    public int getAltoFrame(int altoSpriteSheet) {
        return altoSpriteSheet / numFilas;
    }
}
