package br.com.fafnir.estoque.lancamento.dto;

import java.util.UUID;

public record LancamentoFilterDto(

        UUID produtoId,

        UUID dispositivoId,

        UUID usuarioId,

        String tipo,

        String notaFiscal) {
}
