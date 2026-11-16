package br.com.fafnir.estoque.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail deve ser um endereço de e-mail válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        String senha) {
}
