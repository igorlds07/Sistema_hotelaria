package com.hotelaria.clientes.model.entity.service;

import com.hotelaria.clientes.model.entity.ClienteEntity;
import com.hotelaria.clientes.model.entity.dto.ClienteRequestDto;
import com.hotelaria.clientes.model.entity.dto.ClienteResponseDto;
import com.hotelaria.clientes.model.entity.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public ClienteResponseDto criar(ClienteRequestDto request){

        // Converte a requisição Dto para uma entidade no banco de dados
        ClienteEntity cliente = new ClienteEntity();
        BeanUtils.copyProperties(request, cliente);

        // Salva no banco de dados
        ClienteEntity salvo = clienteRepository.save(cliente);

        // Converte o cliente salvo para um responseDto
        ClienteResponseDto response = new ClienteResponseDto();
        BeanUtils.copyProperties(salvo, response);

        return response;
    }

    public List<ClienteResponseDto> listar(){
            return clienteRepository.findAll()
                    .stream()
                    .map(cliente -> {
                        ClienteResponseDto response = new ClienteResponseDto();
                        BeanUtils.copyProperties(cliente, response);
                        return response;
                    }).toList();
    }

    private ClienteEntity buscarClientePorId(Integer id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }

    public ClienteResponseDto buscarPorId(Integer id){
        ClienteEntity cliente = buscarClientePorId(id);

        ClienteResponseDto response = new ClienteResponseDto();
        BeanUtils.copyProperties(cliente, response);
        return response;
    }

    public ClienteResponseDto deletar(Integer id){
        ClienteEntity cliente = buscarClientePorId(id);

        clienteRepository.delete(cliente);

        ClienteResponseDto responseDto = new ClienteResponseDto();
        BeanUtils.copyProperties(cliente, responseDto);
        return responseDto;
    }

    public ClienteResponseDto atualizar(Integer id, ClienteRequestDto request){
        ClienteEntity cliente = buscarClientePorId(id);

        BeanUtils.copyProperties(request, cliente, "id");

        ClienteEntity clienteAtualizado = clienteRepository.save(cliente);

        ClienteResponseDto responseDto = new ClienteResponseDto();
        BeanUtils.copyProperties(clienteAtualizado, responseDto);


        return responseDto;
    }
}
