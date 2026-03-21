package com.projeto.usuario.infraestructure.exceptions;

public class ConflitException extends RuntimeException {
    public ConflitException(String emnsagem) {
        super(emnsagem);
    }

    public ConflitException(String mensagem, Throwable causa) {
        super(mensagem);
    }
}
