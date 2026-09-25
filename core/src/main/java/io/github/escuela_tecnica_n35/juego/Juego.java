package io.github.escuela_tecnica_n35.juego;

import io.github.escuela_tecnica_n35.entidades.Jugador;
import io.github.escuela_tecnica_n35.etapas.Etapa;
import io.github.escuela_tecnica_n35.etapas.GeneracionEtapa;
import io.github.escuela_tecnica_n35.etapas.Sala;

import io.github.escuela_tecnica_n35.etapas.TipoSala;

public class Juego {

    private Jugador jugador;
    private Etapa etapaActual;
    private GeneracionEtapa generador;

    // NUEVO:
    // Guarda la habitación en la que estamos actualmente.
    private Sala salaActual;

    public Juego(Jugador jugador) {

        this.jugador = jugador;
        this.generador = new GeneracionEtapa();
    }


    public void iniciarJuego() {

        etapaActual = generador.generarEtapa(1);

        // Cada piso siempre comienza en la sala INICIAL.
        salaActual = buscarSalaInicial(
            etapaActual.getMapa()
        );

        if (salaActual == null) {

            throw new IllegalStateException(
                "La etapa generada no contiene una sala INICIAL."
            );
        }

        anunciarSalaActual();
    }


    public void avanzarEtapa(int numeroEtapa) {

        etapaActual =
            generador.generarEtapa(numeroEtapa);

        salaActual =
            buscarSalaInicial(etapaActual.getMapa());

        if (salaActual == null) {

            throw new IllegalStateException(
                "La etapa generada no contiene una sala INICIAL."
            );
        }

        anunciarSalaActual();
    }


    // --------------------------------------------------
    // MOVIMIENTO ENTRE SALAS
    // --------------------------------------------------

    public boolean haySalaEnDireccion(
            int cambioFila,
            int cambioColumna) {

        Sala[][] mapa = etapaActual.getMapa();

        int nuevaFila =
            salaActual.getFila() + cambioFila;

        int nuevaColumna =
            salaActual.getColumna() + cambioColumna;


        // Primero verificamos que la coordenada
        // siga estando dentro de la matriz.
        if (nuevaFila < 0 ||
            nuevaFila >= mapa.length ||
            nuevaColumna < 0 ||
            nuevaColumna >= mapa[nuevaFila].length) {

            return false;
        }


        // Si la posición existe pero contiene null,
        // entonces tampoco hay habitación.
        return mapa[nuevaFila][nuevaColumna] != null;
    }


    public boolean cambiarSala(
            int cambioFila,
            int cambioColumna) {

        // Si quedan enemigos vivos,
        // no permitimos abandonar la habitación.
        if (!salaActual.puedeSalir()) {

            System.out.println(
                "No podés salir: todavía quedan enemigos."
            );

            return false;
        }


        // Comprobamos que realmente exista
        // una sala en esa dirección.
        if (!haySalaEnDireccion(
                cambioFila,
                cambioColumna)) {

            return false;
        }


        Sala[][] mapa = etapaActual.getMapa();

        int nuevaFila =
            salaActual.getFila() + cambioFila;

        int nuevaColumna =
            salaActual.getColumna() + cambioColumna;


        // Cambiamos la sala actual.
        salaActual =
            mapa[nuevaFila][nuevaColumna];


        anunciarSalaActual();

        return true;
    }


    // --------------------------------------------------
    // SALA INICIAL
    // --------------------------------------------------

    private Sala buscarSalaInicial(Sala[][] mapa) {

        for (int fila = 0;
             fila < mapa.length;
             fila++) {

            for (int columna = 0;
                 columna < mapa[fila].length;
                 columna++) {

                Sala sala = mapa[fila][columna];

                if (sala != null &&
                	    sala.getTipo() == TipoSala.INICIAL) {

                	    return sala;
                	}
            }
        }

        return null;
    }


    // --------------------------------------------------
    // MENSAJE AL ENTRAR A UNA SALA
    // --------------------------------------------------

    private void anunciarSalaActual() {

        System.out.println();
        System.out.println(
            "Sala [" +
            salaActual.getFila() +
            "][" +
            salaActual.getColumna() +
            "]"
        );


        switch (salaActual.getTipo()) {

            case INICIAL:

                System.out.println(
                    "Estás en la sala INICIAL."
                );

                break;


            case ENEMIGOS:

                System.out.println(
                    "Llegaste a una sala de ENEMIGOS."
                );

                break;


            case ITEM:

                System.out.println(
                    "Llegaste a la sala de OBJETO."
                );

                break;


            case TIENDA:

                System.out.println(
                    "Llegaste a la TIENDA."
                );

                break;


            case JEFE:

                System.out.println(
                    "Llegaste a la sala del JEFE."
                );

                break;
        }
    }


    // --------------------------------------------------
    // GETTERS
    // --------------------------------------------------

    public Jugador getJugador() {
        return jugador;
    }

    public Etapa getEtapaActual() {
        return etapaActual;
    }

    public Sala getSalaActual() {
        return salaActual;
    }
}