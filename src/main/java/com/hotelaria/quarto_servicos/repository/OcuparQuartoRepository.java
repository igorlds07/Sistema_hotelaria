package com.hotelaria.quarto_servicos.repository;

import com.hotelaria.quarto_servicos.model.entity.OcuparQuartoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OcuparQuartoRepository extends JpaRepository<OcuparQuartoEntity, Integer> {

    Optional<OcuparQuartoEntity> findByQuarto_NumeroQuarto(Integer numeroQuarto);

    Optional<OcuparQuartoEntity> findFirstByQuarto_NumeroQuartoAndDataHoraSaidaIsNull(Integer numeroQuarto);

    Optional<OcuparQuartoEntity> findByQuartoAndDataHoraSaidaIsNull(OcuparQuartoEntity quarto);


    //List<OcuparQuartoEntity> findByQuarto_NumeroQuarto(Integer numeroQuarto);
 }
