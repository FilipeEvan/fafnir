package br.com.fafnir.estoque.produto.dto;

import br.com.fafnir.estoque.produto.model.ProdutoModel;
import br.com.fafnir.estoque.produto.model.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoRequestDto(

        @NotNull(message = "A empresa é obrigatória.")
        UUID empresaId,

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @Size(max = 100, message = "A marca deve ter no máximo 100 caracteres.")
        String marca,

        @NotNull(message = "O peso é obrigatório.")
        @Positive(message = "O peso deve ser maior que zero.")
        BigDecimal peso,

        @NotNull(message = "A unidade de medida é obrigatória.")
        UnidadeMedida unidadeMedida) {

        public ProdutoModel toModel(){
            var produtoModel = new ProdutoModel();
            BeanUtils.copyProperties(this, produtoModel);
            return produtoModel;
        }
}
