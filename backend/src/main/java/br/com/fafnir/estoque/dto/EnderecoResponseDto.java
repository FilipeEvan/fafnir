package br.com.fafnir.estoque.dto;

import br.com.fafnir.estoque.model.Endereco;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record EnderecoResponseDto(

        String cep,

        String logradouro,

        String bairro,

        String numero,

        String complemento,

        String municipio,

        String uf) {

        public static EnderecoResponseDto fromModel(Endereco endereco) {
            if (endereco == null) {
                return null;
            }

            return new EnderecoResponseDto(
                    endereco.getCep(),
                    endereco.getLogradouro(),
                    endereco.getBairro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getMunicipio(),
                    endereco.getUf()
            );
        }
}
