package com.example.sistema.controllers;

import com.example.sistema.ClienteRecordDto.TransacaoRecordDto;
import com.example.sistema.models.ClienteModel;
import com.example.sistema.models.TransacaoModel;
import com.example.sistema.repositories.ClienteRepository;
import com.example.sistema.repositories.EmpresaRepository;
import com.example.sistema.repositories.TransacaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TransacaoController {

    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @PostMapping("/transacao")
    public ResponseEntity<TransacaoModel> realizarTransacao(@RequestBody TransacaoRecordDto transacaoRecordDto) {
        TransacaoModel transacaoModel = processarTransacao(transacaoRecordDto);
        return ResponseEntity.ok(transacaoModel)
    }

    private TransacaoModel processarTransacao(TransacaoRecordDto transacaoRecordDto) {
        String TipoImposto = transacaoRecordDto.getTipoImposto();
        BigDecimal valor = transacaoRecordDto.getValor();
        String idCliente = transacaoRecordDto.getIdCliente();

        TransacaoModel transacaoModel = new TransacaoModel();

        if ("DEPOSITO".equals(tipoImposto)) {
            realizarDeposito(valor, idCliente);

            transacaoModel.setMensagem("Depósito realizado com sucesso!");
            transacaoModel.setStatus("SUCESSO");
        } else if ("SAQUE".equals(tipoImposto)) {
            BigDecimal taxaImposto = calcularTaxaImposto(valor);
            realizarSaque(valor + taxaImposto, idCliente);
            descontarImposto(taxaImposto);
            transacaoModel.setMensagem("Saque realizado com sucesso!");
            transacaoModel.setStatus("SUCESSO");
        } else {
            transacaoModel.setMensagem("Tipo de transação não suportada");
            transacaoModel.setStatus("ERRO");
        }
        return transacaoModel
    }

    private void realizarDeposito(BigDecimal valor) {

    }

    private void realizarSaque(BigDecimal valor, String idCliente) {

    }

}



