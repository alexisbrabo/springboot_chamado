package com.chamadoapp.chamadoapp.repository;

import com.chamadoapp.chamadoapp.models.Chamado;
import org.springframework.data.repository.CrudRepository;

public interface ChamadoRepository extends CrudRepository<Chamado, String> {
    Chamado findById(long id);
}
