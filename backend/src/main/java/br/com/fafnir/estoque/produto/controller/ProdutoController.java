package br.com.fafnir.estoque.produto.controller;

import br.com.fafnir.estoque.produto.dto.ProdutoFilterDto;
import br.com.fafnir.estoque.produto.dto.ProdutoRequestDto;
import br.com.fafnir.estoque.produto.dto.ProdutoResponseDto;
import br.com.fafnir.estoque.produto.service.ProdutoService;
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
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Operações para o gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Lista todos os produtos com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    @GetMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<Page<ProdutoResponseDto>> getAllProdutos(@ParameterObject ProdutoFilterDto produtoFilterDto,
                                                                   @ParameterObject @PageableDefault(page = 0, size = 10, sort = "nome") Pageable pageable) {
        Page<ProdutoResponseDto> produtoResponseDtos = produtoService.findAll(produtoFilterDto, pageable)
                .map(ProdutoResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDtos);
    }

    @Operation(summary = "Busca um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.", content = @Content)
    })
    @GetMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<ProdutoResponseDto> getOneProduto(@PathVariable(value = "id") UUID id) {
        ProdutoResponseDto produtoResponseDto = ProdutoResponseDto.fromModel(produtoService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDto);
    }

    @Operation(summary = "Cadastra um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Produto já cadastrado.", content = @Content)
    })
    @PostMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<ProdutoResponseDto> saveProduto(@RequestBody @Valid ProdutoRequestDto produtoRequestDto) {
        ProdutoResponseDto produtoResponseDto = ProdutoResponseDto.fromModel(produtoService.save(produtoRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponseDto);
    }

    @Operation(summary = "Atualiza os dados do produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato da requisição inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Produto já cadastrado.", content = @Content)
    })
    @PutMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<ProdutoResponseDto> updateProduto(@PathVariable(value = "id") UUID id,
                                                            @RequestBody @Valid ProdutoRequestDto produtoRequestDto) {
        ProdutoResponseDto produtoResponseDto = ProdutoResponseDto.fromModel(produtoService.update(id, produtoRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDto);
    }

    @Operation(summary = "Deleta um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado.", content = @Content)
    })
    @DeleteMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteProduto(@PathVariable(value = "id") UUID id) {
        produtoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}