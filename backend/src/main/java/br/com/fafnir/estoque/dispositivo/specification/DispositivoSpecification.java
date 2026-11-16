package br.com.fafnir.estoque.dispositivo.specification;

import br.com.fafnir.estoque.config.TenantContext;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoFilterDto;
import br.com.fafnir.estoque.dispositivo.model.DispositivoModel;
import br.com.fafnir.estoque.dispositivo.model.StatusDispositivo;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class DispositivoSpecification {

    public DispositivoSpecification() {
    }

    public static Specification<DispositivoModel> getSpecification(DispositivoFilterDto dispositivoFilterDto) {
        Specification<DispositivoModel> specification = Specification.<DispositivoModel>unrestricted()
                .and(tenantEquals());

        if (dispositivoFilterDto != null) {
            specification = specification
                    .and(statusEquals(dispositivoFilterDto.status()))
                    .and(identificadorContains(dispositivoFilterDto.identificador()))
                    .and(produtoIdEquals(dispositivoFilterDto.produtoId()));
        }

        return specification;
    }

    private static Specification<DispositivoModel> tenantEquals() {
        return (root, query, cb) -> {
            if (TenantContext.isSuperAdmin()) {
                return null;
            }
            return cb.equal(root.get("empresa").get("id"), TenantContext.getTenantId());
        };
    }

    private static Specification<DispositivoModel> statusEquals(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) {
                return null;
            }
            try {
                return cb.equal(root.get("status"), StatusDispositivo.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    private static Specification<DispositivoModel> identificadorContains(String identificador) {
        return (root, query, cb) -> {
            if (identificador == null || identificador.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("identificador")), "%" + identificador.toLowerCase() + "%");
        };
    }

    private static Specification<DispositivoModel> produtoIdEquals(UUID produtoId) {
        return (root, query, cb) -> {
            if (produtoId == null) {
                return null;
            }
            return cb.equal(root.get("produto").get("id"), produtoId);
        };
    }
}
