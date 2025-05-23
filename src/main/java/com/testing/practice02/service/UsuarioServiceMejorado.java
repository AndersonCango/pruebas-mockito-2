package com.testing.practice02.service;

import com.testing.practice02.model.Usuario;
import com.testing.practice02.repository.UsuarioRepository;

// Versión de UsuarioService que usa la interfaz en lugar de los estáticos
 public class UsuarioServiceMejorado {
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;
    private final UtilsService utilsService;
    public UsuarioServiceMejorado(UsuarioRepository usuarioRepository, 
                                NotificacionService notificacionService,
                                UtilsService utilsService) {
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
        this.utilsService = utilsService;
    }
    public Usuario crearUsuario(Usuario usuario) {
        if (!utilsService.esEmailValido(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        notificacionService.enviarNotificacionRegistro(usuario);
        return usuarioGuardado;
    }
}
