# Changelog — Musical Tower

Todos los cambios significativos de Musical Tower se documentan en este archivo.

El formato toma como referencia [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/). Este registro reconstruye la evolución del proyecto a partir de los 77 commits del historial de `main`, desde el 24 de agosto hasta el 13 de noviembre de 2026, y de los trabajos preparados en la carpeta del proyecto hasta el 15 de noviembre de 2026.

Las fechas del historial corresponden a la fecha de autor de cada commit, expresada en horario de Argentina (UTC−3). Por eso algunos cambios pueden figurar un día antes que en una vista de GitHub configurada en UTC. Las integraciones de ramas se conservan en el índice de commits, sin contarlas como funcionalidades nuevas.

Se mantiene la versión `0.1.0` del changelog original y la denominación «beta» del último commit. Las demás entradas se identifican por fecha, sin asignarles números de versión retroactivos.

## [Sin publicar]

### Documentación

- Reconstrucción completa de este changelog: movimiento, combate, enemigos, jefes, generación de etapas, objetos, interfaz, menús, audio y entregas locales.
- Incorporación de un índice con los 77 commits revisados, incluidas las integraciones de ramas.
- Separación entre cambios registrados en GitHub y trabajos locales cuya publicación no queda acreditada por el historial.

### Entregas y comprobaciones locales

Los siguientes trabajos se prepararon durante el desarrollo en la carpeta del proyecto. Esta sección describe esas entregas; no implica que cada archivo o paquete se haya incorporado posteriormente a GitHub.

- Corrección de comentarios dañados de `Main_game.java`, conservando el código ejecutable. La corrección de comentarios también quedó reflejada en el commit del 7 de noviembre.
- Preparación de cambios para conectar las estadísticas de pogo, dash, tamaño del ataque y tiempo entre ataques con los objetos del jugador. Su incorporación funcional quedó registrada el 10 de noviembre.
- Resolución local de cinco conflictos de `Main_game.java` para combinar el menú principal con los cambios de recursos y lógica de la otra rama. El resultado se comprobó mediante compilación temporal y la integración quedó registrada el 12 de noviembre.
- Diagnóstico del fondo gris al abrir el volumen desde pausa: separación de la actualización del submenú y su dibujo. La copia beta revisada ya contiene el tratamiento de `viendoVolumen` dentro de los métodos de actualización y dibujo del menú de pausa.
- Diagnóstico de `disco.mp3` y `jazz.mp3`: los originales analizados contenían audio AAC en un contenedor MP4 pese a su extensión.
- Conversión local de esas dos canciones a MP3 real, estéreo, 44,1 kHz y 192 kb/s, y entrega en `canciones-mp3-corregidas.zip`. Se verificó su decodificación completa con el decodificador utilizado por el juego; las otras seis pistas analizadas también se decodificaron correctamente. No se da por confirmada la sustitución de los archivos del repositorio.
- Entrega de `musical-tower-sin-comentarios.zip`, basado en `0d9cfbb`: proyecto de 85 archivos, con 513 comentarios retirados de 29 archivos Java.
- Entrega de `musical-tower-beta-sin-comentarios.zip`, basado en `de4928a`: proyecto de 101 archivos, con 514 comentarios retirados de 32 archivos Java.
- En ambas copias se retiraron comentarios de línea, de bloque y Javadoc del código Java. Se conservaron los demás archivos, incluidos recursos, configuración y herramientas de construcción.
- Comprobación de las copias sin comentarios: compilación del código original y limpio de los módulos `core` y `lwjgl3`, comparación del bytecode sin información de depuración y verificación del contenido de los ZIP. Se obtuvieron resultados equivalentes para las 36 clases de la primera copia y las 40 de la beta. Estas comprobaciones no sustituyen una prueba completa de todas las situaciones durante una partida.

## [Beta] — 2026-11-13

