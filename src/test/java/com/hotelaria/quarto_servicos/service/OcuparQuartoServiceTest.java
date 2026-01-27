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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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


    @Test
    void deveListarQuartosOcupados(){

        // Arrange

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1);
        quarto.setNumeroQuarto(25);
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1);

        OcuparQuartoEntity ocuparQuarto = new OcuparQuartoEntity();
        ocuparQuarto.setQuarto(quarto);
        ocuparQuarto.setOcupante(cliente);
        ocuparQuarto.setDataHoraSaida(null);

        // Act
        when(ocuparQuartoRepository.findByDataHoraSaidaIsNull()).thenReturn(List.of(ocuparQuarto));

        List<OcuparQuartoResponseDto> resultado = ocuparQuartoService.listarQuartosOcupados();

        // Assert

        assertEquals(1, resultado.size());
        assertEquals(25, resultado.get(0).getNumeroQuarto());
        assertEquals(StatusQuarto.OCUPADO, resultado.get(0).getStatusQuarto());
        assertEquals(cliente.getId(), resultado.get(0).getOcupanteId());

        verify(ocuparQuartoRepository).findByDataHoraSaidaIsNull();

    }

    @Test
    void deveRetornarUmaListaVazia_QuandoNaoHouverQuartosOcupados(){

        when(ocuparQuartoRepository.findByDataHoraSaidaIsNull()).thenReturn(Collections.emptyList());

        List<OcuparQuartoResponseDto> resultado = ocuparQuartoService.listarQuartosOcupados();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(ocuparQuartoRepository).findByDataHoraSaidaIsNull();

    }

    @Test
    void deveDesocuparSeQuartoOcupado(){

        int numeroQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1);
        quarto.setValorDiaria(new BigDecimal(150));
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1);
        cliente.setNome("Teste");

        OcuparQuartoEntity ocuparQuarto = new OcuparQuartoEntity();
        ocuparQuarto.setQuarto(quarto);
        ocuparQuarto.setOcupante(cliente);
        ocuparQuarto.setDataHoraEntrada(LocalDateTime.now().minusHours(2));
        ocuparQuarto.setDataHoraSaida(null);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto))
                .thenReturn(Optional.of(quarto));

        when(ocuparQuartoRepository.findByQuartoAndDataHoraSaidaIsNull(any(QuartoEntity.class)))
                .thenReturn(Optional.of(ocuparQuarto));

        when(ocuparQuartoRepository
                .findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(numeroQuarto))
                .thenReturn(Optional.of(ocuparQuarto));

        when(ocuparQuartoRepository.save(any(OcuparQuartoEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        when(quartoRepository.save(any(QuartoEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        BigDecimal valorFinal = ocuparQuartoService.desocuparQuarto(numeroQuarto);

        assertNotNull(valorFinal);
        assertEquals(StatusQuarto.DISPONIVEL, quarto.getStatusQuarto());


    }

    @Test
    void deveRetornarErro_AoNaoEncontrarUmQuartoAoDesocupar(){
        Integer numQuarto = 25;

        when(quartoRepository.findByNumeroQuarto(numQuarto)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> ocuparQuartoService.desocuparQuarto(numQuarto));

        assertEquals("Quarto não encontrado!", ex.getMessage());

        verify(quartoRepository).findByNumeroQuarto(numQuarto);
        verifyNoInteractions(ocuparQuartoRepository);
    }

    @Test
    void deveRetornarBussinesException_SeQuartoNaoEstiverOcupado(){
        Integer numeroQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);
        quarto.setNumeroQuarto(numeroQuarto);

        when(quartoRepository.findByNumeroQuarto(quarto.getNumeroQuarto())).thenReturn(Optional.of(quarto));

        when(ocuparQuartoRepository.findByQuartoAndDataHoraSaidaIsNull(quarto)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
        () -> ocuparQuartoService.desocuparQuarto(25));

        assertEquals("O Quarto " + quarto.getNumeroQuarto() + " não está ocupado!" , ex.getMessage() );

        verify(quartoRepository).findByNumeroQuarto(numeroQuarto);
        verify(ocuparQuartoRepository).findByQuartoAndDataHoraSaidaIsNull(quarto);
        verify(ocuparQuartoRepository, never())
                .findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(anyInt());

    }

    @Test
    void deveLancarBussinesException_SeNaoTiverOcupacaoAtiva(){

        Integer numeroQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setNumeroQuarto(numeroQuarto);
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);

        OcuparQuartoEntity ocuparQuarto = new OcuparQuartoEntity();
        ocuparQuarto.setQuarto(quarto);
        ocuparQuarto.setDataHoraSaida(null);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto)).thenReturn(Optional.of(quarto));

        when(ocuparQuartoRepository.findByQuartoAndDataHoraSaidaIsNull(quarto)).thenReturn(Optional.of(ocuparQuarto));

        when(ocuparQuartoRepository.findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(numeroQuarto))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> ocuparQuartoService.desocuparQuarto(numeroQuarto));


        assertEquals("Não existe ocupação ativa para o quarto N° " + numeroQuarto, ex.getMessage());

        verify(quartoRepository).findByNumeroQuarto(numeroQuarto);
        verify(ocuparQuartoRepository).findByQuartoAndDataHoraSaidaIsNull(quarto);
        verify(ocuparQuartoRepository)
                .findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(numeroQuarto);

        verify(quartoRepository, never()).save(any());
        verify(ocuparQuartoRepository, never()).save(any());
    }
    @Test
    void deveDesocuparQuartoComSucesso_cobrandoUmaDiaria() {
        Integer numeroQuarto = 25;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setNumeroQuarto(numeroQuarto);
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);
        quarto.setValorDiaria(new BigDecimal("150.00"));

        OcuparQuartoEntity ocupacao = new OcuparQuartoEntity();
        ocupacao.setQuarto(quarto);
        ocupacao.setDataHoraEntrada(LocalDateTime.now().minusHours(24));
        ocupacao.setDataHoraSaida(null);

        when(quartoRepository.findByNumeroQuarto(numeroQuarto))
                .thenReturn(Optional.of(quarto));

        when(ocuparQuartoRepository.findByQuartoAndDataHoraSaidaIsNull(quarto))
                .thenReturn(Optional.of(ocupacao));

        when(ocuparQuartoRepository
                .findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(numeroQuarto))
                .thenReturn(Optional.of(ocupacao));

        // Act
        BigDecimal valor = ocuparQuartoService.desocuparQuarto(numeroQuarto);

        // Assert
        assertEquals(new BigDecimal("150.00"), valor);
        assertEquals(StatusQuarto.DISPONIVEL, quarto.getStatusQuarto());
        assertNotNull(ocupacao.getDataHoraSaida());

        verify(ocuparQuartoRepository).save(ocupacao);
        verify(quartoRepository).save(quarto);
    }
}