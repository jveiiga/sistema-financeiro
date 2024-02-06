package com.example.sistema.controllers;

import com.example.sistema.ClienteRecordDto.EmpresaRecordDTO;
import com.example.sistema.models.EmpresaModel;
import com.example.sistema.repositories.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpresaController {

    @Autowired
    EmpresaRepository empresaRepository;

    @PostMapping("/empresas")
    public ResponseEntity<EmpresaModel> salvandoEmpresa(@RequestBody @Valid EmpresaRecordDTO empresaRecordDTO) {
        var novaEmpresa = new EmpresaModel();
        BeanUtils.copyProperties(empresaRecordDTO, novaEmpresa);
        return ResponseEntity.status(HttpStatus.OK).body(empresaRepository.save(novaEmpresa));
    }
}
