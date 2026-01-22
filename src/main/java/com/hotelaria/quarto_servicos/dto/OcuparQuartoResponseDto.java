package com.hotelaria.quarto_servicos.dto;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OcuparQuartoResponseDto {
    private Integer id;
    private Integer numeroQuarto;
    private StatusQuarto statusQuarto;
    private Integer ocupanteId;
    private String ocupanteNome;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
}
