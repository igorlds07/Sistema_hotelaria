package com.hotelaria.quarto_servicos.model.entity;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

public class OcuparQuartoEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quarto_id", nullable = false)
    private QuartoEntity quarto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusQuarto statusQuarto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ocupante_id", nullable = false)
    private ClienteEntity ocupante;

    @Column(nullable = false)
    @NotNull
    private BigDecimal valorDiaria;

    @Column(nullable = false)
    private LocalDateTime dataHoraEntrada;

    private LocalDateTime dataHoraSaida;

}