Base de esta entrega: [`de4928a`](https://github.com/agusdal/musical-tower/commit/de4928a69f51166499e097e8f1caaf8770898551).

### Añadido

- Menú de victoria al derrotar al jefe final, con las opciones de volver a jugar, regresar al menú principal y salir del juego.
- Diseño gráfico de la pantalla de victoria y recursos visuales para los jefes.
- Música reproducida mediante `GestorMusica`, con una canción para el menú y siete asociaciones musicales para las variantes de etapa.
- Correspondencia entre etapas y pistas:
  - DISCOTERÍA: `musica/disco.mp3`.
  - ROCK AND FALL: `musica/rock.mp3`.
  - FIESTA SHOCKEANTE: `musica/electro.mp3`.
  - FLOTANDO EN NUBES: `musica/reggae.mp3`.
  - BASE Y COMPÁS: `musica/rap.mp3`.
  - WHIPLASH: `musica/jazz.mp3`.
  - PISO FINAL: `musica/final.mp3`.
  - Menú principal: `musica/menu.mp3`.
- Reproducción en bucle y cambio de pista al cambiar de etapa o volver al menú.
- Menú de volumen accesible desde el menú principal y desde pausa.
- Volumen por puntos enteros de 0 a 10: 0 silencia la música y 10 aplica el volumen máximo del reproductor.
- Guardado del volumen mediante preferencias, con valor inicial de 5 cuando no existe una configuración previa.
- Fondos seleccionados aleatoriamente para las salas.
- Diseño visual del menú principal y de la pantalla de derrota.

### Cambiado

- Separación de los fotogramas de ataque respecto de las animaciones de movimiento.
- Mejoras de claridad en la representación de los objetos.
- Presentación del precio sobre cada oferta de la tienda: valor centrado, en color dorado y con restauración posterior del tamaño y color de la fuente.
- Restablecimiento del daño inicial del jugador de 999, usado para pruebas, a 7.
- Ajuste de la vida del jefe final de 300 a 250.

### Corregido

- Errores relacionados con la sala final.
- Integración de los recursos visuales y la lógica del menú de victoria.

## 2026-11-12

### Añadido

- Menú principal al iniciar la aplicación.
- Acciones para iniciar una partida y acceder a las opciones del juego.
- Posibilidad de volver al menú principal desde pausa.
- Menú de derrota con reinicio de partida, regreso al menú principal y salida del juego.
- Selección de opciones con el mouse en los menús: detección del botón señalado, actualización de la selección y activación mediante clic.
- Desplazamiento del indicador de selección junto con la opción señalada por el mouse, manteniendo la navegación con teclado.
- Animaciones del enemigo terrestre y del enemigo volador.
- Recursos y animaciones de plataformas.
- Imágenes para los objetos.
- Diseño de los ascensores que conectan salas superiores e inferiores.

### Cambiado

- Integración progresiva de las imágenes de objetos con su representación en las salas.
- Retroceso temporal de parte del código a un estado anterior para reducir errores durante la incorporación de recursos, conservando la mayoría de los assets.

### Corregido

- Fallos en las animaciones del enemigo terrestre.
- Problemas de representación de las imágenes de los objetos.
- Conflictos de integración entre las ramas que incorporaban menús, animaciones y recursos visuales.
- Integración del menú principal con los cambios simultáneos de `Main_game.java`.

## 2026-11-11

### Cambiado

- Configuración independiente de la hitbox del ataque del jugador hacia arriba, siguiendo el criterio de separar los ataques que necesitan dimensiones propias.
- Ajustes de la hitbox corporal, velocidad y vida de los jefes.

## 2026-11-10

### Añadido

- Ataque del jefe final hacia arriba cuando detecta al jugador sobre su cuerpo.
- Selección de la dirección del ataque del jefe al iniciar su secuencia.
- Ampliación del catálogo de objetos y de los tipos de estadísticas modificables.
- Aplicación de modificadores de fuerza de pogo, velocidad de dash, ancho y alto del ataque normal y reducción de su cooldown.
- Valores iniciales de esas estadísticas en `Jugador` y restauración al reiniciar la partida.
- Límite mínimo del cooldown del ataque normal para impedir que las mejoras lo reduzcan por debajo del umbral configurado.
- Poción permanente de tres usos, con curación instantánea de 30 puntos por uso.
- Recarga completa de la poción al comenzar una nueva etapa.
- Indicador de los usos restantes de la poción en el HUD.
- Menú de pausa con opciones para continuar, consultar los controles y reiniciar la partida.
- Oscurecimiento de la escena de fondo al mostrar la pausa.

### Cambiado

- Uso de los valores del jugador para calcular la fuerza de rebote del pogo y la velocidad del dash, en lugar de mantener valores independientes en `Main_game`.
- Posibilidad de ampliar las dimensiones de una `Hitbox` y modificar el cooldown de un `Ataque` desde los efectos de los objetos.
- Curación limitada a la vida máxima, sin consumir una carga cuando el jugador ya tiene la vida completa.
- En la copia beta revisada, el uso de la poción está asignado a la tecla A; la propuesta inicial del desarrollo utilizaba Q.

## 2026-11-07

### Cambiado

- Adaptación del jefe a la lógica de persecución y ataque de los enemigos terrestres.
- Separación de la configuración del ataque cuerpo a cuerpo del jefe respecto de la del enemigo común.
- Uso de dimensiones propias para el cuerpo y el ataque del jefe.
- Coordinación de la persecución y el ataque cuerpo a cuerpo con su comportamiento de disparo.

### Corregido

- Comentarios con caracteres dañados en el código.

## 2026-11-06

### Añadido

- Avance a la siguiente etapa después de derrotar al jefe de la etapa actual.
- Mensaje «Presionar ENTER para avanzar de etapa», situado abajo y centrado.
- Restricción del mensaje y de la transición a la sala actual de tipo `JEFE`, una vez derrotado su jefe.
- Incremento del número de etapa y generación del nuevo mapa al confirmar el avance.
- Etapa final provisional.

### Cambiado

- Conservación del progreso del jugador al avanzar de etapa, diferenciando esa transición del reinicio completo de la partida.
- Recolocación del jugador y restablecimiento de los estados de movimiento necesarios al entrar en una nueva etapa.

## 2026-11-02

### Añadido

- Configuración aleatoria de las salas de combate al generar la etapa.
- Selección entre tres moldes de sala de enemigos.
- Molde con enemigos voladores distribuidos a diferentes alturas.
- Molde con dos plataformas y enemigos terrestres sobre ellas.
- Molde con plataformas a distintas alturas, enemigos terrestres y un enemigo volador.
- Gravedad para los enemigos terrestres.
- Aterrizaje de enemigos sobre el piso y la superficie superior de las plataformas.
- Comprobación de altura entre jugador y enemigo antes de iniciar un ataque cuerpo a cuerpo.
- Secuencia de ataque con preparación, ejecución de un golpe y recuperación.

### Cambiado

- Un enemigo que ya comenzó a atacar completa su secuencia aunque el jugador abandone el rango inicial.
- Regreso a la persecución al finalizar la recuperación del ataque.
- Espera del cooldown cuando el enemigo está cerca y todavía no puede iniciar otro ataque.
- Ajustes de dimensiones y márgenes de las hitboxes.
- Resolución del aterrizaje a partir de los pies de la hitbox, contemplando sus desplazamientos respecto de la posición.

### Corregido

- Aparición del jugador por la puerta equivocada al cambiar horizontalmente de sala: salir por la derecha lleva al borde izquierdo de la sala siguiente y salir por la izquierda lleva al borde derecho.
- Caída de los enemigos terrestres y apoyo en plataformas.

## 2026-11-01

### Añadido

- Clase `Hitbox` para definir dimensiones y desplazamientos del cuerpo de los enemigos respecto de su `Posicion`.
- Clase `EnemigoVolador` y aparición de prueba para comprobar su comportamiento.
- Clase `AtaqueEnemigo` para trasladar parte del cálculo del ataque enemigo fuera de `Main_game`.

### Cambiado

- Incorporación de la hitbox corporal a la construcción de los enemigos.
- Adaptación de la lógica de colisiones para consultar las hitboxes definidas por las entidades.
- Ajuste del choque del jugador con los límites laterales de la sala.

### Corregido

- Incompatibilidades entre los constructores y sus llamadas después de introducir `Hitbox`.
- Error de compilación que había quedado registrado en el primer commit de esta incorporación.
- Desajuste del límite lateral que permitía que parte del personaje quedara fuera del área prevista.

## 2026-10-30

### Añadido

- Disparo continuo del jefe mediante proyectiles.
- Hurtbox elíptica del jugador para comprobar daño por contacto, ataques y proyectiles.
- Definiciones y nombres de las variantes de etapa, preparadas para recibir su contenido específico.

### Cambiado

- Separación entre el cuerpo utilizado para el movimiento y la zona del jugador utilizada para recibir daño.

## 2026-10-27

### Añadido

- Jefe dentro de su sala correspondiente.
- Clase `Proyectil` como base para los disparos del juego.

## 2026-10-25

### Añadido

- Ataque del jugador hacia arriba al mantener ARRIBA y pulsar la tecla de ataque.
- Tiempo de preparación del ataque enemigo antes de ejecutar el golpe.
- Visualización de la hitbox del ataque enemigo para depuración.

## 2026-10-24

### Añadido

- Alcance de ataque enemigo separado del daño por contacto, permitiendo golpear al jugador desde cierta distancia.

## 2026-10-21

### Añadido

- Estados de comportamiento de los enemigos: `QUIETO`, `PERSEGUIR` y `ATACAR`.
- Detección del jugador dentro de un rango.
- Persecución hasta alcanzar la distancia necesaria para detenerse y atacar.

## 2026-10-13

### Añadido

- Ofertas de objetos en la tienda.
- Compra de objetos por 20 monedas.

### Cambiado

- Anclaje de los objetos a sus plataformas o pedestales.

## 2026-10-10

### Añadido

- Plataformas en las salas.
- Colisiones del jugador con las plataformas.

## 2026-10-06

### Añadido

- Recogida de objetos en la sala de ítems.
- Objeto de prueba que aumenta la vida máxima en 20 puntos.
- Enumeración para identificar la estadística modificada por cada objeto.
- Selección aleatoria de un objeto de un catálogo inicial de tres opciones.
- Mejoras iniciales de vida máxima, velocidad y daño.

## 2026-10-05

### Añadido

- Clase `Ataque` para representar los ataques asociados a las entidades.
- Cooldowns para los ataques del jugador.

## 2026-10-04

### Añadido

- Pogo: golpe hacia abajo en el aire que impulsa al jugador hacia arriba al conectar.
- Restablecimiento del dash aéreo y del doble salto al acertar un pogo.
- Retroceso ligero del jugador al conectar un ataque horizontal.

### Cambiado

- Reorganización de controles: flechas para movimiento lateral, Z/ARRIBA para salto y doble salto, S para caída rápida, X para atacar y C para dash.
- En esta implementación inicial, el pogo se activaba manteniendo S y atacando con X. Los controles siguieron evolucionando durante el desarrollo.

## 2026-10-03

### Añadido

- Continuación de la integración del ataque con alcance y de las representaciones de depuración.

### Corregido

- Desfase entre la hitbox corporal y la imagen del jugador.
- Centrado de la hitbox respecto del sprite.
- Conflictos y errores de integración de cambios locales.

## 2026-10-02

### Añadido

- Ataque del jugador con un área de alcance.
- Dibujo de las hitboxes del jugador y de su ataque para depuración.

### Incidencias registradas

- La hitbox corporal todavía no coincidía con el sprite. Su corrección quedó registrada al día siguiente.

## 2026-09-28

### Añadido

- Pantalla inicial de Game Over al morir el personaje.
- Reinicio de la partida mediante ENTER desde esa pantalla.
- Nuevos recursos gráficos.

### Cambiado

- Ajustes de tamaño de elementos del HUD, incluida la vida y la moneda.

## 2026-09-26

### Añadido

- Enemigo de prueba que recibe daño y puede morir.
- Ataque del jugador contra enemigos.
- Daño por contacto de los enemigos al jugador.
- Entrega de monedas al derrotar enemigos.
- Indicador de monedas actuales en el HUD.
- Bloqueo de la salida de la sala mientras quedan enemigos por derrotar.
- Icono propio del juego en reemplazo del predeterminado.

### Cambiado

- Limitación a un dash por permanencia en el aire.
- Suspensión de la caída mientras el dash está activo.

## 2026-09-25

### Añadido

- Recorrido del mapa mediante las puertas de los cuatro lados de la sala.
- Interacción con F para las conexiones verticales.
- Información de la sala y la posición del jugador en la consola.
- Selección entre dos variantes de etapa para probar la distribución aleatoria.
- HUD con vida, tipo de sala, posición en la matriz, nombre de la etapa y minimapa.
- Doble salto.
- Salto de altura variable según cuánto se mantiene pulsada la tecla.
- Dash horizontal.

### Cambiado

- Ajustes de velocidad y salto del jugador.

### Corregido

- Problemas de resolución y pantalla completa que afectaban el HUD y la representación del personaje.
- Desalineación del personaje respecto del piso.

## 2026-09-23

### Añadido

- Animación de caminata del protagonista.

### Cambiado

- Movimiento más fluido del personaje.

## 2026-09-21

### Añadido

- Reglas para generar una sala de jefe, una de objetos y una tienda por etapa.
- Restricciones para que las conexiones y la ubicación de las salas especiales tengan sentido dentro del mapa.

## 2026-09-20

### Añadido

- Sprite del jugador en reposo.
- Movimiento horizontal, salto, gravedad y colisión con el piso.
- Caída rápida.
- Conexión entre los objetos de la lógica del juego y la representación en LibGDX.
- Integración de `Jugador`, `Juego` y `Main_game`.
- Generación básica aleatoria del mapa de una etapa y representación de su estructura mediante una matriz.

### Cambiado

- Uso de valores de tipo `float` para la velocidad.

## 2026-09-19

### Añadido

- Entidades principales del juego.
- Estructura inicial de etapas.
- Primeras clases y relaciones de la lógica de juego.

## 2026-08-25

### Cambiado

- Corrección del formato y finalización del README de la primera entrega.

## [0.1.0] — 2026-08-24 — Pre-entrega N.º 1

Esta denominación se conserva del changelog original del proyecto.

### Añadido

- Estructura inicial de LibGDX para escritorio con LWJGL3.
- Configuración de Gradle y archivos necesarios para construir y ejecutar el proyecto.
- Control de versiones con Git y repositorio en GitHub.
- Archivo `.gitignore` para excluir temporales y resultados de compilación.
- Primer README con información del proyecto, equipo de la E.T. N.º 35, tecnologías e instrucciones de ejecución.
- Archivo inicial de changelog.

### Documentado

- Propuesta del videojuego en la Wiki: combate con bufanda, pogo, parry, torre organizada por géneros musicales y árbol de habilidades. Esta entrada conserva el antecedente del changelog original y describe la propuesta; no acredita la implementación de todas esas mecánicas.
- El README fue creado y eliminado durante los ajustes iniciales; su revisión posterior quedó registrada el 25 de agosto.

## Alcance y observaciones

- Las siete asociaciones de música corresponden a seis variantes musicales y al piso final. No representan siete pisos consecutivos: la generación contempla variantes de etapa y un piso final.
- La existencia de un tipo de modificador no demuestra que todos los objetos lo apliquen correctamente. La beta contiene efectos para `FUERZA_POGO`, `VELOCIDAD_DASH`, `ANCHO_ATAQUE`, `ALTO_ATAQUE` y `COOLDOWN_ATAQUE`; también conserva referencias a `FUERZA_SALTO` en parte del catálogo de `GeneracionEtapa`, sin un caso equivalente de aplicación en `Jugador`.
- Las mejoras de arquitectura trasladaron responsabilidades a `Hitbox`, `Ataque` y `AtaqueEnemigo`; esto no implica que se haya eliminado todo cálculo de combate de `Main_game`.
- No se registra un sistema de guardado o carga de partidas como funcionalidad implementada. Una opción visual o una propuesta no equivale a una función terminada.
- Las copias sin comentarios son entregas del código fuente y los recursos del proyecto; no se presentan como instaladores ni como nuevos lanzamientos numerados.
- El estado «beta» y las comprobaciones de compilación documentadas no garantizan ausencia de fallos durante la ejecución.

## Índice completo de commits revisados

Los títulos se transcriben del historial para facilitar la trazabilidad; pueden conservar errores de escritura. Una integración combina ramas y no se contabiliza como otra incorporación de las mismas funcionalidades.

| Fecha (Argentina) | Commit | Tipo | Título registrado |
| --- | --- | --- | --- |
| 2026-11-13 | [`de4928a`](https://github.com/agusdal/musical-tower/commit/de4928a69f51166499e097e8f1caaf8770898551) | Cambio | Versión BETA del juego. Con algunos detalles corregidos. |
| 2026-11-13 | [`5c1619e`](https://github.com/agusdal/musical-tower/commit/5c1619efb21c3d28cb74c54406e3f09490a35b43) | Integración | Merge remote-tracking branch 'origin/main' |
| 2026-11-13 | [`e4ce484`](https://github.com/agusdal/musical-tower/commit/e4ce48425a049233f35916d0a62907b24d5c9f6e) | Cambio | se ingreso los diseños de los jefes y del menu de victoria |
| 2026-11-13 | [`fa9ff7c`](https://github.com/agusdal/musical-tower/commit/fa9ff7ca8c2815791a97c18f96eaeb8e54e742de) | Cambio | Se implementó un menú de victoria al derrotar al jefe final. |
| 2026-11-13 | [`70c3c79`](https://github.com/agusdal/musical-tower/commit/70c3c791825efa449c5c22c19d1742f353888cd1) | Cambio | Se implementó la opcions de cambiar el volumen de la musica desde el menú y desde las opciones al pausar el juego. Se colocaron 7 archivos MP3 que contienen la musica de cada etapa correspondiente. |
| 2026-11-13 | [`593a208`](https://github.com/agusdal/musical-tower/commit/593a208493a2c378f26435af73905eee8339fff9) | Cambio | solucion de errores en la sala final |
| 2026-11-13 | [`0d9cfbb`](https://github.com/agusdal/musical-tower/commit/0d9cfbbcb2d23aa1c01902757b744826026e3ced) | Cambio | se mejoró el menu inicial y se implemento la visual del menu de derrota |
| 2026-11-13 | [`01237ba`](https://github.com/agusdal/musical-tower/commit/01237badabdd53de1e63a3cb13238b8d3e4503b8) | Cambio | se añadio el diseño del menu principal |
| 2026-11-13 | [`6f7a13b`](https://github.com/agusdal/musical-tower/commit/6f7a13b303be952bb98332c50d32bc8cb882ec64) | Cambio | se añadieron fondos los cuales cambian de manera aleatoria |
| 2026-11-13 | [`d606333`](https://github.com/agusdal/musical-tower/commit/d60633377f154ea32d7cf8a44be736b27e89f528) | Integración | Merge remote-tracking branch 'origin/main' |
| 2026-11-13 | [`3c1f197`](https://github.com/agusdal/musical-tower/commit/3c1f19720c7a2f359b103015170709aabc43dd3e) | Cambio | ahora se muestran lo items con claridad |
| 2026-11-13 | [`9c08342`](https://github.com/agusdal/musical-tower/commit/9c08342d638af3dbdbd4ec38d82ae569d110b61b) | Cambio | separe los ataques de los frames de movilidad |
| 2026-11-12 | [`daef06e`](https://github.com/agusdal/musical-tower/commit/daef06e9411e1ac56b80e2ae04242dc98afcd4cb) | Cambio | se ingreso correctamente el diseño de los "ascensores" que permiten el pasaje de una sala inferior a una superior y viceversa |
| 2026-11-12 | [`d15997b`](https://github.com/agusdal/musical-tower/commit/d15997bcbfccb4e3276f9f86da7de454aa818705) | Cambio | Se agregó que en todos los menús se pueda seleccionar cosas de forma mas dinamica con el mouse. |
| 2026-11-12 | [`ad58b8b`](https://github.com/agusdal/musical-tower/commit/ad58b8bf1c191852aa5c3962e9599d5692f9421c) | Cambio | Se implementó un menú de muerte mas variado (mas opciones). |
| 2026-11-12 | [`6a0a1bf`](https://github.com/agusdal/musical-tower/commit/6a0a1bfc2ec877bf8660ada890e82ba6851bee03) | Integración | Resuelve conflictos e integra el menu principal |
| 2026-11-12 | [`51f5ef4`](https://github.com/agusdal/musical-tower/commit/51f5ef4e951c8fcb8162ae6b7a75c69015e9663b) | Cambio | Se implementó un menú al principio de la ejecución del juego. |
| 2026-11-12 | [`551d79f`](https://github.com/agusdal/musical-tower/commit/551d79f588ac1c3b8d61555e7fb4f4398e9d04cc) | Integración | Merge remote-tracking branch 'origin/main' |
| 2026-11-12 | [`210ccb7`](https://github.com/agusdal/musical-tower/commit/210ccb71d0c2d51ecedad32308ba3e3b069ba34b) | Cambio | se corrigieron errores en las animaciones del enemigo y se agregaron animaciones para el enemigo volador |
| 2026-11-12 | [`178135c`](https://github.com/agusdal/musical-tower/commit/178135c56dd35d71befe582c1767c8b7d6435567) | Integración | Merge remote-tracking branch 'origin/main' |
| 2026-11-12 | [`ea0467f`](https://github.com/agusdal/musical-tower/commit/ea0467fc15cdd1f697092b307d2f76f1ad584193) | Cambio | con la intencion de prevenir errores el codigo fue enviado a una etapa anterior pero se añadieron lagran mayoria de assets |
| 2026-11-12 | [`dbc5105`](https://github.com/agusdal/musical-tower/commit/dbc5105df9b32f676f7cdd18224acd4d5ec1204e) | Cambio | hay ciertos errores sencillos de solucionar, pero las imagenes de los items ya se solucionaron |
| 2026-11-12 | [`cb1d266`](https://github.com/agusdal/musical-tower/commit/cb1d266f8b3c9e93ba28318baa73c91e5a50648a) | Cambio | hice avances con la integracion de imagenes de los items |
| 2026-11-12 | [`993ce6f`](https://github.com/agusdal/musical-tower/commit/993ce6fd1980195b2c57dc8825c95d7915816715) | Cambio | agreguè las imagenes de los items |
| 2026-11-12 | [`8baaf48`](https://github.com/agusdal/musical-tower/commit/8baaf4834f862d223b5ef2b73ca483f00de58ed4) | Cambio | añadi tanto las animaciones del enemigo basico como la de las plataformas |
| 2026-11-12 | [`cb1ed7b`](https://github.com/agusdal/musical-tower/commit/cb1ed7b379ae9f0e483772c4ea1c44b0e0d3820e) | Integración | Merge remote-tracking branch 'origin/main' |
| 2026-11-12 | [`34028df`](https://github.com/agusdal/musical-tower/commit/34028df8ae33fb0a35d53214435aa27e14c31196) | Cambio | añadi tanto las animaciones del enemigo basico como la de las plataformas |
| 2026-11-11 | [`4b16b8e`](https://github.com/agusdal/musical-tower/commit/4b16b8ea58e54f73e2dc6f9653ce6aa2cef9dc6e) | Cambio | Se modificó la hitbox del ataque hacia arriba del jugador, y se ajusto la hitbox; velocidad y vida de los jefes. |
| 2026-11-10 | [`958e920`](https://github.com/agusdal/musical-tower/commit/958e920de5e24a44dfdd53280eed26e21271e76f) | Cambio | Se implementó un objeto permanente al jugador, el mismo es una pocima de 3 usos que se renueva cada vez que se cambia de etapa. También se colocó un menú de pausa para que el jugador pueda ver sus controles y reiniciar la partida. |
| 2026-11-10 | [`b32b112`](https://github.com/agusdal/musical-tower/commit/b32b11234025a34494c3dd72578777460e1a5414) | Cambio | Se agregaron mas items a la pool. |
| 2026-11-10 | [`c251be1`](https://github.com/agusdal/musical-tower/commit/c251be1ae07b1854805d66c5d7f4cda266c5aa14) | Cambio | Se agregó el ataque hacia arriba para el jefe final. |
| 2026-11-07 | [`bd06203`](https://github.com/agusdal/musical-tower/commit/bd06203b7fa3274bc3acd99672887f7523cc59bd) | Cambio | Se modificó el comportamiento de el jefe, ahora persigue al jugador y tiene su propio ataque aparte de del de el enemigo común. También se corrigieron los comentarios dañados del codigo. |
| 2026-11-06 | [`af74a17`](https://github.com/agusdal/musical-tower/commit/af74a178ca58c8684d02f2d59cbc32ea9a0b6041) | Cambio | Se agregó la funcionalidad de avanzar de etapa y una etapa final provisoria |
| 2026-11-02 | [`84338c5`](https://github.com/agusdal/musical-tower/commit/84338c58c6da0d48cd4db87c45175816612b8897) | Cambio | Se refinó el comportamiento de los enemigos y algunas hitboxes. Se solucionó el error que causaba que el jugador no aparezca en la puerta que debe tras cambiar de sala. |
| 2026-11-02 | [`93a9669`](https://github.com/agusdal/musical-tower/commit/93a96694324932a8c99285f45bf2e23963408f59) | Cambio | CAMBIO IMPORTANTE: Se agregó el sistema de generación aleatoria de enemigos y plataformas por sala. Esto se efectua a la hora de definir el mapa en generacion etapa. |
| 2026-11-01 | [`e8967c7`](https://github.com/agusdal/musical-tower/commit/e8967c7c311886e9f870e06a8de3492c41f3e1e4) | Cambio | Se implementó la clase ataque enemigo para separar mejor la función de calcular hacia donde va el ataque del enemigo del main game. También se corrigió que se pueda compilar el codigo. Y se ájustó la forma que tenía el jugador para chocar con una pared. |
| 2026-11-01 | [`f657058`](https://github.com/agusdal/musical-tower/commit/f657058b600e4087f8c894cd7107ab1537af76bf) | Cambio | CAMBIO IMPORTANTE: Se agregó la clase enemigo volador y la clase HITBOX para los enemigos. Ademas se colocó uno de estos enemigos nuevos en la sala inicial para probar su funcionamiento. Error: el codigo no compila, se corregirá en una actualización futura. |
| 2026-10-30 | [`054e966`](https://github.com/agusdal/musical-tower/commit/054e966bac20652cbf2b0307661407f9ad01f74c) | Cambio | Se agregó la HURTBOX del jugador, la misma representa su colisión respecto a los enemigos, ataques y proyectiles (tiene forma de elipse). También se implementaron las nuevas etapas con sus respectivos nombres preparadas para recibir mas adelante todos sus respectivos datos. |
| 2026-10-30 | [`176cd8f`](https://github.com/agusdal/musical-tower/commit/176cd8f48a103d62bd983d99bcec7cc8062d8f98) | Cambio | Se agregó la funcion de disparar para el jefe, el cual ahora dispara continuamente. |
| 2026-10-27 | [`aa1c5c8`](https://github.com/agusdal/musical-tower/commit/aa1c5c8c6d3e13094ee21766a2d8481c320fb0d6) | Cambio | NUEVA ADICIÓN: Se agregó al jefe dentro de su habitación respectiva. Y ademas se implementó la clase proyectil. |
| 2026-10-25 | [`13285b4`](https://github.com/agusdal/musical-tower/commit/13285b483d1d8e0c503852db4e8a5acc19af25a0) | Cambio | Se agregó la mecanica del jugador que le permite golpear hacia arriba, manteniendo UP y apretando X. |
| 2026-10-25 | [`435e62e`](https://github.com/agusdal/musical-tower/commit/435e62e4b0f390c3bee3ee81c5cc8d33cdadd94a) | Cambio | Se emprolijo el tema de el ataque del enemigo para que tenga un start-up (no ataque instantaneamente) y ahora se dibuja la hitbox del mismo. |
| 2026-10-24 | [`4a1f154`](https://github.com/agusdal/musical-tower/commit/4a1f1547a42a0701fb18357cf2a2b2d9d6eef6e3) | Cambio | Se agregó que los enemigos puedan golpear al jugador desde lejos. |
| 2026-10-21 | [`c6288db`](https://github.com/agusdal/musical-tower/commit/c6288dbcd429576c45988c6a2d192c210e0074f6) | Cambio | Se agregó la logica que permite que el enemigo se mueva junto con 3 estados: quieto, perseguir y atacar. Los mismos permiten que detecte al jugador y se mueva hacia el mismo hasta cierta distancia, donde parará y atacará al jugador. |
| 2026-10-13 | [`7050e7b`](https://github.com/agusdal/musical-tower/commit/7050e7b6cd1eb6d4bfbce93e28ff0d24fddf2318) | Cambio | Se agregaron objetos dentro de la tienda que pueden ser comprados por 20 monedas. Ademas se anclaron los objetos a sus respectivas plataformas o pedestales |
| 2026-10-10 | [`bd2229b`](https://github.com/agusdal/musical-tower/commit/bd2229bf1b73f791fa95ae6c546641a56150d19f) | Cambio | Se agregaron las plataformas, con las que se pueden colisionar. |
| 2026-10-06 | [`46730c6`](https://github.com/agusdal/musical-tower/commit/46730c671ddae12fb44bedb49f0385e867d18f03) | Cambio | Se implementó la mecanica de que de manera aleatoria se seleccionará para la sala de item un objeto aleatorio de 3 que están en un arraylist de la clase juego. (uno aumenta la vidaMax otro la velocidad y un ultimo el daño). |
| 2026-10-06 | [`f0f32de`](https://github.com/agusdal/musical-tower/commit/f0f32dee210c61b760d61acdc5fdb3da7245c6e1) | Cambio | Se implementó en la sala de objetos un objeto de prueba que aumenta la vida maxima en 20 puntos, ahora se pueden recoger objetos. También se agregó un enum que permite identificar que stat modifica cada item. |
| 2026-10-05 | [`63937e6`](https://github.com/agusdal/musical-tower/commit/63937e657c109b6260adeea5d033b0ded5c70493) | Cambio | Se agregó la clase ATAQUE que guardará cada ataque de cada entidad en el juego dentor de si misma. Otra adición son los COOLDOWN para los ataques del jugador |
| 2026-10-04 | [`ff1f1d5`](https://github.com/agusdal/musical-tower/commit/ff1f1d5682f17b3edc183a8af42bee004b56fded) | Cambio | Se agregó la mecanica de hacer pogo (funciona manteniendo la S, en el aire y golpeando con X) y tambien un ligero retroceso al golpear horizontalmente, cada vez que suceda un pogo, el dash y el doble salto se reiniciaran. |
| 2026-10-04 | [`03b207e`](https://github.com/agusdal/musical-tower/commit/03b207e09c9b2c1e8e9a1912ac0797dea9ab189f) | Cambio | Se cambiaron los controles a: LEFT y RIGHT como movimiento lateral, Z/UP para salto y doble salto, S caida rapida, X atacar y C dash. |
| 2026-10-03 | [`0e8778d`](https://github.com/agusdal/musical-tower/commit/0e8778da5a1cf16bbba898090061fcc21c97d939) | Cambio | ERROR CORREGIDO: Se solucionó el tema de la hitbox fuera de lugar, se centro con respecto a la imagen del jugador y ahora esta hitbox tiene sentído. |
| 2026-10-03 | [`3c56147`](https://github.com/agusdal/musical-tower/commit/3c5614794b115e8c8c741a9549940047b4c7af67) | Cambio | Se agregó la función de golpear con rango, se puede observar la hitbox del ataque y la del jugador. La del mismo está desfasada y la imagen que lo acompaña no es representativa de la misma. Esto se corregirá en un parche futuro. |
| 2026-10-03 | [`6c478a3`](https://github.com/agusdal/musical-tower/commit/6c478a3adbc1a857ea1746bc7e727088bd42945b) | Integración | Resolver cambios con errores localmente. |
| 2026-10-02 | [`6f341df`](https://github.com/agusdal/musical-tower/commit/6f341dff8e43b99eee47109dede221e149fed783) | Cambio | Se agregó el ataque a distancia (rango para atacar a enemigos) y se puede ver la hitbox del jugador y de su ataque (se corregirá ya que no coincide con la imagen) |
| 2026-09-28 | [`1b6b257`](https://github.com/agusdal/musical-tower/commit/1b6b2573c9fbac282410ab0e684e97e19b6fcd4a) | Cambio | Se guardaron NUEVOS ASSETS y se modifico el tamaño de algunas cosas del HUD (vida y la moneda) |
| 2026-09-28 | [`8939b39`](https://github.com/agusdal/musical-tower/commit/8939b39296a6cdf77a29f25957e4c410fa71766d) | Cambio | Se agregò la funcion de que cuando el personaje muere salte una pantalla de Game Over y empiece el juego de nuevo tocando la tecla ENTER |
| 2026-09-26 | [`c4a79b9`](https://github.com/agusdal/musical-tower/commit/c4a79b976640e6ea795a9e856d314acc178b9747) | Cambio | Se creó la interfaz de monedas actuales, ahora el jugador recibe daño de contacto por el enemigo (se refleja en la barra de vida y en la consola) al matarlo otorga monedas al jugador. Por último si intentas salir sin haber matado al enemigo, no te deja pero si lo matás si te deja. |
| 2026-09-26 | [`425fc8a`](https://github.com/agusdal/musical-tower/commit/425fc8a7d0d49571a781725ca3c38a3f65fa6bea) | Cambio | Se implementó la mecanica de golpear enemigos junto a un enemigo que aparece en la sala inicial para comprobar que el enemigo reciba daño y pueda morir (todavía no daña al jugador) |
| 2026-09-26 | [`ca681d6`](https://github.com/agusdal/musical-tower/commit/ca681d6c4a72e62b87e8219da61d3a1b6d43e325) | Cambio | Se agregó que solo se pueda hacer un dash en el aire, que el dash no decaiga mientras está activo y se cambió el logo predeterminado por el del juego |
| 2026-09-25 | [`aa68ccd`](https://github.com/agusdal/musical-tower/commit/aa68ccdb752d5b8fa0e3ca71321bca2f7e4edf54) | Cambio | SE AGREGARON 3 FUNCIONES NUEVAS: Doble salto, salto controlable (modificar la altura del mismo) y un dash horizontal |
| 2026-09-25 | [`3e22031`](https://github.com/agusdal/musical-tower/commit/3e22031499264ec0a41ae2da2c50c2a74de06c40) | Cambio | Se agregó un HUD principal que muestra: vida, tipo de sala actual, posicion (respecto a la matriz), el nombre de la etapa actual y el mapa. Ademas se solucionaron errores respecto a la resolución (cuando se hacia pantalla completa se rompía el HUD, el modelo del personaje, etc. o el personaje flotaba sin tocar el piso) y también se modificaron algunos valores del jugador como su velocidad y salto |
| 2026-09-25 | [`8995dbc`](https://github.com/agusdal/musical-tower/commit/8995dbcbdb2dfac1347fbd89aea99a6931c0bffe) | Cambio | Se añadió que se pueda recorrer el mapa pudiendo ir hacia la puerta de la derecha, izquierda, la que lleva a arriba y la que lleva a abajo (se presiona F para interactuar con las mismas). Con un indicativo en la consola que te dice constantemente donde estas y en que tipo de habitacion estas (Se crearon dos etapas para probar la funcion de eleccion 50/50). |
| 2026-09-23 | [`8e166a9`](https://github.com/agusdal/musical-tower/commit/8e166a97a6b63afdcfa5ef753578957980ac0713) | Cambio | movimiento fluido del protagonista |
| 2026-09-23 | [`f774724`](https://github.com/agusdal/musical-tower/commit/f7747240d9673c8e38481f518c37c4e30d980839) | Cambio | animacion de caminata agregada |
| 2026-09-21 | [`cb1ff65`](https://github.com/agusdal/musical-tower/commit/cb1ff65196ca467203af59dd7e5f351e251af8e2) | Cambio | Configuré la generación de el mapa para que haya una sala de jefe, de objeto y una tienda una de cada una por jefe. Y que a su vez las mismas tengan sentido sin que toquen varias salas |
| 2026-09-20 | [`7beaed3`](https://github.com/agusdal/musical-tower/commit/7beaed3a6eb62417005bacdb897093bcf6a2d98e) | Cambio | Cree la logica basica para la generación del mapa de cada etapa, mostrando en cada ejecución la forma de el mapa representado en una matriz |
| 2026-09-20 | [`333b77b`](https://github.com/agusdal/musical-tower/commit/333b77b8ac6daf56cc8d89450e09e72c30089de3) | Cambio | Cree la mecanica de caida rapida y conecté jugador, juego y main_game |
| 2026-09-20 | [`91108b3`](https://github.com/agusdal/musical-tower/commit/91108b37fd1989745bd9f448d7a41c75fde3fc32) | Cambio | Cree el movimiento del personaje, el salto, una colision con el piso y la gravedad, también conecté la logica de las clases (jugador en este caso) a el libgdx, es decir que practicamente lo que se mueve en pantalla es el objeto Jugador. Cambié la velocidad en general a un tipo FLOAT |
| 2026-09-20 | [`aee3da2`](https://github.com/agusdal/musical-tower/commit/aee3da2730370abe1fa669cd63ad616f3bd66aa3) | Cambio | Añadí el asset del jugador parado |
| 2026-09-19 | [`4ad23ef`](https://github.com/agusdal/musical-tower/commit/4ad23ef75307783695670f78c5ca5a56d352bfee) | Cambio | Agrega entidades, etapas y logica inicial del juego |
| 2026-08-25 | [`b890c26`](https://github.com/agusdal/musical-tower/commit/b890c26465c57f96d3ba54928477130ac4b56235) | Cambio | readme.me listo |
| 2026-08-25 | [`af81c53`](https://github.com/agusdal/musical-tower/commit/af81c53841bfac0b39cf44852a60ead7fd737b2a) | Cambio | Correct formatting in readme.md |
| 2026-08-24 | [`966398d`](https://github.com/agusdal/musical-tower/commit/966398d18ae5df35f06e1309354e88a7292980e3) | Cambio | Add initial changelog for project documentation |
| 2026-08-24 | [`c5caa67`](https://github.com/agusdal/musical-tower/commit/c5caa67ebcfc813f7227c13b6f1acffd909f675a) | Cambio | Delete README.md |
| 2026-08-24 | [`86c8b59`](https://github.com/agusdal/musical-tower/commit/86c8b594bc0b05858f4f59636ed196f018807aea) | Cambio | Initialize README with project information |
| 2026-08-24 | [`84dcc8f`](https://github.com/agusdal/musical-tower/commit/84dcc8f3e9066f48d3feea657446bd7ed51cf919) | Cambio | Feat: Configuracion inicial del proyecto con LibGDX |

Fuente del historial: [agusdal/musical-tower](https://github.com/agusdal/musical-tower), rama `main`, hasta `de4928a69f51166499e097e8f1caaf8770898551`. Las entregas locales se contrastaron con los archivos preparados en la carpeta del proyecto.

