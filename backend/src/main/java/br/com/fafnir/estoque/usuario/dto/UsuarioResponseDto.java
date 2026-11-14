package br.com.fafnir.estoque.usuario.dto;

import br.com.fafnir.estoque.usuario.model.UsuarioModel;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDto(

        UUID id,

        String situacao,

        String nomeCompleto,

        String email,

        LocalDateTime criadoEm,

        LocalDateTime atualizadoEm) {

        public static UsuarioResponseDto fromModel(UsuarioModel usuarioModel){
            if (usuarioModel == null) {
                return null;
            }

            return new UsuarioResponseDto(
                    usuarioModel.getId(),
                    usuarioModel.getSituacao().name(),
                    usuarioModel.getNomeCompleto(),
                    usuarioModel.getEmail(),
                    usuarioModel.getCriadoEm(),
                    usuarioModel.getAtualizadoEm()
            );
        }
}
