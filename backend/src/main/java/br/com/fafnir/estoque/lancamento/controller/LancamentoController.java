package br.com.fafnir.estoque.lancamento.controller;

import br.com.fafnir.estoque.lancamento.dto.LancamentoFilterDto;
import br.com.fafnir.estoque.lancamento.dto.LancamentoRequestDto;
import br.com.fafnir.estoque.lancamento.dto.LancamentoResponseDto;
import br.com.fafnir.estoque.lancamento.service.LancamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/lancamentos")
@Tag(name = "Lançamentos", description = "Operações para o gerenciamento de lançamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @Operation(summary = "Lista todos os lançamentos com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    @GetMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<Page<LancamentoResponseDto>> getAllLancamentos(@ParameterObject LancamentoFilterDto lancamentoFilterDto,
                                                                         @ParameterObject @PageableDefault(page = 0, size = 10, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<LancamentoResponseDto> lancamentoResponseDtos = lancamentoService.findAll(lancamentoFilterDto, pageable)
                .map(LancamentoResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(lancamentoResponseDtos);
    }

    @Operation(summary = "Busca um lançamento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamento encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lançamento não encontrado.", content = @Content)
    })
    @GetMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<LancamentoResponseDto> getOneLancamento(@PathVariable(value = "id") UUID id) {
        LancamentoResponseDto lancamentoResponseDto = LancamentoResponseDto.fromModel(lancamentoService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(lancamentoResponseDto);
    }

    @Operation(summary = "Cadastra um novo lançamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lançamento criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto, dispositivo ou usuário não encontrado.", content = @Content)
    })
    @PostMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<LancamentoResponseDto> saveLancamento(@RequestBody @Valid LancamentoRequestDto lancamentoRequestDto) {
        LancamentoResponseDto lancamentoResponseDto = LancamentoResponseDto.fromModel(lancamentoService.save(lancamentoRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoResponseDto);
    }

    @Operation(summary = "Atualiza os dados do lançamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamento atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lançamento, produto, dispositivo ou usuário não encontrado.", content = @Content)
    })
    @PutMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<LancamentoResponseDto> updateLancamento(@PathVariable(value = "id") UUID id,
                                                                  @RequestBody @Valid LancamentoRequestDto lancamentoRequestDto) {
        LancamentoResponseDto lancamentoResponseDto = LancamentoResponseDto.fromModel(lancamentoService.update(id, lancamentoRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(lancamentoResponseDto);
    }

    @Operation(summary = "Deleta um lançamento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lançamento deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lançamento não encontrado.", content = @Content)
    })
    @DeleteMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteLancamento(@PathVariable(value = "id") UUID id) {
        lancamentoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}