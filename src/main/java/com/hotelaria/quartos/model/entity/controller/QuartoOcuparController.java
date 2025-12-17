package com.hotelaria.quartos.model.entity.controller;

import com.hotelaria.quartos.model.entity.dto.QuartoOcuparDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.service.QuartoOcuparService;
import com.hotelaria.quartos.model.entity.service.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quarto/servicos")
public class QuartoOcuparController {

    @Autowired
    private QuartoService quartoService;

    @Autowired
    private  QuartoOcuparService quartoOcuparService;



    @GetMapping
    public ResponseEntity<List<QuartoResponseDto>> listar(){
        return ResponseEntity.ok(quartoOcuparService.listarQuarto());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{numero}")
    public ResponseEntity<QuartoResponseDto> buscarPorNumero(@PathVariable Integer numeroQuarto){
        return ResponseEntity.ok(quartoService.buscarQuartoEspecifio(numeroQuarto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ocupar")
    public ResponseEntity <?> ocuparQuarto(
            @RequestBody QuartoOcuparDto request){

            QuartoOcuparDto dto = quartoOcuparService.ocuparQuarto(request.getNumeroQuarto(), request.getIdCliente());
            return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/desocupar/numero")
    public ResponseEntity<QuartoOcuparDto> desouparQuarto(@PathVariable Integer numeroQuarto){
        quartoOcuparService.desocuparQuarto(numeroQuarto);
        return ResponseEntity.noContent().build();
    }
}
