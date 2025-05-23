package com.testing.practice02.service;

import java.time.LocalDate;

// Interfaz para los utils
public interface UtilsService {
    String formatearFecha(LocalDate fecha);
    boolean esEmailValido(String email);
    int generarIdUnico();
}
 
