calcular(A + B, Resultado) :-
    calcular(A, ValorA),
    calcular(B, ValorB),
    Resultado is ValorA + ValorB.

calcular(A - B, Resultado) :-
    calcular(A, ValorA),
    calcular(B, ValorB),
    Resultado is ValorA - ValorB.

calcular(A * B, Resultado) :-
    calcular(A, ValorA),
    calcular(B, ValorB),
    Resultado is ValorA * ValorB.

calcular(A / B, Resultado) :-
    calcular(A, ValorA),
    calcular(B, ValorB),
    Resultado is ValorA / ValorB.

calcular(-A, Resultado) :-
    calcular(A, Valor),
    Resultado is -Valor.

calcular(Numero, Numero) :-
    number(Numero).