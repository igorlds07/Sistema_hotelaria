package com.hotelaria.usuario.model.entity.service;

import com.hotelaria.usuario.model.entity.Perfil;
import com.hotelaria.usuario.model.entity.UsuarioEntity;
import com.hotelaria.usuario.model.entity.dto.UsuarioRequestDto;
import com.hotelaria.usuario.model.entity.dto.UsuarioResponseDto;
import com.hotelaria.usuario.model.entity.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCriarUmUsuarioComSucesso() {

        UsuarioRequestDto usuario = new UsuarioRequestDto();
        usuario.setNome("Nome Teste");
        usuario.setCpf("03309145107");
        usuario.setEmail("igor@gmail.com");
        usuario.setPerfil(Perfil.ADMIN);
        usuario.setSenha("123456789");

        when(usuarioRepository.save(any(UsuarioEntity.class)))
                .thenAnswer(inv -> {UsuarioEntity usuario1 = inv.getArgument(0);
                    usuario1.setId(1);
                    return  usuario1;
                });

        UsuarioResponseDto response = usuarioService.criar(usuario);

        assertEquals("Nome Teste", response.getNome());
        assertEquals("igor@gmail.com", response.getEmail());
        assertEquals("03309145107", response.getCpf());
        assertEquals(Perfil.ADMIN, response.getPerfil());


        verify(usuarioRepository).save(any(UsuarioEntity.class));

    }

    @Test
    void listar() {
    }

    @Test
    void buscarUsuarios() {
    }

    @Test
    void atualizar() {
    }

    @Test
    void deletar() {
    }
}