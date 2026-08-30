
personaje(eric).
personaje(timmy).
personaje(kelvin).
personaje(virginia).


protagonista(eric).
edad(eric, 30).


aliado(kelvin).
capturado(kelvin).
no_habla(kelvin).
puede_cargar(kelvin, troncos).
puede_construir(kelvin).


mutante(virginia).
puede_ser_aliada(virginia).


tiene(eric, hacha).
tiene(eric, encendedor).


zona(superficie).
zona(cuevas).
zona(bunkeres).


enemigo(canibales).
enemigo(mutantes).


aparece_en(canibales, superficie).
aparece_en(mutantes, superficie).
aparece_en(mutantes, cuevas).


sin_enemigos(bunkeres).
requiere(bunkeres, llave).


peligro(cuevas, alto).


peligro(superficie, dia, medio).
peligro(superficie, noche, alto).


necesita(eric, refugio).
necesita(eric, comida).
necesita(eric, agua).


encuentra_en(troncos, superficie).
encuentra_en(piedras, superficie).