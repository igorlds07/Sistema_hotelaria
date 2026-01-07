package com.hotelaria.quarto_servicos.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
import com.hotelaria.quarto_servicos.dto.OcuparQuartoResponseDto;
import com.hotelaria.quarto_servicos.model.entity.OcuparQuartoEntity;
import com.hotelaria.quarto_servicos.repository.OcuparQuartoRepository;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OcuparQuartoService {

    @Autowired
    private OcuparQuartoRepository ocuparQuartoRepository;

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public OcuparQuartoResponseDto ocuparQuarto(Integer numeroQuarto, Integer idCliente) {

        QuartoEntity quarto = quartoRepository.findByNumeroQuarto(numeroQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.OCUPADO) {
            throw new BusinessException("O quarto N° " + numeroQuarto + " já está ocupado!");
        }

        ClienteEntity cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado!"));

        // cria uma ocupação (registro de estadia)
        OcuparQuartoEntity ocupacao = new OcuparQuartoEntity();
        ocupacao.setQuarto(quarto);
        ocupacao.setOcupante(cliente);
        ocupacao.setStatusQuarto(StatusQuarto.OCUPADO);
        ocupacao.setValorDiaria(quarto.getValorDiaria());
        ocupacao.setDataHoraEntrada(LocalDateTime.now());
        ocupacao.setDataHoraSaida(null);

        // muda o status do quarto
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);


        quartoRepository.save(quarto);
        ocuparQuartoRepository.save(ocupacao);

        // monta o response
        OcuparQuartoResponseDto dto = new OcuparQuartoResponseDto();
        dto.setNumeroQuarto(quarto.getNumeroQuarto());
        dto.setStatusQuarto(quarto.getStatusQuarto());
        dto.setDataHoraEntrada(ocupacao.getDataHoraEntrada());
        dto.setOcupanteId(cliente.getId());
        dto.setOcupanteNome(cliente.getNome());

        return dto;
    }

    public List<OcuparQuartoResponseDto> listarQuartosOcupados(){
        return ocuparQuartoRepository.findAll().stream().
                map(this::toResponse).toList();
    }


    public BigDecimal desocuparQuarto(Integer numeroQuarto) {

        QuartoEntity quarto = quartoRepository.findByNumeroQuarto(numeroQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.DISPONIVEL) {
            throw new BusinessException("O quarto N° " + numeroQuarto + " não está ocupado!");
        }

        // pega a ocupação ATIVA (a que ainda não tem dataHoraSaida)
        OcuparQuartoEntity ocupacao = ocuparQuartoRepository
                .findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(numeroQuarto)
                .orElseThrow(() -> new BusinessException("Não existe ocupação ativa para o quarto N° " + numeroQuarto));

        LocalDateTime saida = LocalDateTime.now();
        ocupacao.setDataHoraSaida(saida);

        LocalDateTime entrada = ocupacao.getDataHoraEntrada();
        if (entrada == null) {
            throw new BusinessException("Data de entrada não registrada para o quarto.");
        }

        Duration duracao = Duration.between(entrada, saida);

        // cobrando por hora arredondando pra cima (igual você fez)
        long horas = duracao.toHours();
        if (duracao.toMinutes() % 60 != 0) horas++;
        horas = Math.max(horas, 1);

        BigDecimal valorFinal = BigDecimal.valueOf(horas)
                .multiply(quarto.getValorDiaria())
                .setScale(2, RoundingMode.HALF_UP);

        // libera o quarto
        quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);

        // salva tudo
        ocuparQuartoRepository.save(ocupacao);
        quartoRepository.save(quarto);

        return valorFinal;
    }

    private OcuparQuartoResponseDto toResponse(OcuparQuartoEntity ocupacao) {

        OcuparQuartoResponseDto dto = new OcuparQuartoResponseDto();

        dto.setId(ocupacao.getId());
        dto.setStatusQuarto(ocupacao.getStatusQuarto());

        dto.setNumeroQuarto(ocupacao.getQuarto().getNumeroQuarto());

        dto.setDataHoraEntrada(ocupacao.getDataHoraEntrada());

        dto.setOcupanteNome(ocupacao.getOcupante().getNome());

        return dto;
    }

}
