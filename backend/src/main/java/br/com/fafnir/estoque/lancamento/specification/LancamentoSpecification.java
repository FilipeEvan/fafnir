package br.com.fafnir.estoque.lancamento.specification;

import br.com.fafnir.estoque.lancamento.dto.LancamentoFilterDto;
import br.com.fafnir.estoque.lancamento.model.LancamentoModel;
import br.com.fafnir.estoque.lancamento.model.TipoLancamento;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class LancamentoSpecification {

    public LancamentoSpecification() {
    }

    public static Specification<LancamentoModel> getSpecification(LancamentoFilterDto lancamentoFilterDto) {
        if (lancamentoFilterDto == null) {
            return Specification.unrestricted();
        }
        return Specification.<LancamentoModel>unrestricted()
                .and(produtoIdEquals(lancamentoFilterDto.produtoId()))
                .and(dispositivoIdEquals(lancamentoFilterDto.dispositivoId()))
                .and(usuarioIdEquals(lancamentoFilterDto.usuarioId()))
                .and(tipoEquals(lancamentoFilterDto.tipo()))
                .and(notaFiscalContains(lancamentoFilterDto.notaFiscal()));
    }

    private static Specification<LancamentoModel> produtoIdEquals(UUID produtoId) {
        return (root, query, cb) -> {
            if (produtoId == null) {
                return null;
            }
            return cb.equal(root.get("produto").get("id"), produtoId);
        };
    }

    private static Specification<LancamentoModel> dispositivoIdEquals(UUID dispositivoId) {
        return (root, query, cb) -> {
            if (dispositivoId == null) {
                return null;
            }
            return cb.equal(root.get("dispositivo").get("id"), dispositivoId);
        };
    }

    private static Specification<LancamentoModel> usuarioIdEquals(UUID usuarioId) {
        return (root, query, cb) -> {
            if (usuarioId == null) {
                return null;
            }
            return cb.equal(root.get("usuario").get("id"), usuarioId);
        };
    }

    private static Specification<LancamentoModel> tipoEquals(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null || tipo.isBlank()) {
                return null;
            }
            try {
                return cb.equal(root.get("tipo"), TipoLancamento.valueOf(tipo.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    private static Specification<LancamentoModel> notaFiscalContains(String notaFiscal) {
        return (root, query, cb) -> {
            if (notaFiscal == null || notaFiscal.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("notaFiscal")), "%" + notaFiscal.toLowerCase() + "%");
        };
    }
}
