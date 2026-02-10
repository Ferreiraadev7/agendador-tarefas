package com.javanauta.agendadortarefas.infrastructure.excepitions;


public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String mensagem){
        super(mensagem);
    }

    public ResourceNotFound(String mensagem,Throwable throwable){
        super(mensagem, throwable);
    }
}
