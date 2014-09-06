TP-Tweet-Wars
=============

TP POD Tweet Wars

La idea es publicar tweets y detectar los tweets falsos.
Va a existir un servidor (no que sirva de fuente de tweets válidos, y los clientes deben obtener los tweets de ese servidor. Luego de obtener los tweets hay que difundirlos entre los otros jugadores.

Se ganan puntos por avisar al servidor cuando se reciben tweets, cuando se detectan tweets falsos, y cuando se reporta un usuario que ha publicado más de X tweets falsos. Gana el jugador que sobreviva hasta el final y tenga la mayor cantidad de puntos.
Se gana:
- 1 punto por tweet recibido para el que acepta un tweet
- 1 punto cuando un tweet verdadero del jugador es aceptado, y 10 puntos cuando el tweet es falso
- 100 puntos por eliminar otro jugador

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


