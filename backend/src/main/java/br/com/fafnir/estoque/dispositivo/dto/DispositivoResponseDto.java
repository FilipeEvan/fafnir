package br.com.fafnir.estoque.dispositivo.dto;

import br.com.fafnir.estoque.dispositivo.model.DispositivoModel;
import br.com.fafnir.estoque.produto.dto.ProdutoResponseDto;
import br.com.fafnir.estoque.produto.model.ProdutoModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record DispositivoResponseDto(

        UUID id,

        UUID produtoId,

        String status,

        String identificador,

        BigDecimal pesoMaximo,

        Integer quantidade,

        BigDecimal peso,

        LocalDateTime criadoEm,

        LocalDateTime atualizadoEm) {

        public static DispositivoResponseDto fromModel(DispositivoModel dispositivoModel){
            if (dispositivoModel == null) {
                return null;
            }

            return new DispositivoResponseDto(
                    dispositivoModel.getId(),
                    dispositivoModel.getProduto() != null ? dispositivoModel.getProduto().getId() : null,
                    dispositivoModel.getStatus().name(),
                    dispositivoModel.getIdentificador(),
                    dispositivoModel.getPesoMaximo(),
                    dispositivoModel.getQuantidade(),
                    dispositivoModel.getPeso(),
                    dispositivoModel.getCriadoEm(),
                    dispositivoModel.getAtualizadoEm()
            );
        }
}
