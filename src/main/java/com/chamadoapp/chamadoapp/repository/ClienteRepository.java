package com.chamadoapp.chamadoapp.repository;

import com.chamadoapp.chamadoapp.models.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, String> {
    Cliente findById(long id);

}