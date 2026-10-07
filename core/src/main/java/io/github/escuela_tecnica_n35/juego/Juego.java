package io.github.escuela_tecnica_n35.juego;

import java.util.ArrayList;
import java.util.Random;

import io.github.escuela_tecnica_n35.entidades.Jugador;
import io.github.escuela_tecnica_n35.etapas.Etapa;
import io.github.escuela_tecnica_n35.etapas.GeneracionEtapa;
import io.github.escuela_tecnica_n35.etapas.Sala;
import io.github.escuela_tecnica_n35.entidades.Item;
import io.github.escuela_tecnica_n35.entidades.Posicion;
import io.github.escuela_tecnica_n35.entidades.TipoItem;
import io.github.escuela_tecnica_n35.entidades.TipoModificadorItem;

import io.github.escuela_tecnica_n35.etapas.TipoSala;

public class Juego {

    private Jugador jugador;
    private Etapa etapaActual;
    private GeneracionEtapa generador;
    
    // NUEVO:
    // Guarda la habitación en la que estamos actualmente.
    private Sala salaActual;
    
    // Items que todavía pueden aparecer durante esta partida.
    private ArrayList<Item> poolItems;

    private Random random;
    
    public Juego(Jugador jugador) {

        this.jugador = jugador;
        this.generador = new GeneracionEtapa();
        
        poolItems = new ArrayList<Item>();

        random = new Random();

        cargarPoolItems();
    }


    public void iniciarJuego() {

    	// Elegimos el item que aparecerá en la sala ITEM.
    	// Al sacarlo también desaparece de la pool.
    	Item itemSala =
    	    sacarItemAleatorio();

    	etapaActual =
    	    generador.generarEtapa(
    	        1,
    	        itemSala
    	    );

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

    	Item itemSala =
    		    sacarItemAleatorio();

    	generador.generarEtapa(
    		    numeroEtapa,
    		    itemSala
    		);

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
    
    private void cargarPoolItems() {

        poolItems.add(
            new Item(
                "Corazon Amplificado",
                new Posicion(600, 100),
                "Aumenta la vida maxima.",
                1,
                TipoItem.PASIVO,
                TipoModificadorItem.VIDA_MAX,
                20
            )
        );


        poolItems.add(
            new Item(
                "Botas Ligeras",
                new Posicion(600, 100),
                "Aumenta la velocidad de movimiento.",
                1,
                TipoItem.PASIVO,
                TipoModificadorItem.VELOCIDAD,
                30
            )
        );


        poolItems.add(
            new Item(
                "Pua Afilada",
                new Posicion(600, 100),
                "Aumenta el daño del ataque normal.",
                1,
                TipoItem.ARMA,
                TipoModificadorItem.DAÑO_ATAQUE,
                2
            )
        );
    }
    
    private Item sacarItemAleatorio() {

        if (poolItems.isEmpty()) {

            return null;
        }


        int indice =
            random.nextInt(poolItems.size());


        // Lo obtenemos y lo eliminamos de la pool
        // al mismo tiempo.
        return poolItems.remove(indice);
    }
}