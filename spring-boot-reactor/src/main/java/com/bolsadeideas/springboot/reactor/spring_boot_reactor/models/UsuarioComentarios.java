package com.bolsadeideas.springboot.reactor.spring_boot_reactor.models;

public class UsuarioComentarios {

    private Usuario usuario;

    private Comentarios comentarios;

    public UsuarioComentarios(Usuario usuario, Comentarios comentarios) {
        this.usuario = usuario;
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "usuario=" + usuario + ", comentarios=" + comentarios;
    }
}
