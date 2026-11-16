package io.github.escuela_tecnica_n35;

import java.util.ArrayList;

import io.github.escuela_tecnica_n35.entidades.*;
import io.github.escuela_tecnica_n35.etapas.*;
import io.github.escuela_tecnica_n35.juego.Juego;

public class DatosPartida {

    public int version = 1;
    public String nombreEtapa;
    public int monedas;
    public long fecha;

    public Jugador jugador;
    public Etapa etapa;

    public int fila;
    public int columna;

    public ArrayList<Item> poolItems;
    public ArrayList<Item> poolGenerador;
    public ArrayList<Bioma> biomasDisponibles;

    public float velocidadX;
    public float velocidadY;
    public float tiempoInvulnerable;

    public int saltosDisponibles;

    public boolean haciendoDash;
    public int dashDisponibleAire;

    public float tiempoDashRestante;
    public float tiempoCooldownDash;

    public int direccionDash;

    public boolean mirandoIzquierda;
    public boolean transicionBloqueada;

    public static DatosPartida capturar(Juego juego) {

        DatosPartida datos = new DatosPartida();

        datos.jugador = juego.getJugador();
        datos.etapa = juego.getEtapaActual();

        datos.nombreEtapa = datos.etapa.getNombre();
        datos.monedas = datos.jugador.getMonedas();
        datos.fecha = System.currentTimeMillis();

        datos.fila = juego.getSalaActual().getFila();
        datos.columna = juego.getSalaActual().getColumna();

        datos.poolItems =
            juego.getPoolItemsGuardado();

        datos.poolGenerador =
            juego.getGeneradorGuardado()
                .getPoolItemsGuardado();

        datos.biomasDisponibles =
            juego.getGeneradorGuardado()
                .getBiomasGuardado();

        return datos;
    }
}