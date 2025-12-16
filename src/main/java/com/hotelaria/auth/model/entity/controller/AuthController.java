package com.hotelaria.auth.model.entity.controller;

import com.hotelaria.auth.model.entity.dto.LoginRequestDto;
import com.hotelaria.auth.model.entity.dto.LoginResponseDto;
import com.hotelaria.auth.model.entity.service.JwtService;
import com.hotelaria.exceptions.NotFoundException;
import com.hotelaria.usuario.model.entity.Perfil;
import com.hotelaria.usuario.model.entity.UsuarioEntity;
import com.hotelaria.usuario.model.entity.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) throws AccessDeniedException {
        UsuarioEntity usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Email não encontrado."));
        if (passwordEncoder.matches(request.getSenha(), usuario.getSenha())){
            throw new BadCredentialsException("Senha invalída!");
        }
        if (usuario.getPerfil() != Perfil.ADMIN){
            throw new AccessDeniedException("Acesso negado");
        }
        String token = jwtService.gerarToken(usuario);

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setEmail(usuario.getEmail());
        response.setSenha(usuario.getSenha());

        return response;
    }

}
