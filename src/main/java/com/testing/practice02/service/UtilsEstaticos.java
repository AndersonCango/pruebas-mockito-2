package com.testing.practice02.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UtilsEstaticos {
    public static String formatearFecha(LocalDate fecha) {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    public static boolean esEmailValido(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public static int generarIdUnico() {
        return (int) (System.currentTimeMillis() % 10000);
    }
}