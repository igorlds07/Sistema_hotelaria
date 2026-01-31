package com.hotelaria.clientes.model.entity.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.dto.ClienteRequestDto;
import com.hotelaria.clientes.model.entity.dto.ClienteResponseDto;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import com.hotelaria.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


}