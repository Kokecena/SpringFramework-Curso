package com.github.kokecena.curso.dao;

import com.github.kokecena.curso.models.Usuario;

import java.util.List;

public interface UsuarioDAO {

    List<Usuario> getUsuarios();

    void delete(Long id);

    void register(Usuario usuario);

    Usuario getUserByCredentials(Usuario usuario);
}
