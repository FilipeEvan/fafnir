package br.com.fafnir.estoque.login.service;

import br.com.fafnir.estoque.login.dto.LoginRequestDto;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import br.com.fafnir.estoque.usuario.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class LoginService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtEncoder jwtEncoder;

    public LoginService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional(readOnly = true)
    public Jwt login(LoginRequestDto loginRequestDto) {
        Optional<UsuarioModel> usuarioModelOptional = usuarioRepository.findByEmailIgnoreCase(loginRequestDto.email());

        if (usuarioModelOptional.isEmpty() || !passwordEncoder.matches(loginRequestDto.senha(), usuarioModelOptional.get().getSenha())) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }

        Instant now = Instant.now();
//        Long expiresIn = 300L;
        Long expiresIn = 600L;
        String scope = usuarioModelOptional.get().getPapel().toString();
        String empresaId = usuarioModelOptional.get().getEmpresa().getId().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("fafnir")
                .subject(usuarioModelOptional.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scope)
                .claim("empresaId", empresaId)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}
