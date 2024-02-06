package com.example.sistema.ClienteRecordDto;

import com.example.sistema.enums.StatusTransacao;
import com.example.sistema.enums.TipoImposto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransacaoRecordDto(@NotNull UUID idCliente, @NotNull UUID idEmpresa, @NotNull TipoImposto tipoImposto, @NotBlank BigDecimal valor, @NotNull StatusTransacao status, @NotNull String mensagem) {
}
