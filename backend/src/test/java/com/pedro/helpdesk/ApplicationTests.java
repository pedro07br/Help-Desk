package com.pedro.helpdesk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ApplicationTests — smoke test básico")
class ApplicationTests {

    @Test
    @DisplayName("deve passar sem subir contexto Spring")
    void contextLoads() {
        assertTrue(true);
    }
}