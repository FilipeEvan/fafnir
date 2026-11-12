package br.com.fafnir.estoque.dto;

import br.com.fafnir.estoque.model.EmpresaModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

public record EmpresaRequestDto(

        @NotBlank(message = "O documento é obrigatório.")
        @Size(min = 11, max = 14, message = "O documento deve ter entre 11 e 14 caracteres.")
        @Pattern(regexp = "[a-zA-Z0-9]+", message = "O documento só pode conter apenas letras e números.")
        String documento,

        @NotBlank(message = "A razão social é obrigatória.")
        @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres.")
        String razaoSocial,

        @Size(max = 55, message = "O nome fantasia deve ter no máximo 55 caracteres.")
        String nomeFantasia,

        @NotBlank(message = "O telefone é obrigatório.")
        @Size(min = 10, max = 13, message = "O telefone deve ter entre 10 e 13 caracteres.")
        @Pattern(regexp = "\\d+", message = "O telefone só pode conter apenas números.")
        String telefone,

        @NotNull(message = "O endereço é obrigatório.")
        @Valid
        EnderecoRequestDto endereco) {

        public EmpresaModel toModel(){
                var empresaModel = new EmpresaModel();
                BeanUtils.copyProperties(this, empresaModel);
                return empresaModel;
        }
}
