package com.example.sistema.ClienteRecordDto;

import com.example.sistema.enums.TipoImposto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EmpresaRecordDTO(@NotBlank String nome, @NotBlank String cnpj, @NotNull BigDecimal saldo, TipoImposto tipoImposto) {
}
