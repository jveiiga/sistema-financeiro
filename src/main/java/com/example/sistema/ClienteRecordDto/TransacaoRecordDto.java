package com.example.sistema.ClienteRecordDto;

import com.example.sistema.enums.StatusTransacao;
import com.example.sistema.enums.TipoImposto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransacaoRecordDto(UUID idCliente, UUID idEmpresa, TipoImposto tipoImposto, BigDecimal valor, StatusTransacao status, String mensagem, BigDecimal saldoCliente, BigDecimal saldoEmpresa) {

    @Override
    public UUID idCliente() {
        return idCliente;
    }

    @Override
    public UUID idEmpresa() {
        return idEmpresa;
    }

    @Override
    public TipoImposto tipoImposto() {
        return tipoImposto;
    }

    public String getTipoImposto() {
        return tipoImposto.toString();
    }

    @Override
    public BigDecimal valor() {
        return valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldoCliente() {
        return saldoCliente;
    }

    public BigDecimal getSaldoEmpresa() {
        return saldoEmpresa;
    }

    @Override
    public StatusTransacao status() {
        return status;
    }

    @Override
    public String mensagem() {
        return mensagem;
    }
}
