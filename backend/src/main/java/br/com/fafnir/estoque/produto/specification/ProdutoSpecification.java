package br.com.fafnir.estoque.produto.specification;

import br.com.fafnir.estoque.produto.dto.ProdutoFilterDto;
import br.com.fafnir.estoque.produto.model.ProdutoModel;
import br.com.fafnir.estoque.produto.model.UnidadeMedida;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {

    private ProdutoSpecification() {
    }

    public static Specification<ProdutoModel> getSpecification(ProdutoFilterDto produtoFilterDto) {
        if (produtoFilterDto == null) {
            return Specification.unrestricted();
        }
        return Specification.<ProdutoModel>unrestricted()
                .and(nomeContains(produtoFilterDto.nome()))
                .and(marcaContains(produtoFilterDto.marca()))
                .and(unidadeMedidaEquals(produtoFilterDto.unidadeMedida()));
    }

    private static Specification<ProdutoModel> nomeContains(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    private static Specification<ProdutoModel> marcaContains(String marca) {
        return (root, query, cb) -> {
            if (marca == null || marca.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("marca")), "%" + marca.toLowerCase() + "%");
        };
    }

    private static Specification<ProdutoModel> unidadeMedidaEquals(String unidadeMedida) {
        return (root, query, cb) -> {
            if (unidadeMedida == null || unidadeMedida.isBlank()) {
                return null;
            }
            try {
                return cb.equal(root.get("unidadeMedida"), UnidadeMedida.valueOf(unidadeMedida.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
