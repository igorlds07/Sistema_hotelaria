package com.hotelaria.quartos.model.entity.dto;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import lombok.Data;

@Data
public class QuartoRequestDto {
    private Integer numeroQuarto;
}
