package com.example.sistema.enums;

public enum TipoImposto {
    DEPOSITO, SAQUE;

    public static TipoImposto fromString(String tipo) {
        switch (tipo.toUpperCase()) {
            case "DEPOSITO":
                return DEPOSITO;
            case "SAQUE":
                return SAQUE;
            default:
                throw new IllegalArgumentException("Tipo de imposto não reconhecido: " + tipo);
        }
    }
}
