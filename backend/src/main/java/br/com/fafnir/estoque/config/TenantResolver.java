package br.com.fafnir.estoque.config;

import br.com.fafnir.estoque.usuario.model.PapelUsuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantResolver {

    public UUID resolveTenantId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String empresaId = jwt.getClaimAsString("empresaId");

            if (empresaId != null) {
                return UUID.fromString(empresaId);
            }
        }

        return null;
    }

    public boolean resolveSuperAdmin(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String scope = jwt.getClaimAsString("scope");
            return PapelUsuario.ADMINISTRADOR.name().equals(scope);
        }

        return false;
    }
}