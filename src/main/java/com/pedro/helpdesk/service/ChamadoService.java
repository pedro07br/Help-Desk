package com.pedro.helpdesk.service;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.repository.ChamadoRepository;
import com.pedro.helpdesk.exception.ChamadoNaoEncontradoException;
import com.pedro.helpdesk.exception.TituloInvalidoException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public Chamado salvar(Chamado chamado) {
        if (chamado.getTitulo() == null || chamado.getTitulo().isBlank()) {
            throw new TituloInvalidoException("O título do chamado é obrigatório.");
        }
        if (chamado.getStatus() == null) {
            chamado.setStatus(Chamado.Status.ABERTO);
        }
        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public Chamado buscarPorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException("ID " + id + " não encontrado!"));
    }
}