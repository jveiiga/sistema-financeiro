package com.example.sistema.controllers;

import com.example.sistema.ClienteRecordDto.ClienteRecordDto;
import com.example.sistema.models.ClienteModel;
import com.example.sistema.repositories.ClienteRepository;
import com.example.sistema.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteModel> salvandoCliente(@RequestBody @Valid ClienteRecordDto clienteRecordDto) {
        ClienteModel novoCliente = clienteService.salvandoCliente(clienteRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }
}
