package com.chamadoapp.chamadoapp.repository;

import com.chamadoapp.chamadoapp.models.Funcionario;
import org.springframework.data.repository.CrudRepository;

public interface FuncionarioRepository extends CrudRepository<Funcionario, String> {
    Funcionario findById(long id);

}