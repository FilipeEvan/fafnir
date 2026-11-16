package br.com.fafnir.estoque.usuario.specification;

import br.com.fafnir.estoque.config.TenantContext;
import br.com.fafnir.estoque.usuario.dto.UsuarioFilterDto;
import br.com.fafnir.estoque.usuario.model.PapelUsuario;
import br.com.fafnir.estoque.usuario.model.SituacaoUsuario;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    private UsuarioSpecification() {
    }

    public static Specification<UsuarioModel> getSpecification(UsuarioFilterDto usuarioFilterDto) {
        Specification<UsuarioModel> specification = Specification.<UsuarioModel>unrestricted()
                .and(tenantEquals());

        if (usuarioFilterDto != null) {
            specification = specification
                    .and(papelEquals(usuarioFilterDto.papel()))
                    .and(situacaoEquals(usuarioFilterDto.situacao()))
                    .and(nomeCompletoContains(usuarioFilterDto.nomeCompleto()))
                    .and(emailContains(usuarioFilterDto.email()));
        }

        return specification;
    }

    private static Specification<UsuarioModel> tenantEquals() {
        return (root, query, cb) -> {
            if (TenantContext.isSuperAdmin()) {
                return null;
            }
            return cb.equal(root.get("empresa").get("id"), TenantContext.getTenantId());
        };
    }

    private static  Specification<UsuarioModel> papelEquals(String papel) {
        return (root, query, cb) -> {
            if (papel == null || papel.isBlank()) {
                return null;
            }
            try {
                PapelUsuario papelUsuario = PapelUsuario.valueOf(papel.toUpperCase());
                return cb.equal(root.get("papel"), papelUsuario);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
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
