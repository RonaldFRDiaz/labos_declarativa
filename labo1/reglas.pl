:- consult('hechos.pl').


puede_ayudar_construccion(Personaje) :-
    puede_cargar(Personaje, troncos),
    puede_construir(Personaje).


personaje_util(Personaje) :-
    puede_construir(Personaje);
    puede_ser_aliada(Personaje).



adulto(Personaje) :-
    edad(Personaje, Edad),
    Edad >= 18.