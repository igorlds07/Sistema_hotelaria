package com.hotelaria.usuario.model.entity.service;

import com.hotelaria.usuario.model.entity.UsuarioEntity;
import com.hotelaria.usuario.model.entity.dto.UsuarioRequestDto;
import com.hotelaria.usuario.model.entity.dto.UsuarioResponseDto;
import com.hotelaria.usuario.model.entity.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    }

    public List<UsuarioResponseDto> listar(){
        List<UsuarioEntity> entites = usuarioRepository.findAll();
        List<UsuarioResponseDto> response = new ArrayList<>();
        for (UsuarioEntity e: entites){
            UsuarioResponseDto novo = new UsuarioResponseDto();
            BeanUtils.copyProperties(entites, response);
            response.add(novo);
        }
        return response;
    }

    public Integer atualizar(Integer id, UsuarioEntity request){
        UsuarioEntity user = usuarioRepository.findById(id).orElse(null);

        if (user != null){
            BeanUtils.copyProperties(user, request);
            return usuarioRepository.save(request).getId();
        }
        return null;

    }
    public Integer deletar(Integer id){
        UsuarioEntity user = usuarioRepository.findById(id).orElse(null);

        if (user != null){
            usuarioRepository.deleteById(id);
        }
        return null;
    }
}
