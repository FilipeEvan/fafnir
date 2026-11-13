package br.com.fafnir.estoque.usuario.dto;

import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public record UsuarioRequestDto(

        @NotNull(message = "A empresa é obrigatória.")
        UUID empresaId,

        @NotBlank(message = "O nome completo é obrigatório.")
        @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres.")
        String nomeCompleto,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail deve estar em um formato válido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 12, message = "A senha deve ter no mínimo  12 caracteres.")
        String senha) {

        public UsuarioModel toModel(){
            var usuarioModel = new UsuarioModel();
            BeanUtils.copyProperties(this, usuarioModel);
            return usuarioModel;
        }
}
