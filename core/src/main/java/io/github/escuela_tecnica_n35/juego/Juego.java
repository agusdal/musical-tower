package juego;

import entidades.Jugador;
import etapas.Etapa;
import etapas.GeneracionEtapa;

public class Juego {

    private Jugador jugador;
    private Etapa etapaActual;
    private GeneracionEtapa generador;

    public Juego(Jugador jugador) {
        this.jugador = jugador;
        this.generador = new GeneracionEtapa();
    }

    public void iniciarJuego() {
        etapaActual = generador.generarEtapa(1);
    }

    public void avanzarEtapa(int numeroEtapa) {
        etapaActual = generador.generarEtapa(numeroEtapa);
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Etapa getEtapaActual() {
        return etapaActual;
    }
}