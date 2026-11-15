package br.com.fafnir.estoque.lancamento.dto;

import br.com.fafnir.estoque.lancamento.model.LancamentoModel;
import br.com.fafnir.estoque.lancamento.model.TipoLancamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.UUID;

public record LancamentoRequestDto(

        @NotNull(message = "A empresa é obrigatória.")
        UUID empresaId,

        @NotNull(message = "O produto é obrigatório.")
        UUID produtoId,

        @NotNull(message = "O dispositivo é obrigatório.")
        UUID dispositivoId,

        UUID usuarioId,

        @NotNull(message = "O tipo de lançamento é obrigatório.")
        TipoLancamento tipo,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer quantidade,

        @Size(max = 44, message = "A nota fiscal deve ter no máximo 50 caracteres.")
        String notaFiscal,

        @Positive(message = "O peso unitário deve ser maior que zero.")
        BigDecimal pesoUnitario,

        @Positive(message = "O peso total deve ser maior que zero.")
        BigDecimal pesoTotal,

        @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
        String observacao) {

        public LancamentoModel toModel(){
            var lancamentoModel = new LancamentoModel();
            BeanUtils.copyProperties(this, lancamentoModel);
            return lancamentoModel;
        }
}
