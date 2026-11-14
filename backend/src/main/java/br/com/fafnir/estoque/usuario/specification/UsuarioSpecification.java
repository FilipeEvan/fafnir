package br.com.fafnir.estoque.usuario.specification;

import br.com.fafnir.estoque.usuario.dto.UsuarioFilterDto;
import br.com.fafnir.estoque.usuario.model.SituacaoUsuario;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    private UsuarioSpecification() {
    }

    public static Specification<UsuarioModel> getSpecification(UsuarioFilterDto usuarioFilterDto) {
        if (usuarioFilterDto == null) {
            return Specification.unrestricted();
        }
        return Specification.<UsuarioModel>unrestricted()
                .and(situacaoEquals(usuarioFilterDto.situacao()))
                .and(nomeCompletoContains((usuarioFilterDto.nomeCompleto())))
                .and(emailContains(usuarioFilterDto.email()));
    }

    private static  Specification<UsuarioModel> situacaoEquals(String situacao) {
        return (root, query, cb) -> {
            if (situacao == null || situacao.isBlank()) {
                return null;
            }
            try {
                SituacaoUsuario situacaoUsuario = SituacaoUsuario.valueOf(situacao.toUpperCase());
                return cb.equal(root.get("situacao"), situacaoUsuario);
            } catch (IllegalArgumentException e) {
                return null;
            }

        };
    }

    private static  Specification<UsuarioModel> nomeCompletoContains(String nomeCompleto) {
        return (root, query, cb) -> {
            if (nomeCompleto == null || nomeCompleto.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nomeCompleto")), "%" + nomeCompleto.toLowerCase() + "%");
        };
    }

    private static  Specification<UsuarioModel> emailContains(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }
}
