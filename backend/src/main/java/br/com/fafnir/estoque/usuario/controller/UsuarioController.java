package br.com.fafnir.estoque.usuario.controller;

import br.com.fafnir.estoque.usuario.dto.UsuarioFilterDto;
import br.com.fafnir.estoque.usuario.dto.UsuarioRequestDto;
import br.com.fafnir.estoque.usuario.dto.UsuarioResponseDto;
import br.com.fafnir.estoque.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(version = "v1")
    public ResponseEntity<Page<UsuarioResponseDto>> getAllUsuarios(UsuarioFilterDto usuarioFilterDto,
                                                                   @PageableDefault(page = 0, size = 10, sort = "nomeCompleto") Pageable pageable) {
        Page<UsuarioResponseDto> usuarioResponseDtos = usuarioService.findAll(usuarioFilterDto, pageable)
                .map(UsuarioResponseDto::fromModel);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDtos);
    }

    @GetMapping(value = "/{id}", version = "v1")
    public ResponseEntity<UsuarioResponseDto> getOneUsuario(@PathVariable(value = "id") UUID id) {
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.fromModel(usuarioService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDto);
    }

    @PostMapping(version = "v1")
    public ResponseEntity<UsuarioResponseDto> saveUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.fromModel(usuarioService.save(usuarioRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDto);
    }

    @PutMapping(value = "/{id}", version = "v1")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable(value = "id") UUID id,
                                                            @RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.fromModel(usuarioService.update(id, usuarioRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDto);
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Void> deleteUsuario(@PathVariable(value = "id") UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
