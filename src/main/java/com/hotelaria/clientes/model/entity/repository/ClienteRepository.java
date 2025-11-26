package com.hotelaria.clientes.model.entity.repository;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
}
