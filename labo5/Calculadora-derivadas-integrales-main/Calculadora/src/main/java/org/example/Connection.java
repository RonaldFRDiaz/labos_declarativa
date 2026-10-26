package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Envía las operaciones a las reglas de calculo.pl usando SWI-Prolog. */
public class Connection {

    public String calcular(String expresion) throws IOException, InterruptedException {
        String operacion = expresion.replace('×', '*').replace('÷', '/');
        if (operacion.length() > 512 || !operacion.matches("[0-9.()+*/\\s-]+")) {
            throw new IllegalArgumentException(
                    "Usá solo números, paréntesis y las operaciones +, -, * y /.");
        }

        Path archivoTemporal = null;
        try (InputStream reglas = getClass().getResourceAsStream("/calculo.pl")) {
            if (reglas == null) {
                throw new IOException("No se encontró calculo.pl en los recursos de la aplicación.");
            }
            archivoTemporal = Files.createTempFile("calculadora-", ".pl");
            Files.copy(reglas, archivoTemporal, StandardCopyOption.REPLACE_EXISTING);

            String consulta = "calcular((" + operacion + "),R),write(R)";
            Process proceso = new ProcessBuilder(
                    "swipl", "-q", "-s", archivoTemporal.toString(), "-g", consulta, "-t", "halt")
                    .redirectErrorStream(true)
                    .start();
            String salida = new String(proceso.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            int codigoSalida = proceso.waitFor();

            if (codigoSalida != 0 || salida.isEmpty()) {
                if (salida.contains("zero_divisor")) {
                    throw new IllegalArgumentException("No se puede dividir por cero.");
                }
                throw new IllegalArgumentException("La operación no es válida.");
            }
            return salida;
        } finally {
            if (archivoTemporal != null) {
                Files.deleteIfExists(archivoTemporal);
            }
        }
    }
}
