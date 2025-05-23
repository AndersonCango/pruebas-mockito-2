package com.testing.practice02;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.testing.practice02.service.ProcesadorArchivos;
import com.testing.practice02.service.ProcesadorArchivos.ResultadoProceso;

@ExtendWith(MockitoExtension.class)
class ProcesadorArchivosTest {

    private final Logger logger = mock(Logger.class);
    private final ProcesadorArchivos procesador = new ProcesadorArchivos(logger);

    @Test
    void testArchivoNoExiste() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            Path path = Path.of("noexiste.txt");
            filesMock.when(() -> Files.exists(path)).thenReturn(false);

            ResultadoProceso resultado = procesador.procesarArchivo("noexiste.txt");

            assertEquals(ResultadoProceso.ERROR_NO_EXISTE, resultado);
            verify(logger).error(eq("Archivo no encontrado: {}"), eq("noexiste.txt"));
        }
    }

    @Test
    void testSinPermisos() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            Path path = Path.of("sinpermisos.txt");
            filesMock.when(() -> Files.exists(path)).thenReturn(true);
            filesMock.when(() -> Files.isReadable(path)).thenReturn(false);

            ResultadoProceso resultado = procesador.procesarArchivo("sinpermisos.txt");

            assertEquals(ResultadoProceso.ERROR_PERMISOS, resultado);
            verify(logger).error(eq("Sin permisos para leer: {}"), eq("sinpermisos.txt"));
        }
    }

    @Test
    void testArchivoTexto() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            Path path = Path.of("texto.txt");
            filesMock.when(() -> Files.exists(path)).thenReturn(true);
            filesMock.when(() -> Files.isReadable(path)).thenReturn(true);

            ResultadoProceso resultado = procesador.procesarArchivo("texto.txt");

            assertEquals(ResultadoProceso.EXITO, resultado);
        }
    }

    @Test
    void testArchivoCSV() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            Path path = Path.of("datos.csv");
            filesMock.when(() -> Files.exists(path)).thenReturn(true);
            filesMock.when(() -> Files.isReadable(path)).thenReturn(true);

            ResultadoProceso resultado = procesador.procesarArchivo("datos.csv");

            assertEquals(ResultadoProceso.EXITO, resultado);
        }
    }

    @Test
    void testFormatoNoSoportado() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            Path path = Path.of("imagen.jpg");
            filesMock.when(() -> Files.exists(path)).thenReturn(true);
            filesMock.when(() -> Files.isReadable(path)).thenReturn(true);

            ResultadoProceso resultado = procesador.procesarArchivo("imagen.jpg");

            assertEquals(ResultadoProceso.ERROR_FORMATO, resultado);
            verify(logger).warn(eq("Formato no soportado: {}"), eq("jpg"));
        }
    }

    @Test
    void testExcepcionDuranteProcesamiento() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            Path path = Path.of("error.txt");
            filesMock.when(() -> Files.exists(path)).thenThrow(new RuntimeException("Error simulado"));

            ResultadoProceso resultado = procesador.procesarArchivo("error.txt");

            assertEquals(ResultadoProceso.ERROR_PROCESAMIENTO, resultado);
            verify(logger).error(eq("Error al procesar archivo: {}"), eq("Error simulado"), any(Throwable.class));
        }
    }
}
