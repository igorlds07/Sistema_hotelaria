package com.hotelaria.quartos.model.entity.service;

import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.dto.QuartoRequestDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @InjectMocks
    private QuartoService quartoService;


    @Test
    void deveCriarQuartoComSucesso() {

        Integer id = 1;

        QuartoRequestDto quarto = new QuartoRequestDto();
        quarto.setNumeroQuarto(25);
        quarto.setValorDiaria(new BigDecimal(125));

        when(quartoRepository.save(any(QuartoEntity.class)))
                .thenAnswer(inv -> {
                    QuartoEntity entity = inv.getArgument(0);
                    entity.setId(1);
                    return entity;
                });

        QuartoResponseDto response = quartoService.criarQuarto(quarto);

        assertEquals(1, response.getId());
        assertEquals(25, response.getNumeroQuarto());
        assertEquals(new BigDecimal(125), response.getValorDiaria());

        verify(quartoRepository).save(any(QuartoEntity.class));

    }

    @Test
    void deveLancarBusinessExceptionSe_NumeroDoQuartoExiste(){

        Integer numQuarto = 25;

        QuartoRequestDto quarto = new QuartoRequestDto();
        quarto.setNumeroQuarto(numQuarto);
        quarto.setValorDiaria(new BigDecimal(125));

        QuartoEntity quartoExistente = new QuartoEntity();
        quartoExistente.setNumeroQuarto(numQuarto);

        when(quartoRepository.findByNumeroQuarto(numQuarto)).thenReturn(Optional.of(quartoExistente));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> quartoService.criarQuarto(quarto));

        verify(quartoRepository).findByNumeroQuarto(numQuarto);
        assertEquals("Esse quarto já existe!", ex.getMessage());

        verify(quartoRepository, never()).save(any());
    }

    @Test
    void listarQuartos() {
    }

    @Test
    void buscarQuartoEspecifio() {
    }

    @Test
    void atualizarQuarto() {
    }

    @Test
    void deletar() {
    }
}