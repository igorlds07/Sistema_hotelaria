package com.hotelaria.quartos.model.entity;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class QuartoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer numeroQuarto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusQuarto statusQuarto;

    @ManyToOne
    private ClienteEntity ocupante;

    @Column(nullable = false)
    private LocalDateTime dataHoraEntrada;

    @Column(nullable = false)
    private LocalDateTime dataHoraSaida;

}
