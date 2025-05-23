package com.testing.practice02.service;

import com.testing.practice02.model.Usuario;
import com.testing.practice02.repository.UsuarioRepository;

public class UsuarioServiceConEstaticos {
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;
    public UsuarioServiceConEstaticos(UsuarioRepository usuarioRepository, 
                                     NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }
    public Usuario crearUsuario(Usuario usuario) {
        // Usamos el método estático para validar el email
        if (!UtilsEstaticos.esEmailValido(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        notificacionService.enviarNotificacionRegistro(usuario);
        return usuarioGuardado;
    }
}
