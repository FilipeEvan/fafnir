package br.com.fafnir.estoque.dispositivo.dto;

import java.util.UUID;

public record DispositivoFilterDto(

        String status,

        String identificador,

        UUID produtoId
) {
}
