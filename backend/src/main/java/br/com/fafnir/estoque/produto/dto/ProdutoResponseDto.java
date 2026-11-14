package br.com.fafnir.estoque.produto.dto;

import br.com.fafnir.estoque.produto.model.ProdutoModel;
import br.com.fafnir.estoque.usuario.dto.UsuarioResponseDto;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ProdutoResponseDto(

        UUID id,

        String nome,

        String marca,

        BigDecimal peso,

        String unidadeMedida,

        LocalDateTime criadoEm,

        LocalDateTime atualizadoEm) {

        public static ProdutoResponseDto fromModel(ProdutoModel produtoModel){
            if (produtoModel == null) {
                return null;
            }

            return new ProdutoResponseDto(
                    produtoModel.getId(),
                    produtoModel.getNome(),
                    produtoModel.getMarca(),
                    produtoModel.getPeso(),
                    produtoModel.getUnidadeMedida().name(),
                    produtoModel.getCriadoEm(),
                    produtoModel.getAtualizadoEm()
            );
        }
}
