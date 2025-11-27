package com.hotelaria.clientes.model.entity.service;

import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
}
