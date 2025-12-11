package com.hotelaria.clientes.model.entity.controller;

import com.hotelaria.clientes.model.entity.dto.ClienteRequestDto;
import com.hotelaria.clientes.model.entity.dto.ClienteResponseDto;
import com.hotelaria.clientes.model.entity.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody ClienteRequestDto requestDto){
        ClienteResponseDto cliente = clienteService.criar(requestDto);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listar(){
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarCliente(@PathVariable Integer id){
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(
            @PathVariable Integer idCliente, @Valid @RequestBody ClienteRequestDto requestDto
    ){
        ClienteResponseDto cliente = clienteService.atualizar(idCliente, requestDto);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> deletar(@PathVariable Integer idCliente){
        clienteService.deletar(idCliente);
        return ResponseEntity.noContent().build();
    }
}
