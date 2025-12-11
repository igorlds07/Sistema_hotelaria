package com.hotelaria.quartos.model.entity.dto;

import com.hotelaria.quartos.model.entity.StatusQuarto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuartoOcuparDto {
    private Integer numeroQuarto;
    private Integer IdCliente;
    private LocalDateTime dataHoraEntrada;
    private StatusQuarto status;
}
