package io.github.escuela_tecnica_n35.entidades;

public class EnemigoVolador extends Enemigo {

    private float tiempoAnimacionVuelo = 0f;
    private float velocidadAnimacionVuelo = 0.1f;

    // Estados de animación para la mosca (matriz 3x4 = 12 frames)
    private enum EstadoVuelo {
        VOLANDO_IZQUIERDA,  // Frames 1 al 4
        GIRANDO_A_DERECHA,  // Frames 6 y 5
        VOLANDO_DERECHA,    // Frames 7 al 10
        GIRANDO_A_IZQUIERDA // Frames 12 y 11
    }

    private EstadoVuelo estadoVuelo = EstadoVuelo.VOLANDO_IZQUIERDA;
    private int pasoGiro = 0;

    public EnemigoVolador(
        String nombre,
        Posicion posicion,
        int vidaMax,
        float velocidad,
        int daño,
        int monedasMinimas,
        int monedasMaximas) {

        super(
            nombre,
            posicion,
            vidaMax,
            velocidad,
            daño,
            monedasMinimas,
            monedasMaximas,
            new Hitbox(70f, 70f, 0f, 0f)
        );

        // CONFIGURACIÓN EXACTA DE LA MATRIZ DE LA MOSCA (3 columnas y 4 filas)
        this.numColumnas = 3;
        this.numFilas = 4;
        this.frameActual = 1;
    }

    public void perseguir(Posicion objetivo, float delta) {
        float distanciaX = objetivo.getX() - getPosicion().getX();
        float distanciaY = objetivo.getY() - getPosicion().getY();

        float distancia = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        if (distancia == 0) {
            return;
        }

        float direccionX = distanciaX / distancia;
        float direccionY = distanciaY / distancia;

        getPosicion().moverX(direccionX * getVelocidad() * delta);
        getPosicion().moverY(direccionY * getVelocidad() * delta);

        // Detectar cambios de dirección en X para activar los giros exactos
        if (direccionX > 0) {
            if (!isMirandoDerecha()) {
                setMirandoDerecha(true);
                if (estadoVuelo == EstadoVuelo.VOLANDO_IZQUIERDA || (frameActual >= 1 && frameActual <= 4)) {
                    estadoVuelo = EstadoVuelo.GIRANDO_A_DERECHA;
                    pasoGiro = 0;
                }
            }
        } else if (direccionX < 0) {
            if (isMirandoDerecha()) {
                setMirandoDerecha(false);
                if (estadoVuelo == EstadoVuelo.VOLANDO_DERECHA || (frameActual >= 7 && frameActual <= 10)) {
                    estadoVuelo = EstadoVuelo.GIRANDO_A_IZQUIERDA;
                    pasoGiro = 0;
                }
            }
        }
    }

    public void actualizarAnimacionVoladora(float delta) {
        tiempoAnimacionVuelo += delta;

        if (tiempoAnimacionVuelo >= velocidadAnimacionVuelo) {
            tiempoAnimacionVuelo = 0f;

            switch (estadoVuelo) {
                case VOLANDO_IZQUIERDA:
                    // Frames 1 al 4: vuelo hacia la izquierda
                    frameActual++;
                    if (frameActual < 1 || frameActual > 4) {
                        frameActual = 1;
                    }
                    break;

                case GIRANDO_A_DERECHA:
                    // Frames 6 y 5 (en ese orden) para voltearse hacia la derecha
                    if (pasoGiro == 0) {
                        frameActual = 6;
                        pasoGiro = 1;
                    } else if (pasoGiro == 1) {
                        frameActual = 5;
                        pasoGiro = 2;
                    } else {
                        estadoVuelo = EstadoVuelo.VOLANDO_DERECHA;
                        frameActual = 7;
                    }
                    break;

                case VOLANDO_DERECHA:
                    // Frames 7 a 10: vuelo hacia la derecha
                    frameActual++;
                    if (frameActual < 7 || frameActual > 10) {
                        frameActual = 7;
                    }
                    break;

                case GIRANDO_A_IZQUIERDA:
                    // Frames 12 y 11 (en ese orden) para voltearse hacia la izquierda
                    if (pasoGiro == 0) {
                        frameActual = 12;
                        pasoGiro = 1;
                    } else if (pasoGiro == 1) {
                        frameActual = 11;
                        pasoGiro = 2;
                    } else {
                        estadoVuelo = EstadoVuelo.VOLANDO_IZQUIERDA;
                        frameActual = 1;
                    }
                    break;
            }
        }
    }
}
