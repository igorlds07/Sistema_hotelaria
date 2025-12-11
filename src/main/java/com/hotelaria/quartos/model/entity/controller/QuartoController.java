package com.hotelaria.quartos.model.entity.controller;

import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.dto.QuartoOcuparDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.service.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quarto")
public class QuartoController {

    @Autowired
    private QuartoService quartoService;

    @PostMapping("/init")
    public ResponseEntity<Void> iniciarQuartos(){
        quartoService.iniciarQuartos();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuartoResponseDto>> listar(){
        return ResponseEntity.ok(quartoService.listarQuarto());
    }

    @GetMapping("/{numero}")
    public ResponseEntity<QuartoResponseDto> buscarPorNumero(@PathVariable Integer numeroQuarto){
        return ResponseEntity.ok(quartoService.buscarQuartoEspecifio(numeroQuarto));
    }

    @PostMapping("/ocupar")
    public ResponseEntity <QuartoOcuparDto> ocuparQuarto(
            @RequestParam Integer numQuarto, @RequestParam Integer idCliente){

            QuartoOcuparDto dto = quartoService.ocuparQuarto(numQuarto, idCliente);
            return ResponseEntity.ok(dto);
    }

    @PostMapping("/desocupar/numero")
    public ResponseEntity<QuartoOcuparDto> desouparQuarto(@PathVariable Integer numeroQuarto){
        quartoService.desocuparQuarto(numeroQuarto);
        return ResponseEntity.noContent().build();
    }
}
