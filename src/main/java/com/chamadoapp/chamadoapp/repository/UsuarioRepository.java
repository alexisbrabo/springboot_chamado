package com.chamadoapp.chamadoapp.repository;

import com.chamadoapp.chamadoapp.models.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {
    Usuario findByLogin(String login);
}
