package com.hotelaria.usuario.model.entity.service;

import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void deveLancarBusinessExceptionSeEmailJaExistente(){

        String emailExistente = "igor@gmail.com";

        UsuarioRequestDto usuario = new UsuarioRequestDto();
        usuario.setEmail(emailExistente);

        UsuarioEntity usuarioEmailExistente = new UsuarioEntity();
        usuarioEmailExistente.setEmail(emailExistente);

        when(usuarioRepository.existsByEmail(emailExistente)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> usuarioService.criar(usuario));

        verify(usuarioRepository).existsByEmail(emailExistente);
        assertEquals("Email existente!", ex.getMessage());

        verify(usuarioRepository, never()).save(any());

    }

    @Test
    void listarTodosOsUsuariosCadastrados() {

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome("Nome Teste");
        usuario.setCpf("03309145107");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List <UsuarioResponseDto> response = usuarioService.listar();

        assertEquals(1, response.size());
        assertEquals("Nome Teste", response.get(0).getNome());
        assertEquals("03309145107", response.get(0).getCpf());

        verify(usuarioRepository).findAll();

    }
    @Test
    void deveRetornarUmaListaVazia(){

        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDto> response = usuarioService.listar();

        assertNotNull(response);
        assertTrue(response.isEmpty());

    }

    @Test
    void deveBuscarUmUsuarioComSucesso() {

        Integer id = 1;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(id);
        usuario.setNome("Nome Teste");
        usuario.setCpf("03309145107");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioResponseDto response = usuarioService.buscarUsuarios(id);

        assertEquals(1, response.getId());
        assertEquals("Nome Teste", response.getNome());
        assertEquals("03309145107", response.getCpf());

    }
    @Test
    void deveLancarNotFoundExceptionSeNaoEncontrarUsuario(){

        Integer id = 1;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> usuarioService.buscarUsuarios(id));

        //UsuarioResponseDto response = usuarioService.buscarUsuarios(id);

        assertEquals("Usuário não encontrado", ex.getMessage());

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, never()).save(any());

    }

    @Test
    void deveAtualizarUsuarioComSucesso() {

        Integer id = 1;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(id);
        usuario.setNome("Nome Teste");

        UsuarioRequestDto usuarioAtualizar = new UsuarioRequestDto();
        usuarioAtualizar.setNome("Novo Nome Teste");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDto response = usuarioService.atualizar(id, usuarioAtualizar);

        assertEquals("Novo Nome Teste", response.getNome());

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(any());
    }

    @Test
    void deletar() {
    }
}