package com.hotelaria.quarto_servicos.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
import com.hotelaria.quarto_servicos.dto.OcuparQuartoRequestDto;
import com.hotelaria.quarto_servicos.dto.OcuparQuartoResponseDto;
import com.hotelaria.quarto_servicos.model.entity.OcuparQuartoEntity;
import com.hotelaria.quarto_servicos.repository.OcuparQuartoRepository;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OcuparQuartoServiceTest {

    @InjectMocks
    private OcuparQuartoService ocuparQuartoService;

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private OcuparQuartoRepository ocuparQuartoRepository;

    private OcuparQuartoEntity ocuparQuartoEntity;

    @Test
    void deveOcuparQuarto_quandoQuartoLivreEClienteExiste() {

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1);
        quarto.setNumeroQuarto(25);
        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1);

        when(quartoRepository.findByNumeroQuarto(25)).thenReturn(Optional.of(quarto));
        when(ocuparQuartoRepository.findByQuartoAndDataHoraSaidaIsNull(quarto)).thenReturn(Optional.empty());
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        when(quartoRepository.save(any(QuartoEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(ocuparQuartoRepository.save(any(OcuparQuartoEntity.class))).thenAnswer(inv -> inv.getArgument(0));


        OcuparQuartoResponseDto responseDto = ocuparQuartoService.ocuparQuarto(25, 1);

        // Assert
        assertNotNull(responseDto);
        assertEquals(quarto.getNumeroQuarto(), responseDto.getNumeroQuarto());
        assertEquals(StatusQuarto.OCUPADO, responseDto.getStatusQuarto());
        assertEquals(cliente.getId(), responseDto.getOcupanteId());
        assertEquals(cliente.getNome(), responseDto.getOcupanteNome());
        assertNotNull(responseDto.getDataHoraEntrada());

        // verifica efeitos
        assertEquals(StatusQuarto.OCUPADO, quarto.getStatusQuarto());
        verify(quartoRepository).save(quarto);
        verify(ocuparQuartoRepository).save(any(OcuparQuartoEntity.class));

    }

    @Test
    void deveLancarNotFound_quandoQuartoNaoEncontrado(){

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1);
        quarto.setNumeroQuarto(25);
        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1);

        when(quartoRepository.findByNumeroQuarto(25)).thenReturn(Optional.empty());

        NotFoundException exception =  assertThrows(NotFoundException.class, () ->
                ocuparQuartoService.ocuparQuarto(quarto.getNumeroQuarto(), cliente.getId()));

        assertEquals("Quarto não encontrado!", exception.getMessage());
        verify(quartoRepository).findByNumeroQuarto(quarto.getNumeroQuarto());
        verifyNoInteractions(ocuparQuartoRepository);
        verifyNoInteractions(clienteRepository);
    }

    @Test
    void deveLancarBussinesException_casoQuartoOcupado() {

        Integer idCliente = 1;
        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1);
        quarto.setNumeroQuarto(25);
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);

        when(quartoRepository.findByNumeroQuarto(quarto.getNumeroQuarto())).thenReturn(Optional.of(quarto));
        when(ocuparQuartoRepository.findByQuartoAndDataHoraSaidaIsNull(quarto)).thenReturn(Optional.of(
                new OcuparQuartoEntity()));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                ocuparQuartoService.ocuparQuarto(quarto.getNumeroQuarto(), idCliente)
        );

        assertEquals("O quarto N° " + quarto.getNumeroQuarto() + " já está ocupado!", ex.getMessage());

        verify(clienteRepository, never()).findById(any());
        verify(quartoRepository, never()).save(any());
        verify(ocuparQuartoRepository, never()).save(any());

    }
}