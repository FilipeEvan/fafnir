package br.com.fafnir.estoque.usuario.controller;

import br.com.fafnir.estoque.usuario.dto.UsuarioFilterDto;
import br.com.fafnir.estoque.usuario.dto.UsuarioRequestDto;
import br.com.fafnir.estoque.usuario.dto.UsuarioResponseDto;
import br.com.fafnir.estoque.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações para o gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Lista todos os usuários com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    @GetMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<Page<UsuarioResponseDto>> getAllUsuarios(@ParameterObject UsuarioFilterDto usuarioFilterDto,
                                                                   @ParameterObject @PageableDefault(page = 0, size = 10, sort = "nomeCompleto") Pageable pageable) {
        Page<UsuarioResponseDto> usuarioResponseDtos = usuarioService.findAll(usuarioFilterDto, pageable)
                .map(UsuarioResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDtos);
    }

    @Operation(summary = "Busca um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content)
    })
    @GetMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<UsuarioResponseDto> getOneUsuario(@PathVariable(value = "id") UUID id) {
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.fromModel(usuarioService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDto);
    }

    @Operation(summary = "Cadastra um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado.", content = @Content)
    })
    @PostMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<UsuarioResponseDto> saveUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.fromModel(usuarioService.save(usuarioRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDto);
    }

    @Operation(summary = "Atualiza os dados do usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado.", content = @Content)
    })
    @PutMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable(value = "id") UUID id,
                                                            @RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.fromModel(usuarioService.update(id, usuarioRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDto);
    }

    @Operation(summary = "Deleta um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content)
    })
    @DeleteMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable(value = "id") UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}