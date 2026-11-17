package br.com.fafnir.estoque.empresa.controller;

import br.com.fafnir.estoque.empresa.dto.EmpresaFilterDto;
import br.com.fafnir.estoque.empresa.dto.EmpresaRequestDto;
import br.com.fafnir.estoque.empresa.dto.EmpresaResponseDto;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
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
@RequestMapping("/empresas")
@PreAuthorize("hasAuthority('SCOPE_ADMINISTRADOR')")
@Tag(name = "Empresas", description = "Operações para o gerenciamento de empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @Operation(summary = "Lista todas as empresas com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    @GetMapping(version = "v1")
    public ResponseEntity<Page<EmpresaResponseDto>> getAllEmpresas(@ParameterObject EmpresaFilterDto empresaFilterDto,
                                                                   @ParameterObject @PageableDefault(page = 0, size = 10, sort = "nomeFantasia") Pageable pageable) {
        Page<EmpresaResponseDto> empresaResponseDtos = empresaService.findAll(empresaFilterDto, pageable)
                .map(EmpresaResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDtos);
    }

    @Operation(summary = "Busca uma empresa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada.", content = @Content)
    })
    @GetMapping(value = "/{id}", version = "v1")
    public ResponseEntity<EmpresaResponseDto> getOneEmpresa(@PathVariable(value = "id") UUID id) {
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDto);
    }

    @Operation(summary = "Cadastra uma nova empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    @PostMapping(version = "v1")
    public ResponseEntity<EmpresaResponseDto> saveEmpresa(@RequestBody @Valid EmpresaRequestDto empresaRequestDto) {
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaService.save(empresaRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaResponseDto);
    }

    @Operation(summary = "Atualiza os dados da empresa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada.", content = @Content)
    })
    @PutMapping(value = "/{id}", version = "v1")
    public ResponseEntity<EmpresaResponseDto> updateEmpresa(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid EmpresaRequestDto empresaRequestDto) {
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaService.update(id, empresaRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDto);
    }

    @Operation(summary = "Deleta uma empresa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada.", content = @Content)
    })
    @DeleteMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable(value = "id") UUID id) {
        empresaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}