package com.example.sistema.models;

import com.example.sistema.enums.TipoImposto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "EMPRESAS")
public class EmpresaModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idEmpresa;

    @Size(max = 40)
    @NotNull
    private String nome;

    @Size(max = 14)
    @NotNull
    @Column(unique = true)
    private String cnpj;

    @NotNull
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    private TipoImposto tipoImposto;

    public UUID getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(UUID idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoImposto getTipoImposto() {
        return tipoImposto;
    }

    public void setTipoImposto(TipoImposto tipoImposto) {
        this.tipoImposto = tipoImposto;
    }
}

