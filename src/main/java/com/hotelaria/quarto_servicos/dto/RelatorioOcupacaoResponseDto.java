package com.hotelaria.quarto_servicos.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RelatorioOcupacaoResponseDto {

    private Integer ocupacaoId;
    private Integer numQuarto;

    private Integer clienteId;
    private String nomeCliente;

    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;

    private Long horasOcupadas;

    private BigDecimal valorDiaria;
    private BigDecimal valorFinal;

}
