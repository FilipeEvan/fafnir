package br.com.fafnir.estoque.empresa.specification;

import br.com.fafnir.estoque.empresa.dto.EmpresaFilterDto;
import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.model.SituacaoEmpresa;
import org.springframework.data.jpa.domain.Specification;

public class EmpresaSpecification {

    private EmpresaSpecification() {
    }

    public static Specification<EmpresaModel> getSpecification(EmpresaFilterDto empresaFilterDto) {
        if (empresaFilterDto == null) {
            return Specification.unrestricted();
        }
        return Specification.<EmpresaModel>unrestricted()
                .and(situacaoEquals(empresaFilterDto.situacao()))
                .and(razaoSocialContains((empresaFilterDto.razaoSocial())))
                .and(nomeFantasiaContains(empresaFilterDto.nomeFantasia()));
    }

    private static  Specification<EmpresaModel> situacaoEquals(String situacao) {
        return (root, query, cb) -> {
            if (situacao == null || situacao.isBlank()) {
                return null;
            }
            try {
                SituacaoEmpresa situacaoEmpresa = SituacaoEmpresa.valueOf(situacao.toUpperCase());
                return cb.equal(root.get("situacao"), situacaoEmpresa);
            } catch (IllegalArgumentException e) {
                return null;
            }

        };
    }

    private static  Specification<EmpresaModel> razaoSocialContains(String razaoSocial) {
        return (root, query, cb) -> {
            if (razaoSocial == null || razaoSocial.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("razaoSocial")), "%" + razaoSocial.toLowerCase() + "%");
        };
    }

    private static  Specification<EmpresaModel> nomeFantasiaContains(String nomeFantasia) {
        return (root, query, cb) -> {
            if (nomeFantasia == null || nomeFantasia.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nomeFantasia")), "%" + nomeFantasia.toLowerCase() + "%");
        };
    }
}
