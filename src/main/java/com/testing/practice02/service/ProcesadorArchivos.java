package com.testing.practice02.service;

import org.slf4j.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcesadorArchivos {

    private final Logger logger;

    public ProcesadorArchivos(Logger logger) {
        this.logger = logger;
    }

    public ResultadoProceso procesarArchivo(String ruta) {
        try {
            Path path = Paths.get(ruta);

            // Verificar si existe
            if (!Files.exists(path)) {
                logger.error("Archivo no encontrado: {}", ruta);
                return ResultadoProceso.ERROR_NO_EXISTE;
            }

            // Verificar permisos
            if (!Files.isReadable(path)) {
                logger.error("Sin permisos para leer: {}", ruta);
                return ResultadoProceso.ERROR_PERMISOS;
            }

            // Determinar tipo
            String extension = extraerExtension(ruta);

            // Procesar según tipo
            if ("txt".equalsIgnoreCase(extension)) {
                return procesarTexto(path);
            } else if ("csv".equalsIgnoreCase(extension)) {
                return procesarCSV(path);
            } else {
                logger.warn("Formato no soportado: {}", extension);
                return ResultadoProceso.ERROR_FORMATO;
            }
        } catch (Exception e) {
            logger.error("Error al procesar archivo: {}", e.getMessage(), e);
            return ResultadoProceso.ERROR_PROCESAMIENTO;
        }
    }

    private String extraerExtension(String ruta) {
        int punto = ruta.lastIndexOf('.');
        return (punto > 0) ? ruta.substring(punto + 1).toLowerCase() : "";
    }

    private ResultadoProceso procesarTexto(Path path) {
        // Lógica para procesar texto
        return ResultadoProceso.EXITO;
    }

    private ResultadoProceso procesarCSV(Path path) {
        // Lógica para procesar CSV
        return ResultadoProceso.EXITO;
    }

    public enum ResultadoProceso {
        EXITO, ERROR_NO_EXISTE, ERROR_PERMISOS, ERROR_FORMATO, ERROR_PROCESAMIENTO
    }
}
