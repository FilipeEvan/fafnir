package br.com.fafnir.estoque.lancamento.dto;

import br.com.fafnir.estoque.lancamento.model.LancamentoModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LancamentoResponseDto(

        UUID id,

        UUID produtoId,

        UUID dispositivoId,

        UUID usuarioId,

        String tipo,

        Integer quantidade,

        String notaFiscal,

        BigDecimal pesoUnitario,

        BigDecimal pesoTotal,

        String observacao,

        LocalDateTime criadoEm) {

        public static LancamentoResponseDto fromModel(LancamentoModel lancamentoModel) {
            if (lancamentoModel == null) {
                return null;
            }

            return new LancamentoResponseDto(
                    lancamentoModel.getId(),
                    lancamentoModel.getProduto().getId(),
                    lancamentoModel.getDispositivo().getId(),
                    lancamentoModel.getUsuario() != null ? lancamentoModel.getUsuario().getId() : null,
                    lancamentoModel.getTipo().name(),
                    lancamentoModel.getQuantidade(),
                    lancamentoModel.getNotaFiscal(),
                    lancamentoModel.getPesoUnitario(),
                    lancamentoModel.getPesoTotal(),
                    lancamentoModel.getObservacao(),
                    lancamentoModel.getCriadoEm()
            );
        }
}
