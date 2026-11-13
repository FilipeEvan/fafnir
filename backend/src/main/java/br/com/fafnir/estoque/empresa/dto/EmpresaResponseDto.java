package br.com.fafnir.estoque.empresa.dto;

import br.com.fafnir.estoque.empresa.model.EmpresaModel;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmpresaResponseDto(

        UUID id,

        String situacao,

        String documento,

        String razaoSocial,

        String nomeFantasia,

        String telefone,

        EnderecoResponseDto endereco,

        LocalDateTime criadoEm,

        LocalDateTime atualizadoEm) {

        public static EmpresaResponseDto fromModel(EmpresaModel empresaModel){
            if (empresaModel == null) {
                return null;
            }

            return new EmpresaResponseDto(
                    empresaModel.getId(),
                    empresaModel.getSituacao().name(),
                    empresaModel.getDocumento(),
                    empresaModel.getRazaoSocial(),
                    empresaModel.getNomeFantasia(),
                    empresaModel.getTelefone(),
                    EnderecoResponseDto.fromModel(empresaModel.getEndereco()),
                    empresaModel.getCriadoEm(),
                    empresaModel.getAtualizadoEm()
            );
        }
}
