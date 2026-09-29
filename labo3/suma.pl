   suma(1, 1).
   suma(N, S) :- N > 1, M is N-1, suma(M, R), S is N + R.