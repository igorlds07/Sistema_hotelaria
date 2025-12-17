package com.hotelaria.quartos.model.entity.controller;

import com.hotelaria.quartos.model.entity.dto.QuartoRequestDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import com.hotelaria.quartos.model.entity.service.QuartoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartos")
public class QuartoController {

    @Autowired
    private QuartoService quartoService;


    @GetMapping
    public ResponseEntity<List<QuartoResponseDto>>listar(){
        return ResponseEntity.ok(quartoService.listarQuartos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuartoResponseDto> buscarQuarto(@Valid @PathVariable Integer id){
        QuartoResponseDto response = quartoService.buscarQuartoEspecifio(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<QuartoResponseDto> criar(@Valid @RequestBody QuartoRequestDto request){
        return ResponseEntity.ok(quartoService.criarQuarto(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuartoResponseDto> atualizarQuarto(
            @PathVariable Integer id, @RequestBody QuartoRequestDto requestDto){
        QuartoResponseDto response = quartoService.atualizarQuarto(id, requestDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuartoResponseDto> deletar(@PathVariable Integer id){
        quartoService.deletar(id);

        return ResponseEntity.noContent().build();

    }


}
