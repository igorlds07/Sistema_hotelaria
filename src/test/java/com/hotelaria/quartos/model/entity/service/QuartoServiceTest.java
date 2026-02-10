package com.hotelaria.quartos.model.entity.service;

import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
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
import java.util.List;
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
    void develistarQuartosComSucesso() {

        Integer numQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setNumeroQuarto(numQuarto);
        quarto.setValorDiaria(new BigDecimal(125));

        when(quartoRepository.findAll()).thenReturn(List.of(quarto));

        List<QuartoResponseDto> response = quartoService.listarQuartos();

        assertEquals(1, response.size());
        assertEquals(25, response.get(0).getNumeroQuarto());
        assertEquals(new BigDecimal(125), response.get(0).getValorDiaria());

        verify(quartoRepository).findAll();

    }

    @Test
    void deveLancarUmaListaVaziaSeNaoHouverQuartos(){

        when(quartoRepository.findAll()).thenReturn(List.of());

        List<QuartoResponseDto> response = quartoService.listarQuartos();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(quartoRepository).findAll();
    }

    @Test
    void deveBuscarQuartoEspecifio() {

        Integer numQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setNumeroQuarto(numQuarto);
        quarto.setValorDiaria(new BigDecimal(125));
        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);

        when(quartoRepository.findByNumeroQuarto(numQuarto)).thenReturn(Optional.of(quarto));

        QuartoResponseDto response = quartoService.buscarQuartoEspecifio(quarto.getNumeroQuarto());

        assertEquals(25, response.getNumeroQuarto());

        verify(quartoRepository).findByNumeroQuarto(numQuarto);
    }

    @Test
    void deveLancarNotFoundException_QuandoQuartoNaoExistir(){

        Integer numQuarto = 25;

        when(quartoRepository.findByNumeroQuarto(numQuarto)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> quartoService.buscarQuartoEspecifio(numQuarto));

    }

    @Test
    void deveAtualizarQuartoComSucesso() {

        Integer idQuarto = 1;
        Integer numQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(idQuarto);
        quarto.setNumeroQuarto(numQuarto);
        quarto.setValorDiaria(new BigDecimal(125));
        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);

        QuartoRequestDto requestDto = new QuartoRequestDto();
        requestDto.setValorDiaria(new BigDecimal(130));
        requestDto.setNumeroQuarto(26);

        when(quartoRepository.findByNumeroQuarto(numQuarto)).thenReturn(Optional.of(quarto));
        when(quartoRepository.save(any(QuartoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        QuartoResponseDto response = quartoService.atualizarQuarto(numQuarto, requestDto);

        assertNotNull(response);
        assertEquals(26, response.getNumeroQuarto());
        assertEquals(new BigDecimal(130), response.getValorDiaria());

        verify(quartoRepository).findByNumeroQuarto(numQuarto);
        verify(quartoRepository).save(any(QuartoEntity.class));
    }

    @Test
    void deveLancarNotFoundEcpetionSeQuartoNaoEncontrado(){

        Integer numQuarto = 25;
        QuartoRequestDto request = new QuartoRequestDto();
        request.setNumeroQuarto(26);
        request.setValorDiaria(new BigDecimal("130"));

        when(quartoRepository.findByNumeroQuarto(numQuarto))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> quartoService.atualizarQuarto(numQuarto, request));

        assertEquals("Quarto não encontrado!", ex.getMessage());

        verify(quartoRepository).findByNumeroQuarto(numQuarto);
        verify(quartoRepository, never()).save(any());
    }

    @Test
    void deletar() {
    }
}