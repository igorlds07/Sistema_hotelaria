package com.hotelaria.quartos.model.entity.repository;

import com.hotelaria.quartos.model.entity.QuartoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuartoRepository extends JpaRepository<QuartoEntity, Integer> {

    Optional<QuartoEntity> findByRoom (Integer numero);

}
