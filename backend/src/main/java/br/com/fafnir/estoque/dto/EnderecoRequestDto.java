package br.com.fafnir.estoque.dto;

import br.com.fafnir.estoque.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

public record EnderecoRequestDto(

        @NotBlank(message = "O CEP é obrigatório.")
        @Size(min = 8, max = 8, message = "O CEP deve conter 8 dígitos.")
        String cep,

        @NotBlank(message = "O logradouro é obrigatório.")
        String logradouro,

        @NotBlank(message = "O bairro é obrigatório.")
        String bairro,

        @NotBlank(message = "O número é obrigatório.")
        String numero,

        String complemento,

        @NotBlank(message = "O município é obrigatório.")
        String municipio,

        @NotBlank(message = "A UF é obrigatória.")
        String uf) {

        public Endereco toModel(){
                var endereco = new Endereco();
                BeanUtils.copyProperties(this, endereco);
                return endereco;
        }
}
