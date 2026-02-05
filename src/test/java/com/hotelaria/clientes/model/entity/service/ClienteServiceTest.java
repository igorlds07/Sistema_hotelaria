package com.hotelaria.clientes.model.entity.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.dto.ClienteRequestDto;
import com.hotelaria.clientes.model.entity.dto.ClienteResponseDto;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Test
    void deveCriarUmCliente(){

        ClienteRequestDto request = new ClienteRequestDto();
        request.setNome("Cliente Teste");
        request.setCpf("03309145107");
        request.setContato("96206502");

        when(clienteRepository.save(any(ClienteEntity.class)))
                .thenAnswer(inv -> {
                    ClienteEntity entidade = inv.getArgument(0);
                    entidade.setId(1); // simula o ID gerado
                    return entidade;
                });

        ClienteResponseDto response = clienteService.criar(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Cliente Teste", response.getNome());
        assertEquals("96206502", response.getContato());


        verify(clienteRepository).save(any(ClienteEntity.class));

    }

    @Test
    void deveLancarBusinessException_quandoCpfJaExistir() {
        ClienteRequestDto request = new ClienteRequestDto();
        request.setCpf("03309145107");

        when(clienteRepository.existsByCpf(request.getCpf()))
                .thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.criar(request));

        assertEquals("CPF já cadastrado", ex.getMessage());

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveListarTodosOsClientes(){
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("Nome Teste");
        cliente.setCpf("03309145107");
        cliente.setContato("96206502");

        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<ClienteResponseDto> response = clienteService.listar();

        assertEquals(1, response.size());
        assertEquals("Nome Teste", response.get(0).getNome());
        assertEquals("96206502", response.get(0).getContato());

        verify(clienteRepository).findAll();

    }

    @Test
    void deveRetornarA_ListaVaziaQuandoNaoHouverClientes(){
        when(clienteRepository.findAll()).thenReturn(List.of());

        List<ClienteResponseDto> response = clienteService.listar();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(clienteRepository).findAll();
    }
    @Test
    void deveBuscarUmClientePorId(){

        Integer id = 1;

        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("Nome Teste");
        cliente.setId(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteResponseDto response = clienteService.buscarPorId(id);

        assertEquals(1, response.getId());
        assertEquals("Nome Teste", response.getNome());

        verify(clienteRepository).findById(id);

    }
    @Test
    void deveLancarNotFoundException_quandoClienteNaoExistir() {
        Integer id = 1;

        when(clienteRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> clienteService.buscarPorId(id));

        verify(clienteRepository).findById(id);
    }

    @Test
    void deveBuscarE_AtualizarClienteEncontrado(){

        Integer id = 1;

        ClienteEntity clienteExistente = new ClienteEntity();
        clienteExistente.setId(id);
        clienteExistente.setNome("Nome Antigo");
        clienteExistente.setCpf("03309145107");
        clienteExistente.setContato("11111111");

        ClienteRequestDto request = new ClienteRequestDto();
        request.setNome("Nome Novo");
        request.setCpf("03309145107");
        request.setContato("99999999");


        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteResponseDto responseDto = clienteService.atualizar(id, request);

        assertNotNull(responseDto);
        assertEquals(1, responseDto.getId());
        assertEquals("Nome Novo", responseDto.getNome());
        assertEquals("99999999", responseDto.getContato());

        verify(clienteRepository).findById(id);

    }

    @Test
    void deveLancarRuntimedExceptionAoNaoEncontrarCliente(){

        Integer id = 1;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> clienteService.buscarPorId(id));


        verify(clienteRepository).findById(id);
    }

    @Test
    void deveLancarBussinesExceptionSe_CPF_JaExiste(){

        Integer id = 1;

        ClienteEntity clienteExistente = new ClienteEntity();
        clienteExistente.setId(id);
        clienteExistente.setCpf("03309145107");

        ClienteRequestDto request = new ClienteRequestDto();
        request.setCpf("11122233344"); // CPF duplicado
        request.setNome("Nome Atualizado");

        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(clienteExistente));

        when(clienteRepository.existsByCpfAndIdNot(request.getCpf(), id))
                .thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.atualizar(id, request));

        assertEquals("CPF já cadastrado", ex.getMessage());

        verify(clienteRepository).findById(id);
        verify(clienteRepository).existsByCpfAndIdNot(request.getCpf(), id);
        verify(clienteRepository, never()).save(any());
    }

}