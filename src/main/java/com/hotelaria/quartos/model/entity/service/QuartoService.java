package com.hotelaria.quartos.model.entity.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.dto.QuartoOcuparDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.exceptions.BusinessException;
import com.hotelaria.quartos.model.entity.exceptions.NotFoundException;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import com.hotelaria.usuario.model.entity.UsuarioEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public void iniciarQuartos(){
        if (quartoRepository.count() == 0){
            for (int i = 0; i < 15; i++){
                QuartoEntity quarto = new QuartoEntity();
                quarto.setNumeroQuarto(i + 1);
                quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);
                quartoRepository.save(quarto);
            }
        }
    }
    public QuartoOcuparDto ocuparQuarto(Integer numero, Integer idCliente){
        QuartoEntity  quarto = quartoRepository.findByRoom(numero)
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

    public Integer desocuparQuarto(Integer numeroQuarto) {
        QuartoEntity quarto = quartoRepository.findByRoom(numeroQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.DISPONIVEL) {
            throw new BusinessException("O quarto N° " + numeroQuarto + " não está ocupado!");
        }

        LocalDateTime saida = LocalDateTime.now();
        quarto.setDataHoraSaida(saida);

        if (quarto.getDataHoraEntrada() != null){
            Duration duracao = Duration.between(quarto.getDataHoraEntrada(), saida);
            long horas = duracao.toHours();
        }

        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);
        quarto.setOcupante(null);
        quartoRepository.save(quarto);

        return quarto.getId();
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
    public QuartoResponseDto buscarQuartoEspecifio(Integer numQuarto){
        QuartoEntity quarto = quartoRepository.findByRoom(numQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado"));
        return toResponse(quarto);
    }
}
