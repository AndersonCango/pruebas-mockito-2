package com.testing.practice02.repository;

import java.util.List;
import java.util.Optional;

import com.testing.practice02.model.Usuario;


public interface UsuarioRepository {
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    Usuario save(Usuario usuario);
    void delete(Long id);
    boolean existsById(Long id);
}