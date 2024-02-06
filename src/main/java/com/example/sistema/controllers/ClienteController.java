package com.example.sistema.controllers;

import com.example.sistema.ClienteRecordDto.ClienteRecordDto;
import com.example.sistema.models.ClienteModel;
import com.example.sistema.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienteController {

    @Autowired
    ClienteRepository clienteRepository;

    @PostMapping("/clientes")
    public ResponseEntity<ClienteModel> salvandoCliente(@RequestBody @Valid ClienteRecordDto clienteRecordDto) {
        var novoCliente = new ClienteModel();
        BeanUtils.copyProperties(clienteRecordDto, novoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepository.save(novoCliente));
    }
}
