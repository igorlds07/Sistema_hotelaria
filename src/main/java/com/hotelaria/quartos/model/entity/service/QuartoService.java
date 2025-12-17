package com.hotelaria.quartos.model.entity.service;

import com.hotelaria.exceptions.BusinessException;
import com.hotelaria.exceptions.NotFoundException;
import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.dto.QuartoRequestDto;
import com.hotelaria.quartos.model.entity.dto.QuartoResponseDto;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository quartoRepository;

    public QuartoResponseDto criarQuarto(@RequestBody QuartoRequestDto request){

        if (quartoRepository.findByNumeroQuarto(request.getNumeroQuarto()).isPresent()){
            throw new BusinessException("Esse quarto já existe!");
        }

        QuartoResponseDto response = new QuartoResponseDto();
        response.setNumeroQuarto(request.getNumeroQuarto());
        response.setStatusQuarto(request.getStatusQuarto());
        response.setOcupante(String.valueOf(request.getOcupante()));

        return response;
    }

    public List<QuartoResponseDto> listarQuartos(){
        return quartoRepository.findAll()
                .stream()
                .map(quarto -> {
                    QuartoResponseDto response = new QuartoResponseDto();
                    BeanUtils.copyProperties(quarto, response, "id");
                    return response;
                }).toList();
    }


   public QuartoResponseDto buscarQuartoEspecifio(Integer numQuarto){
        QuartoEntity quarto = quartoRepository.findByNumeroQuarto(numQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado"));

            QuartoResponseDto dto = new QuartoResponseDto();
            dto.setNumeroQuarto(quarto.getNumeroQuarto());
            dto.setStatusQuarto(quarto.getStatusQuarto());
            dto.setOcupante(quarto.getOcupante() != null ? quarto.getOcupante().getNome() : "Nenhum");

            return dto;
    }

    public QuartoResponseDto atualizarQuarto(Integer numQuarto, QuartoRequestDto request){
        QuartoEntity quarto = quartoRepository.findByNumeroQuarto(numQuarto)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        BeanUtils.copyProperties(request, quarto, "id",
                "statusQuarto",
                "dataHoraEntrada",
                "dataHoraSaida",
                "ocupante");

        QuartoEntity quartoAtualizado = quartoRepository.save(quarto);


        QuartoResponseDto response = new QuartoResponseDto();
        BeanUtils.copyProperties(quartoAtualizado, response);
        return response;
    }

    public void deletar(Integer id){
        QuartoEntity quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.OCUPADO) {
            throw new BusinessException("Não é possível deletar um quarto ocupado.");
        }

        quartoRepository.delete(quarto);

    }



}


