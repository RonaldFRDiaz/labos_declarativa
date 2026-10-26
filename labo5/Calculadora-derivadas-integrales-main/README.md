# Calculadora básica

Aplicación de escritorio en Java Swing para sumar, restar, multiplicar y dividir.
Acepta números decimales y paréntesis, por ejemplo `2 + 3 * (4 - 1)`. Java
envía cada operación a las reglas de `calculo.pl`, ejecutadas por SWI-Prolog.
No requiere Maven ni librerías Java externas; sí requiere un JDK y SWI-Prolog
instalados y disponibles en el `PATH`.

## Ejecutar

Desde la carpeta `Calculadora`, compilar y ejecutar con PowerShell:

```powershell
$fuentes = Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $fuentes
Copy-Item src\main\resources\calculo.pl out\calculo.pl
java -cp out org.example.Main
```

Las cuatro operaciones y la evaluación recursiva están implementadas en
`src/main/resources/calculo.pl`; Java se encarga de la interfaz y de invocar
SWI-Prolog.