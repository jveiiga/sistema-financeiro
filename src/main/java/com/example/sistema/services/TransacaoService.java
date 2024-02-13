package com.example.sistema.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public TransacaoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransacaoModel realizarTransacao(@Valid TransacaoRecordDto transacaoRecordDto) {
        return processarTransacao(transacaoRecordDto);
    }

    public TransacaoModel processarTransacao(TransacaoRecordDto transacaoRecordDto) {
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
                realizarSaque(idCliente, idEmpresa, valor, saqueTransacaoModel);
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
            }
        } else {
            transacaoModel.setMensagem("Saldo insuficiente para realizar o depósito.");
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

    private void realizarSaque(UUID idCliente, UUID idEmpresa, BigDecimal valor, TransacaoModel transacaoModel) {
        ClienteModel cliente = null;

        Optional<ClienteModel> clienteO = clienteRepository.findById(idCliente);
        Optional<EmpresaModel> empresaO = empresaRepository.findById(idEmpresa);

        if (clienteO.isPresent() && empresaO.isPresent()) {
            cliente = clienteO.get();
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

                // Simulação de envio de e-mail ou SMS para o cliente usando webhook.site
                NotificacaoService notificacaoService = new NotificacaoService(restTemplate);
                notificacaoService.enviarNotificacao("Email enviado com sucesso!", "5a417874-b03f-4dfb-8368-c75df28b7f8f@email.webhook.site");

                transacaoRepository.save(transacaoModel);
                // Problemas ao passar os valores no retorno do json
                // transacaoModel.setIdTransacao();
                transacaoModel.setCliente(cliente);
                transacaoModel.setEmpresa(empresa);
                transacaoModel.setMensagem("Saque realizado com sucesso!");
                transacaoModel.setStatus(StatusTransacao.CONCLUIDA);
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
