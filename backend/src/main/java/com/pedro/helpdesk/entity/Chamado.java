package com.pedro.helpdesk.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "chamado")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Instant dataCriacao;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ABERTO;

    public enum Status {
        ABERTO,
        EM_ANDAMENTO,
        FECHADO
    }

    public Chamado() {
    }

    public Chamado(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = Status.ABERTO; // Garante o status inicial
    }

    // Getters e Setters
    public Long getId() { // Alterado para Long
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
