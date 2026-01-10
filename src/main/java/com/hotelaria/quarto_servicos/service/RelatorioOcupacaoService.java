package com.hotelaria.quarto_servicos.service;

import com.hotelaria.quarto_servicos.dto.RelatorioOcupacaoResponseDto;
import com.hotelaria.quarto_servicos.model.entity.OcuparQuartoEntity;
import com.hotelaria.quarto_servicos.repository.OcuparQuartoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioOcupacaoService {

    @Autowired
    private OcuparQuartoRepository ocuparQuartoRepository;


    private RelatorioOcupacaoResponseDto toDto(OcuparQuartoEntity ocupacao) {

        RelatorioOcupacaoResponseDto dto = new RelatorioOcupacaoResponseDto();

        dto.setOcupacaoId(ocupacao.getId());

        dto.setNumQuarto(ocupacao.getQuarto().getNumeroQuarto());

        dto.setClienteId(ocupacao.getOcupante().getId());
        dto.setNomeCliente(ocupacao.getOcupante().getNome());

        dto.setDataHoraEntrada(ocupacao.getDataHoraEntrada());
        dto.setDataHoraSaida(ocupacao.getDataHoraSaida());

        Duration duracao = Duration.between(
                ocupacao.getDataHoraEntrada(),
                ocupacao.getDataHoraSaida()
        );

        long horas = duracao.toHours();
        if (duracao.toMinutes() % 60 != 0) horas++;
        horas = Math.max(horas, 1);

        dto.setHorasOcupadas(horas);
        dto.setValorDiaria(ocupacao.getValorDiaria());
        dto.setValorFinal(
                ocupacao.getValorDiaria().multiply(BigDecimal.valueOf(horas))
        );

        return dto;
    }

    public List<RelatorioOcupacaoResponseDto> listarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim
    ) {

        return ocuparQuartoRepository
                .findByDataHoraSaidaBetween(inicio, fim)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<RelatorioOcupacaoResponseDto> listarTodos(){
        return ocuparQuartoRepository.findAll()
                .stream().map(ocuparQuartoEntity ->
                        {RelatorioOcupacaoResponseDto responseDto = new RelatorioOcupacaoResponseDto();
                            BeanUtils.copyProperties(ocuparQuartoEntity, responseDto);
                            return responseDto;
                        }).toList();
    }


}
