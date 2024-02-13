package com.example.sistema.controllers;

import com.example.sistema.ClienteRecordDto.TransacaoRecordDto;
import com.example.sistema.enums.StatusTransacao;
import com.example.sistema.enums.TipoImposto;
import com.example.sistema.models.ClienteModel;
import com.example.sistema.models.EmpresaModel;
import com.example.sistema.models.TransacaoModel;
import com.example.sistema.repositories.ClienteRepository;
import com.example.sistema.repositories.EmpresaRepository;
import com.example.sistema.repositories.TransacaoRepository;
import com.example.sistema.services.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/transacao")
    public ResponseEntity<TransacaoModel> realizarTransacao(@RequestBody @Valid TransacaoRecordDto transacaoRecordDto) {
        TransacaoModel transacaoModel = transacaoService.processarTransacao(transacaoRecordDto);
        return ResponseEntity.ok(transacaoModel);
    }
}




