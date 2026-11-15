package br.com.fafnir.estoque.usuario.dto;

public record UsuarioFilterDto(

        String papel,

        String situacao,

        String nomeCompleto,

        String email) {
}
