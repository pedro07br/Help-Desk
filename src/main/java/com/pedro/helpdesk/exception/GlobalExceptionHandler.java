package com.pedro.helpdesk.exception;

import com.pedro.helpdesk.exception.ChamadoNaoEncontradoException;
import com.pedro.helpdesk.exception.TituloInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChamadoNaoEncontradoException.class)
    public ResponseEntity<Object> handleNotFound(ChamadoNaoEncontradoException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", Instant.now());
        corpo.put("status", HttpStatus.NOT_FOUND.value());
        corpo.put("erro", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }

    @ExceptionHandler(TituloInvalidoException.class)
    public ResponseEntity<Object> handleBadRequest(TituloInvalidoException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", Instant.now());
        corpo.put("status", HttpStatus.BAD_REQUEST.value());
        corpo.put("erro", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }
}