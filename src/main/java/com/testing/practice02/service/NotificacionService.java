package com.testing.practice02.service;

import com.testing.practice02.model.Usuario;

public interface NotificacionService {
    void enviarNotificacionRegistro(Usuario usuario);
    void enviarNotificacionDesactivacion(Usuario usuario);
}