package io.github.escuela_tecnica_n35.etapas;

import java.util.Random;

public class GeneracionEtapa {

	public Etapa generarEtapa(int numeroPiso) {

	    if (numeroPiso == 4) {
	        return generarPisoFinal();
	    }

	    Random random = new Random();
	    int variante = random.nextInt(2);

	    if (variante == 0) {
	        return generarVarianteA(numeroPiso);
	    } else {
	        return generarVarianteB(numeroPiso);
	    }
	}
    
	private Etapa generarVarianteA(int numeroPiso) {
	    // Generar enemigos de la variante A
	    // Crear jefe correspondiente

	    return null;
	}

	private Etapa generarVarianteB(int numeroPiso) {
	    // Generar enemigos de la variante B
	    // Crear jefe correspondiente

	    return null;
	}

	private Etapa generarPisoFinal() {
	    // Crear enemigos del piso 4
	    // Crear jefe final

	    return null;
	}
	
}