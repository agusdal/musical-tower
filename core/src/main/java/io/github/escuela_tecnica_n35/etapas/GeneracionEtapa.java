package io.github.escuela_tecnica_n35.etapas;

import java.util.Random;
import java.util.ArrayList;

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
		
		Sala[][] mapa = generarMapa(numeroPiso);
		
		imprimirMapa(mapa);
	    // Más adelante:
	    // crear enemigos exclusivos de esta etapa
	    // crear jefe de esta variante
		
	    return null;
	}

	private Etapa generarVarianteB(int numeroPiso) {
		
		Sala[][] mapa = generarMapa(numeroPiso);
		
		imprimirMapa(mapa);
	    // Más adelante:
	    // crear enemigos exclusivos de esta etapa
	    // crear jefe de esta variante
		
	    return null;
	}

	private Etapa generarPisoFinal() {
	    // Crear enemigos del piso 4
	    // Crear jefe final

	    return null;
	}
	
	private int obtenerTamañoMapa(int numeroPiso) {

	    if (numeroPiso == 1) {
	        return 5;
	    }

	    if (numeroPiso == 2) {
	        return 7;
	    }

	    if (numeroPiso == 3) {
	        return 10;
	    }

	    return 0;
	}

	private int obtenerMaximoSalas(int numeroPiso) {

	    if (numeroPiso == 1) {
	        return 9;
	    }

	    if (numeroPiso == 2) {
	        return 13;
	    }

	    if (numeroPiso == 3) {
	        return 18;
	    }

	    return 0;
	}
	
	private Sala[][] generarMapa(int numeroPiso) {

	    int tamaño = obtenerTamañoMapa(numeroPiso);
	    int maximoSalas = obtenerMaximoSalas(numeroPiso);

	    Sala[][] mapa = new Sala[tamaño][tamaño];

	    ArrayList<Sala> salasCreadas = new ArrayList<Sala>();

	    // Comenzamos aproximadamente en el centro
	    int filaInicial = tamaño / 2;
	    int columnaInicial = tamaño / 2;

	    Sala salaInicial = new Sala(
	        filaInicial,
	        columnaInicial,
	        TipoSala.INICIAL
	    );

	    mapa[filaInicial][columnaInicial] = salaInicial;
	    salasCreadas.add(salaInicial);

	    Random random = new Random();

	    while (salasCreadas.size() < maximoSalas) {

	        // Elegimos una sala existente
	        Sala salaOrigen =
	            salasCreadas.get(random.nextInt(salasCreadas.size()));

	        int nuevaFila = salaOrigen.getFila();
	        int nuevaColumna = salaOrigen.getColumna();

	        // Elegimos una dirección
	        int direccion = random.nextInt(4);

	        if (direccion == 0) {
	            nuevaFila--;
	        }
	        else if (direccion == 1) {
	            nuevaFila++;
	        }
	        else if (direccion == 2) {
	            nuevaColumna--;
	        }
	        else {
	            nuevaColumna++;
	        }

	        // Comprobamos que siga dentro de la matriz
	        if (nuevaFila >= 0 &&
	            nuevaFila < tamaño &&
	            nuevaColumna >= 0 &&
	            nuevaColumna < tamaño) {

	            // Comprobamos que esa posición esté vacía
	            if (mapa[nuevaFila][nuevaColumna] == null) {

	                Sala nuevaSala = new Sala(
	                    nuevaFila,
	                    nuevaColumna,
	                    TipoSala.ENEMIGOS
	                );

	                mapa[nuevaFila][nuevaColumna] = nuevaSala;
	                salasCreadas.add(nuevaSala);
	            }
	        }
	    }

	    return mapa;
	}
	
	private void imprimirMapa(Sala[][] mapa) { // METODO PROVISIONAL PARA VER SI FUNCIONA EL MAPA

	    for (int fila = 0; fila < mapa.length; fila++) {

	        for (int columna = 0; columna < mapa[fila].length; columna++) {

	            if (mapa[fila][columna] == null) {
	                System.out.print(". ");
	            }
	            else if (mapa[fila][columna].getTipo() == TipoSala.INICIAL) {
	                System.out.print("I ");
	            }
	            else {
	                System.out.print("S ");
	            }
	        }

	        System.out.println();
	    }
	}
	
}