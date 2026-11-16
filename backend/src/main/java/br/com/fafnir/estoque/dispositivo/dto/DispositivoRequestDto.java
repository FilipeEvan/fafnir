package br.com.fafnir.estoque.dispositivo.dto;

import br.com.fafnir.estoque.dispositivo.model.DispositivoModel;
import jakarta.validation.constraints.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.UUID;

public record DispositivoRequestDto(

        UUID empresaId,

        UUID produtoId,

        @NotBlank(message = "O identificador é obrigatório.")
        @Pattern(
                regexp = "^([0-9A-Fa-f]{2}:){5}([0-9A-Fa-f]{2})$",
                message = "O identificador deve ser um endereço MAC válido no formato XX:XX:XX:XX:XX:XX."
        )
        String identificador,

        @NotNull(message = "O peso máximo é obrigatório.")
        @Positive(message = "O peso máximo deve ser maior que zero.")
        BigDecimal pesoMaximo,

        @NotNull(message = "A quantidade é obrigatória.")
        @PositiveOrZero(message = "A quantidade não deve ser negativa.")
        Integer quantidade,

        @NotNull(message = "O peso é obrigatório.")
        @PositiveOrZero(message = "O peso não deve ser negativo.")
        BigDecimal peso) {

        public DispositivoModel toModel(){
            var dispositivoModel = new DispositivoModel();
            BeanUtils.copyProperties(this, dispositivoModel);
            return dispositivoModel;
        }
}
