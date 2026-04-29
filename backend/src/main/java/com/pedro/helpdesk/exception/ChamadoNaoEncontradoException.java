package com.pedro.helpdesk.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {


    public ChamadoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}