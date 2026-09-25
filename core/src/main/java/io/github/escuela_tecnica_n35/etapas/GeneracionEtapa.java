package io.github.escuela_tecnica_n35.etapas;

import java.util.Random;
import java.util.ArrayList;

import io.github.escuela_tecnica_n35.entidades.Enemigo;
import io.github.escuela_tecnica_n35.entidades.Jefe;

public class GeneracionEtapa {

    public Etapa generarEtapa(int numeroPiso) {

        if (numeroPiso == 4) {
            return generarPisoFinal();
        }

        Random random = new Random();

        int variante = random.nextInt(2);

        if (variante == 0) {
            return generarVarianteA(numeroPiso);
        }
        else {
            return generarVarianteB(numeroPiso);
        }
    }

    private Etapa generarVarianteA(int numeroPiso) {

        // Generamos la estructura completa del piso.
        Sala[][] mapa = generarMapa(numeroPiso);

        imprimirMapa(mapa);

        // Por ahora todavía no colocamos enemigos reales.
        ArrayList<Enemigo> enemigos =
            new ArrayList<Enemigo>();

        // El jefe concreto lo agregaremos más adelante.
        Jefe jefe = null;

        // IMPORTANTE:
        // Ahora devolvemos una Etapa real que contiene
        // el mapa que acabamos de generar.
        return new Etapa(
            "DISCOTERÍA " + numeroPiso + " - Variante A",
            numeroPiso,
            numeroPiso,
            enemigos,
            jefe,
            mapa
        );
    }

    private Etapa generarVarianteB(int numeroPiso) {

    	// Generamos la estructura completa del piso.
        Sala[][] mapa = generarMapa(numeroPiso);

        imprimirMapa(mapa);

        // Por ahora todavía no colocamos enemigos reales.
        ArrayList<Enemigo> enemigos =
            new ArrayList<Enemigo>();

        // El jefe concreto lo agregaremos más adelante.
        Jefe jefe = null;

        // IMPORTANTE:
        // Ahora devolvemos una Etapa real que contiene
        // el mapa que acabamos de generar.
        return new Etapa(
            "ROCK AND FALL " + numeroPiso + " - Variante B",
            numeroPiso,
            numeroPiso,
            enemigos,
            jefe,
            mapa
        );
    }

    private Etapa generarPisoFinal() {

        // El piso 4 será especial:
        // pasillo hasta la sala del jefe final.

        // Crear enemigos del piso 4
        // Crear jefe final

        return null;
    }

    // -------------------- TAMAÑO DEL MAPA --------------------

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

    // -------------------- CANTIDAD DE SALAS --------------------

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

    // -------------------- GENERACIÓN DEL MAPA --------------------

    private Sala[][] generarMapa(int numeroPiso) {

        Sala[][] mapa;

        do {
            // Generamos un mapa nuevo desde cero.
            mapa = generarEstructuraMapa(numeroPiso);

            // Si no tiene suficientes salas con una sola conexión,
            // el mapa se descarta y el do-while vuelve a intentarlo.
        }
        while (!mapaEsValido(mapa));

        // Recién cuando sabemos que la estructura sirve,
        // asignamos las salas especiales.
        asignarSalaJefe(mapa);
        asignarSalasEspeciales(mapa);

        return mapa;
    }
    
    // GENERA ESTRUCTURA DEL MAPA
    
    private Sala[][] generarEstructuraMapa(int numeroPiso) {

        int tamaño = obtenerTamañoMapa(numeroPiso);
        int maximoSalas = obtenerMaximoSalas(numeroPiso);

        Sala[][] mapa = new Sala[tamaño][tamaño];

        ArrayList<Sala> salasCreadas =
            new ArrayList<Sala>();

        // Comenzamos aproximadamente en el centro.
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

            // Elegimos aleatoriamente una sala ya existente.
            Sala salaOrigen =
                salasCreadas.get(
                    random.nextInt(salasCreadas.size())
                );

            int nuevaFila = salaOrigen.getFila();
            int nuevaColumna = salaOrigen.getColumna();

            // Elegimos una de las cuatro direcciones.
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

            // Comprobamos que la posición esté dentro del mapa.
            if (nuevaFila >= 0 &&
                nuevaFila < tamaño &&
                nuevaColumna >= 0 &&
                nuevaColumna < tamaño) {

                // Solamente creamos la sala si el espacio está vacío.
                if (mapa[nuevaFila][nuevaColumna] == null) {

                    Sala nuevaSala = new Sala(
                        nuevaFila,
                        nuevaColumna,
                        TipoSala.ENEMIGOS
                    );

                    mapa[nuevaFila][nuevaColumna] =
                        nuevaSala;

                    salasCreadas.add(nuevaSala);
                }
            }
        }

