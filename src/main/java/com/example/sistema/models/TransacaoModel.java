package com.example.sistema.models;

import com.example.sistema.enums.StatusTransacao;
import com.example.sistema.enums.TipoImposto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TRANSACOES")
public class TransacaoModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTransacao;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private ClienteModel cliente;

    @ManyToOne
    @JoinColumn(name = "idEmpresa")
    private EmpresaModel empresa;

    @Enumerated(EnumType.STRING)
    private TipoImposto tipoImposto;

    @NotNull
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;
    
    private String mensagem;

    public UUID getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(UUID idTransacao) {
        this.idTransacao = idTransacao;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public TipoImposto getTipoImposto() {
        return tipoImposto;
    }

    public void setTipoImposto(TipoImposto tipoImposto) {
        this.tipoImposto = tipoImposto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
