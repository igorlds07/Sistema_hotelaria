package com.hotelaria.quartos.model.entity.service;

import com.hotelaria.quartos.model.entity.QuartoEntity;
import com.hotelaria.quartos.model.entity.StatusQuarto;
import com.hotelaria.quartos.model.entity.repository.QuartoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository quartoRepository;

    public void iniciarQuartos(){
        if (quartoRepository.count() == 0){
            for (int i = 0; i < 15; i++){
                QuartoEntity quarto = new QuartoEntity();
                quarto.setNumeroQuarto(i);
                quarto.setStatusQuarto(StatusQuarto.DISPONIVEL);
                quartoRepository.save(quarto);
            }
        }
    }
    public Integer ocuparQuarto(Integer numero){
        QuartoEntity  quarto = quartoRepository.findByRoom(numero)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado!"));

        if (quarto.getStatusQuarto() == StatusQuarto.OCUPADO){
            throw new RuntimeException("O quarto N°" + numero + " está ocupado!");
        }
        quarto.setStatusQuarto(StatusQuarto.OCUPADO);

        return quarto.getId();
    }
}
