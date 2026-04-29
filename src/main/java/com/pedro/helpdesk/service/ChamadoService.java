package com.pedro.helpdesk.service;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.exception.ChamadoNaoEncontradoException;
import com.pedro.helpdesk.exception.TituloInvalidoException;
import com.pedro.helpdesk.kafka.ChamadoProducer;
import com.pedro.helpdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ChamadoProducer chamadoProducer;

    public ChamadoService(ChamadoRepository chamadoRepository, ChamadoProducer chamadoProducer) {
        this.chamadoRepository = chamadoRepository;
        this.chamadoProducer = chamadoProducer;
    }

    /** Cria um novo chamado (status inicial = ABERTO) e publica no Kafka. */
    public Chamado salvar(Chamado chamado) {
        if (chamado.getTitulo() == null || chamado.getTitulo().isBlank()) {
            throw new TituloInvalidoException("O título do chamado é obrigatório.");
        }
        if (chamado.getStatus() == null) {
            chamado.setStatus(Chamado.Status.ABERTO);
        }

        Chamado salvo = chamadoRepository.save(chamado);

        // Publica o evento no tópico correspondente ao status
        chamadoProducer.publicar(salvo);

        return salvo;
    }

    /** Atualiza o status de um chamado existente e publica o novo evento no Kafka. */
    public Chamado atualizarStatus(Long id, Chamado.Status novoStatus) {
        Chamado chamado = buscarPorId(id);
        chamado.setStatus(novoStatus);

        Chamado atualizado = chamadoRepository.save(chamado);

        // Publica o evento de mudança de status
        chamadoProducer.publicar(atualizado);

        return atualizado;
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public List<Chamado> listarPorStatus(Chamado.Status status) {
        return chamadoRepository.findByStatus(status);
    }

    public Chamado buscarPorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException("ID " + id + " não encontrado!"));
    }
}
