package com.testing.practice02;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.testing.practice02.model.Usuario;
import com.testing.practice02.repository.UsuarioRepository;
import com.testing.practice02.service.NotificacionService;
import com.testing.practice02.service.UsuarioServiceConEstaticos;
import com.testing.practice02.service.UsuarioServiceMejorado;
import com.testing.practice02.service.UtilsEstaticos;
import com.testing.practice02.service.UtilsService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


 public class MetodosStaticTest {

        @Test
        void testMockearMetodoEstatico() {
                // Usamos try-with-resources para gestionar el ciclo de vida del mock estático
                try (MockedStatic<UtilsEstaticos> utilsMock = mockStatic(UtilsEstaticos.class)) {
                // Configuramos el comportamiento del método estático
                utilsMock.when(() -> UtilsEstaticos.formatearFecha(any(LocalDate.class)))
                        .thenReturn("01/01/2023");
                        
                utilsMock.when(() -> UtilsEstaticos.esEmailValido("cualquier@email.com"))
                        .thenReturn(true);
                        
                utilsMock.when(() -> UtilsEstaticos.esEmailValido("emailinvalido"))
                        .thenReturn(false);
                        
                utilsMock.when(UtilsEstaticos::generarIdUnico)
                        .thenReturn(12345);
                
                // Verificamos que el comportamiento mockeado funciona
                assertEquals("01/01/2023", UtilsEstaticos.formatearFecha(LocalDate.now()));
                assertTrue(UtilsEstaticos.esEmailValido("cualquier@email.com"));
                assertEquals(12345, UtilsEstaticos.generarIdUnico());
                
                // También podemos verificar que los métodos fueron llamados
                utilsMock.verify(() -> UtilsEstaticos.formatearFecha(any()));
                utilsMock.verify(() -> UtilsEstaticos.esEmailValido("cualquier@email.com"));
                utilsMock.verify(UtilsEstaticos::generarIdUnico);
                }
                
                // Fuera del bloque try, los métodos estáticos vuelven a su comportamiento normal
        }

        // Test del servicio con mocking de método estático
        @Test
        void testServicioConMetodoEstatico() {
        // Creamos mocks normales
        UsuarioRepository repositoryMock = mock(UsuarioRepository.class);
        NotificacionService notificacionMock = mock(NotificacionService.class);
        
        // Creamos la instancia del servicio
        UsuarioServiceConEstaticos servicio = new UsuarioServiceConEstaticos(
                repositoryMock, notificacionMock);
        
        // Preparamos datos
        Usuario usuario = new Usuario(1L, "Raúl Pérez", "raul@ejemplo.com");
        when(repositoryMock.save(any())).thenReturn(usuario);
        
        // Mockeamos el método estático
        try (MockedStatic<UtilsEstaticos> utilsMock = mockStatic(UtilsEstaticos.class)) {
                // Configuramos que el validador devuelva true para nuestro email
                utilsMock.when(() -> UtilsEstaticos.esEmailValido("raul@ejemplo.com"))
                        .thenReturn(true);
                
                // Ejecutamos el método que usa el estático
                Usuario resultado = servicio.crearUsuario(usuario);
                
                // Verificamos el resultado
                assertNotNull(resultado);
                
                // Verificamos que el método estático fue llamado
                utilsMock.verify(() -> UtilsEstaticos.esEmailValido("raul@ejemplo.com"));
                
                // Y verificamos el resto de interacciones normales
                verify(repositoryMock).save(usuario);
                verify(notificacionMock).enviarNotificacionRegistro(usuario);
                }
        }

        // Ahora el test es mucho más simple
        @Test
        void testServicioConDependenciaInyectada() {
                UsuarioRepository repositoryMock = mock(UsuarioRepository.class);
                NotificacionService notificacionMock = mock(NotificacionService.class);
                UtilsService utilsMock = mock(UtilsService.class);
                
                UsuarioServiceMejorado servicio = new UsuarioServiceMejorado(
                        repositoryMock, notificacionMock, utilsMock);
                
                Usuario usuario = new Usuario(1L, "Raúl Pérez", "raul@ejemplo.com");
                when(repositoryMock.save(any())).thenReturn(usuario);
                when(utilsMock.esEmailValido("raul@ejemplo.com")).thenReturn(true);
                
                Usuario resultado = servicio.crearUsuario(usuario);
                
                assertNotNull(resultado);
                verify(utilsMock).esEmailValido("raul@ejemplo.com");
                verify(repositoryMock).save(usuario);
                verify(notificacionMock).enviarNotificacionRegistro(usuario);
        }
}