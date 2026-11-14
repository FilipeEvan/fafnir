package br.com.fafnir.estoque.usuario.dto;

public record UsuarioFilterDto(

        String situacao,

        String nomeCompleto,

        String email) {
}
