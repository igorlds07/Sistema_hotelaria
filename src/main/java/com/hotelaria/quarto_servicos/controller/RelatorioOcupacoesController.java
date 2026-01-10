package com.hotelaria.quarto_servicos.controller;

import com.hotelaria.quarto_servicos.dto.RelatorioOcupacaoResponseDto;
import com.hotelaria.quarto_servicos.service.RelatorioOcupacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/relatorio_ocupacoes")
public class RelatorioOcupacoesController {

    @Autowired
    private RelatorioOcupacaoService relatorioOcupacaoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RelatorioOcupacaoResponseDto>> listarTodos(){
        return ResponseEntity.ok(relatorioOcupacaoService.listarTodos());

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/periodo")
    public ResponseEntity<List<RelatorioOcupacaoResponseDto>> listarPorPeuriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim){
        return ResponseEntity.ok(relatorioOcupacaoService.listarPorPeriodo(inicio, fim));
    }


}
