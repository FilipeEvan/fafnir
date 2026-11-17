package br.com.fafnir.estoque.dispositivo.controller;

import br.com.fafnir.estoque.dispositivo.dto.DispositivoFilterDto;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoRequestDto;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoResponseDto;
import br.com.fafnir.estoque.dispositivo.service.DispositivoService;
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
@RequestMapping("/dispositivos")
@Tag(name = "Dispositivos", description = "Operações para o gerenciamento de dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @Operation(summary = "Lista todos os dispositivos com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    @GetMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<Page<DispositivoResponseDto>> getAllDispositivos(@ParameterObject DispositivoFilterDto dispositivoFilterDto,
                                                                           @ParameterObject @PageableDefault(page = 0, size = 10, sort = "identificador") Pageable pageable) {
        Page<DispositivoResponseDto> dispositivoResponseDtos = dispositivoService.findAll(dispositivoFilterDto, pageable)
                .map(DispositivoResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoResponseDtos);
    }

    @Operation(summary = "Busca um dispositivo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado.", content = @Content)
    })
    @GetMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<DispositivoResponseDto> getOneDispositivo(@PathVariable(value = "id") UUID id) {
        DispositivoResponseDto dispositivoResponseDto = DispositivoResponseDto.fromModel(dispositivoService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoResponseDto);
    }

    @Operation(summary = "Cadastra um novo dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dispositivo criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Dispositivo já cadastrado.", content = @Content)
    })
    @PostMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<DispositivoResponseDto> saveDispositivo(@RequestBody @Valid DispositivoRequestDto dispositivoRequestDto) {
        DispositivoResponseDto dispositivoResponseDto = DispositivoResponseDto.fromModel(dispositivoService.save(dispositivoRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoResponseDto);
    }

    @Operation(summary = "Atualiza os dados do dispositivo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dispositivo ou produto não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Dispositivo já cadastrado.", content = @Content)
    })
    @PutMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<DispositivoResponseDto> updateDispositivo(@PathVariable(value = "id") UUID id,
                                                                    @RequestBody @Valid DispositivoRequestDto dispositivoRequestDto) {
        DispositivoResponseDto dispositivoResponseDto = DispositivoResponseDto.fromModel(dispositivoService.update(id, dispositivoRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoResponseDto);
    }

    @Operation(summary = "Deleta um dispositivo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dispositivo deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado.", content = @Content)
    })
    @DeleteMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteDispositivo(@PathVariable(value = "id") UUID id) {
        dispositivoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}