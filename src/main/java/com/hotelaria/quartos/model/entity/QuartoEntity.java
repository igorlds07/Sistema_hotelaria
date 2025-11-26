package com.hotelaria.quartos.model.entity;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class QuartoEntity {
    private List<QuartoEntity> quartos = new ArrayList<>(15);
    private StatusQuarto statusQuarto;
    private ClienteEntity ocupante;


}
