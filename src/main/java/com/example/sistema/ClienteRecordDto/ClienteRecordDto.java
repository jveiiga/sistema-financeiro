package com.example.sistema.ClienteRecordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ClienteRecordDto(@NotBlank String nome, @NotBlank String cpf, @NotNull BigDecimal saldo) {
}
