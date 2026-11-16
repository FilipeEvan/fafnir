package br.com.fafnir.estoque.login.controller;

import br.com.fafnir.estoque.login.dto.LoginRequestDto;
import br.com.fafnir.estoque.login.dto.LoginResponseDto;
import br.com.fafnir.estoque.login.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(version = "v1")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = LoginResponseDto.fromJwt(loginService.login(loginRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }
}
