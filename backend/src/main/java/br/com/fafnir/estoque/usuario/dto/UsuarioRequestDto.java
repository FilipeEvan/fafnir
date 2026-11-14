package br.com.fafnir.estoque.usuario.dto;

import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import jakarta.validation.constraints.*;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public record UsuarioRequestDto(

        @NotNull(message = "A empresa é obrigatória.")
        UUID empresaId,

        @NotBlank(message = "O nome completo é obrigatório.")
        @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres.")
        String nomeCompleto,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail deve ser um endereço de e-mail válido.")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 12, message = "A senha deve ter no mínimo 12 caracteres.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#-])[A-Za-z\\d@$!%*?&._#-]+$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial."
        )
        String senha) {

        public UsuarioModel toModel(){
            var usuarioModel = new UsuarioModel();
            BeanUtils.copyProperties(this, usuarioModel);
            return usuarioModel;
        }
}
