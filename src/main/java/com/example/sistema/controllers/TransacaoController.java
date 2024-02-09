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
    TransacaoRepository transacaoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @PostMapping("/transacao")
    public ResponseEntity<TransacaoModel> realizarTransacao(@RequestBody TransacaoRecordDto transacaoRecordDto) {
        TransacaoModel transacaoModel = processarTransacao(transacaoRecordDto);
        return ResponseEntity.ok(transacaoModel);
    }

    private TransacaoModel processarTransacao(TransacaoRecordDto transacaoRecordDto) {
        String tipoImposto = transacaoRecordDto.getTipoImposto();
        BigDecimal valor = transacaoRecordDto.getValor();
        UUID idCliente = transacaoRecordDto.idCliente();
        UUID idEmpresa = transacaoRecordDto.idEmpresa();

        TransacaoModel transacaoModel = new TransacaoModel();
        transacaoModel.setTipoImposto(TipoImposto.fromString(tipoImposto));
        transacaoModel.setValor(valor);

        if ("DEPOSITO".equals(tipoImposto)) {
            realizarDeposito(idCliente, idEmpresa, valor, transacaoModel);

//            transacaoModel.setMensagem("Depósito realizado com sucesso!");
            transacaoModel.setStatus(StatusTransacao.CONCLUIDA);
        } else if ("SAQUE".equals(tipoImposto)) {
            BigDecimal taxaImposto = calcularTaxaImposto(valor);
            realizarSaque(valor.add(taxaImposto), idCliente);
            descontarImposto(taxaImposto);
            transacaoModel.setMensagem("Saque realizado com sucesso!");
            transacaoModel.setStatus(StatusTransacao.CONCLUIDA);
        } else {
            transacaoModel.setMensagem("Tipo de transação não suportada");
            transacaoModel.setStatus(StatusTransacao.FALHA);
        }
        transacaoRepository.save(transacaoModel);

        return transacaoModel;
    }

    private void realizarDeposito(UUID idCliente, UUID idEmpresa, BigDecimal valor, TransacaoModel transacaoModel) {
        Optional<ClienteModel> clienteO = clienteRepository.findById(idCliente);
        Optional<EmpresaModel> empresaO = empresaRepository.findById(idEmpresa);

        if(clienteO.isPresent() && clienteO.get().getSaldo().compareTo(valor) >= 0) {
            ClienteModel cliente = clienteO.get();
            cliente.setSaldo(cliente.getSaldo().subtract(valor));

            if (empresaO.isPresent()) {
                EmpresaModel empresa = empresaO.get();
                empresa.setSaldo(empresa.getSaldo().add(valor));
                clienteRepository.save(cliente);
                empresaRepository.save(empresa);
                transacaoRepository.save(transacaoModel);

                transacaoModel.setCliente(cliente);
                transacaoModel.setEmpresa(empresa);
                transacaoModel.setMensagem("Depósito realizado com sucesso!");
            } else {
                transacaoModel.setMensagem("Empresa não encontrada.");
                System.out.println("Empresa não encontrada.");
            }
        } else {
                transacaoModel.setMensagem("Saldo insuficiente para realizar o depósito.");
                System.out.println("Saldo insuficiente para realizar o depósito.");
        }
    }

    public BigDecimal calcularTaxaImposto(BigDecimal valor) {
        // Implementação do cálculo da taxa de imposto
        // Substitua isso com a lógica real do seu cálculo de imposto
        BigDecimal taxa = valor.multiply(new BigDecimal("0.1")); // Exemplo de uma taxa de 10%

        return taxa;
    }

    public void descontarImposto(BigDecimal taxaImposto) {
        // Lógica para descontar o imposto
        // Substitua isso com a lógica real do seu desconto de imposto
        BigDecimal porcentagemDesconto = new BigDecimal("0.05"); // Exemplo de desconto de 5%
        BigDecimal desconto = taxaImposto.multiply(porcentagemDesconto);

        // Subtrai o desconto
        taxaImposto = taxaImposto.subtract(desconto);
    }

    private void realizarSaque(BigDecimal valor, UUID idCliente) {

    }


}




