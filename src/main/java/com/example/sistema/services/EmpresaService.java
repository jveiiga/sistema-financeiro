package com.example.sistema.services;

import com.example.sistema.ClienteRecordDto.EmpresaRecordDTO;
import com.example.sistema.models.EmpresaModel;
import com.example.sistema.repositories.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public EmpresaModel salvandoEmpresa(@Valid EmpresaRecordDTO empresaRecordDTO) {
        var novaEmpresa = new EmpresaModel();
        BeanUtils.copyProperties(empresaRecordDTO, novaEmpresa);
        return empresaRepository.save(novaEmpresa);
    }
}
