package com.hotelaria.quartos.model.entity.dto;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import lombok.Data;

@Data
public class QuartoResponseDro {

    private Integer  id;
    private Integer numeroQuarto;
    private StatusQuarto statusQuarto;
    private String ocupante;
}
