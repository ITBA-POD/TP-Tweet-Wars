TP-Tweet-Wars
=============

TP POD Tweet Wars

La idea es publicar tweets y detectar los tweets falsos y reportar los tweets falsos. 

Va a existir un servidor (no que sirva de fuente de tweets válidos, y los clientes deben obtener los tweets de ese servidor. Luego de obtener los tweets hay que difundirlos entre los otros jugadores.

Se ganan puntos por:
* Cuando se avisa que se recibe un tweet  
* la primera vez que alguien avisa que recibió un tweet la fuente recibe puntos
* cuando se reportan tweet falsos y se hecha a un jugador 
Gana el jugador que sobreviva hasta el final y tenga la mayor cantidad de puntos.

Se gana:
- X punto por tweet recibido para el que acepta un tweet
- Z punto cuando un tweet verdadero del jugador es aceptado, y Y puntos cuando el tweet es falso
- W puntos por eliminar otro jugador

Cada jugador tiene que instanciar un GamePlayer con su id y una descripcion, generar un hash privado y registrar ese objeto más el hash con el master.

El procedimiento sería
 - unirse al grupo
 - registrarse con el GameMaster (recibe su id de usuario)
 - conectarse a los otros jugadores
 - obtener tweets del servidor
 - publicar sus tweets (Verdaderos y Falsos) a los otros jugadores
 - recibir tweets de otros jugadores
	- verificar o nó los tweets recibidos y reportarlos al servidor
	- si encuentran un tweet falso pueden reportar al jugador origen


El GameMaster tiene los siguientes métodos:
* registrar jugador nuevo (in gamePlayer, int hash)
* obtener tweet
* obtener tweets (max 100)
* aceptar tweet 
* aceptar tweets (max 100)
* reportar trucho (tienen que ser como minimo 5 tweets del mismo source)
* obtener mis puntos

En los tests hay ejemplos para todas la operaciones necesarias.
