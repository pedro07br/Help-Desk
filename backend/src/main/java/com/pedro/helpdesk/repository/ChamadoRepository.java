package com.pedro.helpdesk.repository;

import com.pedro.helpdesk.entity.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByStatus(Chamado.Status status);
    List<Chamado> findByTitulo(String titulo);
    List<Chamado> findByStatusAndTitulo(Chamado.Status status, String titulo);
    List<Chamado> findByTituloContaining(String titulo);
}
