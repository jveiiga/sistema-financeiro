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
        // Representa a porcentagem da taxa de imposto aplicada ao saque
        BigDecimal porcentagem = new BigDecimal("3");

        TransacaoModel transacaoModel = new TransacaoModel();
        // Defini o tipo de imposto da transação e converte a representação de string para o valor correspondente a enumeração
        transacaoModel.setTipoImposto(TipoImposto.fromString(tipoImposto));
        // Defini o valor da transação com base no valor passado como parâmetro para o método
        transacaoModel.setValor(valor);

        if ("DEPOSITO".equals(tipoImposto)) {
            realizarDeposito(idCliente, idEmpresa, valor, transacaoModel);
            transacaoModel.setStatus(StatusTransacao.CONCLUIDA);
        } else if ("SAQUE".equals(tipoImposto)) {
            BigDecimal taxaImposto = calcularTaxaImposto(valor, porcentagem);

            TransacaoModel saqueTransacaoModel = new TransacaoModel();
            saqueTransacaoModel.setTipoImposto(TipoImposto.fromString(tipoImposto));
            saqueTransacaoModel.setValor(valor);

            Optional<ClienteModel> clienteO = clienteRepository.findById(idCliente);

            if (clienteO.isPresent()) {
                ClienteModel cliente = clienteO.get();
                realizarSaque(cliente, idEmpresa, valor, saqueTransacaoModel);
                taxaImposto = descontarImposto(taxaImposto);
                transacaoModel.setMensagem("Saque realizado com sucesso!");
                transacaoModel.setStatus(StatusTransacao.CONCLUIDA);
            } else {
                transacaoModel.setMensagem("Tipo de transação não suportada");
                transacaoModel.setStatus(StatusTransacao.FALHA);
            }
        }
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

    public BigDecimal calcularTaxaImposto(BigDecimal valor, BigDecimal porcentagem) {
        // Cálcula a taxa de imposto
        BigDecimal taxa = valor.multiply(porcentagem.divide(BigDecimal.valueOf(100)));

        return taxa;
    }

    public BigDecimal descontarImposto(BigDecimal taxaImposto) {
        // Representa a porcentagem de desconto que será aplicada sobre o imposto
        BigDecimal porcentagemDesconto = new BigDecimal("0.03"); // Desconto de 3%
        BigDecimal desconto = taxaImposto.multiply(porcentagemDesconto);

        return taxaImposto.subtract(desconto);
    }

    private void realizarSaque(ClienteModel cliente, UUID idEmpresa, BigDecimal valor, TransacaoModel transacaoModel) {

        UUID idCliente = cliente.getIdCliente();
        Optional<EmpresaModel> empresaO = empresaRepository.findById(idEmpresa);

        if(empresaO.isPresent()) {
            EmpresaModel empresa = empresaO.get();
            if (empresa.getSaldo().compareTo(valor) >= 0) {
                // Representa a taxa de imposto a ser aplicada
                BigDecimal taxaImposto = new BigDecimal("3"); // Desconto de 3%
                BigDecimal taxaDesconto = calcularTaxaImposto(valor, taxaImposto);
                // Desconta a taxa de imposto da conta da empresa
                empresa.setSaldo(empresa.getSaldo().subtract(taxaDesconto));
                // Subtrai o valor total do saque (incluindo a taxa de imposto) da conta da empresa
                empresa.setSaldo(empresa.getSaldo().subtract(valor));
                // Transfere o valor para a conta do cliente
                cliente.setSaldo(cliente.getSaldo().add(valor));

                empresaRepository.save(empresa);
                clienteRepository.save(cliente);

                transacaoModel.setCliente(cliente);
                transacaoModel.setEmpresa(empresa);

                transacaoModel.setMensagem("Saque realizado com sucesso!");
                transacaoModel.setStatus(StatusTransacao.CONCLUIDA);
                transacaoRepository.save(transacaoModel);
            } else {
                transacaoModel.setMensagem("Saldo insuficiente na conta da empresa.");
                transacaoModel.setStatus(StatusTransacao.FALHA);
                transacaoRepository.save(transacaoModel);
            }
        } else {
                transacaoModel.setMensagem("Cliente ou empresa não encontrados.");
                transacaoModel.setStatus(StatusTransacao.FALHA);
                transacaoRepository.save(transacaoModel);
        }
    }

}




