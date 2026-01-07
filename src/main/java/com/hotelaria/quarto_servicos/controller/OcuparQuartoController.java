package com.hotelaria.quarto_servicos.controller;//package com.hotelaria.quarto_servicos.controller;

import com.hotelaria.quarto_servicos.dto.OcuparQuartoRequestDto;
import com.hotelaria.quarto_servicos.dto.OcuparQuartoResponseDto;
import com.hotelaria.quarto_servicos.service.OcuparQuartoService;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.service.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/servicos_quartos")
@RestController
public class OcuparQuartoController {


    @Autowired
    private OcuparQuartoService ocuparQuartoService;

    @Autowired
    private QuartoService quartoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{numero}")
    public ResponseEntity<QuartoResponseDto> buscarPorNumero(@PathVariable Integer numeroQuarto){
        return ResponseEntity.ok(quartoService.buscarQuartoEspecifio(numeroQuarto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ocupacoes")
    public ResponseEntity<List<OcuparQuartoResponseDto>> quartosOcupados(){
        return ResponseEntity.ok(ocuparQuartoService.listarQuartosOcupados());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ocupar")
    public ResponseEntity <?> ocuparQuarto(
            @RequestBody OcuparQuartoRequestDto request){

        OcuparQuartoResponseDto dto = ocuparQuartoService.ocuparQuarto(request.getNumeroQuarto(), request.getIdCliente());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/desocupar/{numeroQuarto}")
    public ResponseEntity<OcuparQuartoResponseDto> desouparQuarto(@PathVariable Integer numeroQuarto){
        ocuparQuartoService.desocuparQuarto(numeroQuarto);
        return ResponseEntity.noContent().build();
    }
}
