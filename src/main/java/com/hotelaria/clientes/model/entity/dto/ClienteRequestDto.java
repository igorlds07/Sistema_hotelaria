package com.hotelaria.clientes.model.entity.dto;

import lombok.Data;

@Data
public class ClienteRequestDto {
    private String nome;
    private String contato;
    private String cpf;
}
