package com.example.sistema.services;

import com.example.sistema.ClienteRecordDto.ClienteRecordDto;
import com.example.sistema.models.ClienteModel;
import com.example.sistema.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteModel salvandoCliente(@Valid ClienteRecordDto clienteRecordDto) {
        var novoCliente = new ClienteModel();
        BeanUtils.copyProperties(clienteRecordDto, novoCliente);
        return clienteRepository.save(novoCliente);
    }
}
