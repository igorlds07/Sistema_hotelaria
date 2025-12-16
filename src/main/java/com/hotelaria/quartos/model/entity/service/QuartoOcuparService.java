package com.hotelaria.quartos.model.entity.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.dto.QuartoOcuparDto;
import com.hotelaria.quartos.model.entity.dto.QuartoRequestDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuartoOcuparService {

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    public QuartoOcuparDto ocuparQuarto(Integer numero, Integer idCliente){
        QuartoEntity  quarto = quartoRepository.findByNumeroQuarto(numero)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.OCUPADO){
            throw new RuntimeException("O quarto N°" + numero + " está ocupado!");
        }

        ClienteEntity cliente = clienteRepository.findById(idCliente)
                        .orElseThrow(() -> new NotFoundException("Cliente não enontrado!"));


        quarto.setStatusQuarto(StatusQuarto.OCUPADO);
        quarto.setOcupante(cliente);
        quarto.setDataHoraEntrada(LocalDateTime.now());
        quarto.setDataHoraSaida(null);
        quartoRepository.save(quarto);

        QuartoOcuparDto dto = new QuartoOcuparDto();
        dto.setNumeroQuarto(quarto.getNumeroQuarto());
        dto.setStatus(quarto.getStatusQuarto());
        dto.setDataHoraEntrada(quarto.getDataHoraEntrada());

        return dto;
    }

    public BigDecimal desocuparQuarto(Integer numeroQuarto) {
        QuartoEntity quarto = quartoRepository.findByNumeroQuarto(numeroQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.DISPONIVEL) {
            throw new BusinessException("O quarto N° " + numeroQuarto + " não está ocupado!");
        }

        LocalDateTime saida = LocalDateTime.now();
        quarto.setDataHoraSaida(saida);

        if (quarto.getDataHoraEntrada() == null) {
            throw new BusinessException("Data de entrada não registrada para o quarto.");
        }

        Duration duracao = Duration.between(quarto.getDataHoraEntrada(), saida);

        long horas = duracao.toHours();
        if (duracao.toMinutes() % 60 != 0) {
            horas++; // arredonda para cima
        }
        horas = Math.max(horas, 1); // mínimo 1 hora/diária

        BigDecimal valorFinalDaEstadia =
                BigDecimal.valueOf(horas).multiply(quarto.getValorDiaria());

        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);
        quarto.setOcupante(null);

        quartoRepository.save(quarto);

        return valorFinalDaEstadia;
    }

    public List<QuartoResponseDto> listarQuarto(){
        return quartoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();

    }
    private QuartoResponseDto toResponse(QuartoEntity quarto){
        QuartoResponseDto dto = new QuartoResponseDto();
        dto.setNumeroQuarto(quarto.getNumeroQuarto());
        dto.setStatusQuarto(quarto.getStatusQuarto());
        dto.setOcupante(quarto.getOcupante() != null ? quarto.getOcupante().getNome() : "Nenhum");

        return dto;
    }



}
