package br.com.fafnir.estoque.login.controller;

import br.com.fafnir.estoque.login.dto.LoginRequestDto;
import br.com.fafnir.estoque.login.dto.LoginResponseDto;
import br.com.fafnir.estoque.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Login", description = "Operação para autenticação de usuários")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Autentica um usuário e retorna o JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos.", content = @Content)
    })
    @PostMapping(version = "v1")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = LoginResponseDto.fromJwt(loginService.login(loginRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }
}