        return mapa;
    }
    
    // COMPRUEBA SI EL MAPA TIENE 3 SALAS CONECTADAS A UNA SALA NOMAS
    
    private boolean mapaEsValido(Sala[][] mapa) {

        int salasConUnVecino = 0;

        for (int fila = 0; fila < mapa.length; fila++) {

            for (int columna = 0;
                 columna < mapa[fila].length;
                 columna++) {

                Sala sala = mapa[fila][columna];

                if (sala != null &&
                    sala.getTipo() != TipoSala.INICIAL &&
                    contarVecinos(mapa, sala) == 1) {

                    salasConUnVecino++;
                }
            }
        }

        // Necesitamos como mínimo:
        // 1 JEFE + 1 ITEM + 1 TIENDA.
        return salasConUnVecino >= 3;
    }

    // ==========================================================
    // NUEVO: ASIGNACIÓN DE LA SALA DEL JEFE
    // ==========================================================

    private void asignarSalaJefe(Sala[][] mapa) {

        // Primero buscamos dónde está la sala INICIAL.
        Sala inicial = buscarSalaInicial(mapa);

        // "pendientes" contiene las salas que todavía debemos revisar.
        ArrayList<Sala> pendientes = new ArrayList<Sala>();

        // "visitadas" contiene las salas que ya revisamos.
        // Esto evita volver constantemente a una misma habitación.
        ArrayList<Sala> visitadas = new ArrayList<Sala>();

        // Empezamos el recorrido desde la sala inicial.
        inicial.setDistanciaInicial(0);
        pendientes.add(inicial);

        // Al principio, la única sala que conocemos es la inicial.
        // Después esta variable irá guardando la más lejana encontrada.
        Sala masLejana = null;

        while (!pendientes.isEmpty()) {

            // Sacamos la primera sala pendiente para analizarla.
            Sala actual = pendientes.remove(0);

            // Marcamos que ya fue revisada.
            visitadas.add(actual);

            // Solo consideramos salas que sean un extremo del mapa:
            // deben tener exactamente una sala vecina.
            if (contarVecinos(mapa, actual) == 1) {

            	// Si todavía no encontramos ninguna candidata,
            	// esta pasa a ser la primera.
            	//
            	// Si ya tenemos una, solamente la reemplazamos
            	// cuando encontramos otra más lejana.
            	if (masLejana == null ||
            			actual.getDistanciaInicial() > masLejana.getDistanciaInicial()) {

            		masLejana = actual;
            	}
            }

            // REVISAMOS LOS CUATRO VECINOS DE "actual".
            //
            // Si actual estuviera en [2][2]:
            //
            //              [1][2]
            //                 ↑
            //      [2][1] ← [2][2] → [2][3]
            //                 ↓
            //              [3][2]

            // Arriba
            agregarVecino(
                mapa,
                actual.getFila() - 1,
                actual.getColumna(),
                actual,
                pendientes,
                visitadas
            );

            // Abajo
            agregarVecino(
                mapa,
                actual.getFila() + 1,
                actual.getColumna(),
                actual,
                pendientes,
                visitadas
            );

            // Izquierda
            agregarVecino(
                mapa,
                actual.getFila(),
                actual.getColumna() - 1,
                actual,
                pendientes,
                visitadas
            );

            // Derecha
            agregarVecino(
                mapa,
                actual.getFila(),
                actual.getColumna() + 1,
                actual,
                pendientes,
                visitadas
            );
        }

        // Cuando terminamos de recorrer el mapa,
        // "masLejana" contiene una de las salas que requiere
        // mayor cantidad de movimientos desde la inicial.
        if (masLejana != null) {
            masLejana.setTipo(TipoSala.JEFE);
        }
    }

    // ==========================================================
    // NUEVO: REVISAR UN VECINO
    // ==========================================================

    private void agregarVecino(
            Sala[][] mapa,
            int fila,
            int columna,
            Sala actual,
            ArrayList<Sala> pendientes,
            ArrayList<Sala> visitadas) {

        // PASO 1:
        // Antes de intentar mapa[fila][columna], comprobamos
        // que esas coordenadas existan.
        //
        // Por ejemplo, [-1][2] no existe.
        if (fila < 0 ||
            fila >= mapa.length ||
            columna < 0 ||
            columna >= mapa[fila].length) {

            return;
        }

        // PASO 2:
        // Obtenemos lo que haya en esa posición.
        Sala vecino = mapa[fila][columna];

        // Si es null significa que en esa posición de la matriz
        // no existe ninguna habitación.
        //
        // Por ejemplo:
        //
        // . . .
        // . S .
        // . . .
        //
        // Los puntos serían null.
        if (vecino == null) {
            return;
        }

        // PASO 3:
        // Si ya visitamos esa sala O ya está esperando para
        // ser visitada, no volvemos a agregarla.
        //
        // Esto es MUY importante porque dos habitaciones
        // diferentes podrían encontrar al mismo vecino.
        if (visitadas.contains(vecino) ||
            pendientes.contains(vecino)) {

            return;
        }

        // PASO 4:
        // Si llegamos hasta acá significa:
        //
        // - la coordenada existe;
        // - hay una sala;
        // - todavía no fue recorrida.
        //
        // Como es vecina de "actual", está exactamente
        // una habitación más lejos de la inicial.
        vecino.setDistanciaInicial(
            actual.getDistanciaInicial() + 1
        );

        // PASO 5:
        // No la procesamos ahora mismo.
        // La ponemos en pendientes para analizarla después.
        pendientes.add(vecino);
    }

    // ==========================================================
    // NUEVO: BUSCAR LA SALA INICIAL
    // ==========================================================

    private Sala buscarSalaInicial(Sala[][] mapa) {

        // Recorremos todas las filas.
        for (int fila = 0; fila < mapa.length; fila++) {

            // Recorremos todas las columnas de esa fila.
            for (int columna = 0;
                 columna < mapa[fila].length;
                 columna++) {

                Sala sala = mapa[fila][columna];

                // Primero comprobamos que exista una sala.
                // Después comprobamos si es la INICIAL.
                if (sala != null &&
                    sala.getTipo() == TipoSala.INICIAL) {

                    return sala;
                }
            }
        }

        // En teoría nunca debería ocurrir porque nosotros
        // siempre creamos una sala inicial.
        return null;
    }

    // ==========================================================
    // MÉTODO PROVISIONAL PARA VISUALIZAR EL MAPA
    // ==========================================================

    private void imprimirMapa(Sala[][] mapa) {

        for (int fila = 0; fila < mapa.length; fila++) {

            for (int columna = 0;
                 columna < mapa[fila].length;
                 columna++) {

            	if (mapa[fila][columna] == null) {

            	    System.out.print(". ");
            	}
            	else if (mapa[fila][columna].getTipo()
            	        == TipoSala.INICIAL) {

            	    System.out.print("I ");
            	}
            	else if (mapa[fila][columna].getTipo()
            	        == TipoSala.JEFE) {

            	    System.out.print("J ");
            	}
            	else if (mapa[fila][columna].getTipo()
            	        == TipoSala.ITEM) {

            	    System.out.print("O ");
            	}
            	else if (mapa[fila][columna].getTipo()
            	        == TipoSala.TIENDA) {

            	    System.out.print("T ");
            	}
            	else {

            	    System.out.print("S ");
            	}
            }

            System.out.println();
        }
    }
    
	// NUEVO:
	// Cuenta cuántas salas existen directamente alrededor de una sala.
	// La sala del jefe deberá tener exactamente 1 vecino.
	private int contarVecinos(Sala[][] mapa, Sala sala) {
	
	    int cantidadVecinos = 0;
	
	    int fila = sala.getFila();
	    int columna = sala.getColumna();
	
	    // Revisamos ARRIBA
	    if (fila - 1 >= 0 &&
	        mapa[fila - 1][columna] != null) {
	
	        cantidadVecinos++;
	    }
	
	    // Revisamos ABAJO
	    if (fila + 1 < mapa.length &&
	        mapa[fila + 1][columna] != null) {
	
	        cantidadVecinos++;
	    }
	
	    // Revisamos IZQUIERDA
	    if (columna - 1 >= 0 &&
	        mapa[fila][columna - 1] != null) {
	
	        cantidadVecinos++;
	    }
	
	    // Revisamos DERECHA
	    if (columna + 1 < mapa[fila].length &&
	        mapa[fila][columna + 1] != null) {
	
	    	cantidadVecinos++;
	    }
	
	    return cantidadVecinos;
	}
	
	// NUEVO:
	// Como la sala JEFE tiene exactamente un vecino,
	// este método busca esa única sala conectada a ella.
	private Sala buscarUnicoVecino(Sala[][] mapa, Sala sala) {

	    int fila = sala.getFila();
	    int columna = sala.getColumna();

	    // Arriba
	    if (fila - 1 >= 0 &&
	        mapa[fila - 1][columna] != null) {

	        return mapa[fila - 1][columna];
	    }

	    // Abajo
	    if (fila + 1 < mapa.length &&
	        mapa[fila + 1][columna] != null) {

	        return mapa[fila + 1][columna];
	    }

	    // Izquierda
	    if (columna - 1 >= 0 &&
	        mapa[fila][columna - 1] != null) {

	        return mapa[fila][columna - 1];
	    }

	    // Derecha
	    if (columna + 1 < mapa[fila].length &&
	        mapa[fila][columna + 1] != null) {

	        return mapa[fila][columna + 1];
	    }

	    return null;
	}
	
	// NUEVO:
	// Busca todas las salas que pueden convertirse
	// en ITEM o TIENDA.
	private ArrayList<Sala> buscarSalasEspecialesDisponibles(Sala[][] mapa) {

	    ArrayList<Sala> disponibles = new ArrayList<Sala>();

	    Sala salaJefe = null;

	    // Primero buscamos la sala JEFE.
	    for (int fila = 0; fila < mapa.length; fila++) {

	        for (int columna = 0;
	             columna < mapa[fila].length;
	             columna++) {

	            Sala sala = mapa[fila][columna];

	            if (sala != null &&
	                sala.getTipo() == TipoSala.JEFE) {

	                salaJefe = sala;
	            }
	        }
	    }

	    // Buscamos la única sala conectada al jefe.
	    // Esta sala debe permanecer obligatoriamente como ENEMIGOS.
	    Sala vecinoJefe = buscarUnicoVecino(mapa, salaJefe);

	    // Ahora recorremos todo el mapa buscando candidatos.
	    for (int fila = 0; fila < mapa.length; fila++) {

	        for (int columna = 0;
	             columna < mapa[fila].length;
	             columna++) {

	            Sala sala = mapa[fila][columna];

	            if (sala != null &&

	                // Debe seguir siendo una sala normal.
	                sala.getTipo() == TipoSala.ENEMIGOS &&

	                // Debe ser un callejón sin salida.
	                contarVecinos(mapa, sala) == 1 &&

	                // No puede ser la sala que conecta con el jefe.
	                sala != vecinoJefe) {

	                disponibles.add(sala);
	            }
	        }
	    }

	    return disponibles;
	}
	
	// NUEVO:
	// Asigna exactamente una sala ITEM y una TIENDA.
	private void asignarSalasEspeciales(Sala[][] mapa) {

	    ArrayList<Sala> disponibles =
	        buscarSalasEspecialesDisponibles(mapa);

	    // Necesitamos como mínimo dos salas:
	    // una para ITEM y otra para TIENDA.
	    if (disponibles.size() < 2) {

	        System.out.println(
	            "No hay suficientes salas disponibles para ITEM y TIENDA"
	        );

	        return;
	    }

	    Random random = new Random();

	    // ---------------- ITEM ----------------

	    int indiceItem = random.nextInt(disponibles.size());

	    Sala salaItem = disponibles.get(indiceItem);

	    salaItem.setTipo(TipoSala.ITEM);

	    // La eliminamos de disponibles para que la TIENDA
	    // no pueda elegir exactamente la misma habitación.
	    disponibles.remove(salaItem);


	    // ---------------- TIENDA ----------------

	    int indiceTienda = random.nextInt(disponibles.size());

	    Sala salaTienda = disponibles.get(indiceTienda);

	    salaTienda.setTipo(TipoSala.TIENDA);
	}
}