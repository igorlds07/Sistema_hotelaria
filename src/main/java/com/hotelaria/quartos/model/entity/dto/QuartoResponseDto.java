package com.hotelaria.quartos.model.entity.dto;

import com.hotelaria.quartos.model.entity.StatusQuarto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuartoResponseDto {

    private Integer  id;
    private Integer numeroQuarto;
    private StatusQuarto statusQuarto;
    private String ocupante;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
}
