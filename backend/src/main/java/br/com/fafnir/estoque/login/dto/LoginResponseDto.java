package br.com.fafnir.estoque.login.dto;

import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Duration;

public record LoginResponseDto(

        String accessToken,

        Long expiresIn) {

        public static LoginResponseDto fromJwt(Jwt jwt){
            if (jwt == null) {
                return null;
            }

            Long expiresIn = Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).toSeconds();

            return new LoginResponseDto(
                    jwt.getTokenValue(),
                    expiresIn
            );
        }
}
