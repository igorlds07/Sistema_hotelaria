package com.hotelaria.quarto_servicos.dto;

import com.hotelaria.quartos.model.entity.StatusQuarto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OcuparQuartoRequestDto {
    private Integer numeroQuarto;
    private Integer idCliente;

}
