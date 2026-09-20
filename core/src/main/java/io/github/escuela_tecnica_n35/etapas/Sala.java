package io.github.escuela_tecnica_n35.etapas;

public class Sala {

    private TipoSala tipo;

    public Sala(TipoSala tipo) {
        this.tipo = tipo;
    }

    public TipoSala getTipo() {
        return tipo;
    }
}