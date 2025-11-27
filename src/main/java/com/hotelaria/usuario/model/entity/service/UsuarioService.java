package com.hotelaria.usuario.model.entity.service;

import com.hotelaria.usuario.model.entity.UsuarioEntity;
import com.hotelaria.usuario.model.entity.dto.UsuarioRequestDto;
import com.hotelaria.usuario.model.entity.dto.UsuarioResponseDto;
import com.hotelaria.usuario.model.entity.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public UsuarioResponseDto criar(UsuarioRequestDto request){
        UsuarioEntity novoUsuario = new UsuarioEntity();
        BeanUtils.copyProperties(request, novoUsuario);

        UsuarioEntity salvo = usuarioRepository.save(novoUsuario);

        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setEmail(salvo.getEmail());
        response.setCpf(salvo.getCpf());
        response.setPerfil(salvo.getPerfil());

        return response;


        return ;
    }

}
