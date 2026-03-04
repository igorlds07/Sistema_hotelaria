package com.hotelaria.auth.model.entity.service;

import com.hotelaria.usuario.model.entity.Perfil;
import com.hotelaria.usuario.model.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;


    private static final String SECRET = "4F9A8D2C7E6B1A90C5F3E8D71B4A6F92C0E7D5A9B1F8642E3C9A7D5B0E8F6A1";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
    }

    @Test
    void deveGerarTokenEExtrairEmail() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setEmail("teste@email.com");
        usuario.setPerfil(Perfil.ADMIN);

        String token = jwtService.gerarToken(usuario);

        String emailExtraido = jwtService.extrairEmail(token);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("teste@email.com", emailExtraido);
    }

    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
        assertThrows(Exception.class, () -> jwtService.extrairEmail("token-invalido"));
    }

}