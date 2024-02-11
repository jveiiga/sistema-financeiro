package com.example.sistema.controllers;

import com.example.sistema.ClienteRecordDto.EmpresaRecordDTO;
import com.example.sistema.models.EmpresaModel;
import com.example.sistema.repositories.EmpresaRepository;
import com.example.sistema.services.EmpresaService;
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
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaModel> salvandoEmpresa(@RequestBody @Valid EmpresaRecordDTO empresaRecordDTO) {
        EmpresaModel novaEmpresa = empresaService.salvandoEmpresa(empresaRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
    }
}
