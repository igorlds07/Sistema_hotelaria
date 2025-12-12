package com.hotelaria.usuario.model.entity.controller;

import com.hotelaria.usuario.model.entity.dto.UsuarioRequestDto;
import com.hotelaria.usuario.model.entity.dto.UsuarioResponseDto;
import com.hotelaria.usuario.model.entity.service.UsuarioService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@RequestBody UsuarioRequestDto request){
        UsuarioResponseDto usuario = usuarioService.criar(request);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listar());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarios(@PathVariable Integer id){
        UsuarioResponseDto response = usuarioService.buscarUsuarios(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atulizarUsuarios(
            @PathVariable Integer id,
            @RequestBody UsuarioRequestDto request
    ){
        UsuarioResponseDto response = usuarioService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> deletarUsuario(@PathVariable Integer id){
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
