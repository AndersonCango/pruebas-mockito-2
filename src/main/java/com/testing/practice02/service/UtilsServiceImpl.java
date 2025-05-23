package com.testing.practice02.service;

import java.time.LocalDate;

// Implementación que usa los métodos estáticos
 public class UtilsServiceImpl implements UtilsService {
    @Override
    public String formatearFecha(LocalDate fecha) {
        return UtilsEstaticos.formatearFecha(fecha);
    }
    
    @Override
    public boolean esEmailValido(String email) {
        return UtilsEstaticos.esEmailValido(email);
    }
    
    @Override
    public int generarIdUnico() {
        return UtilsEstaticos.generarIdUnico();
    }
}