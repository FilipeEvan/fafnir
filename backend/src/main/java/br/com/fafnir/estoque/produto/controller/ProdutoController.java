package br.com.fafnir.estoque.produto.controller;

import br.com.fafnir.estoque.produto.dto.ProdutoFilterDto;
import br.com.fafnir.estoque.produto.dto.ProdutoRequestDto;
import br.com.fafnir.estoque.produto.dto.ProdutoResponseDto;
import br.com.fafnir.estoque.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping(version = "v1")
    public ResponseEntity<Page<ProdutoResponseDto>> getAllProdutos(ProdutoFilterDto produtoFilterDto,
                                                                   @PageableDefault(page = 0, size = 10, sort = "nome") Pageable pageable) {
        Page<ProdutoResponseDto> produtoResponseDtos = produtoService.findAll(produtoFilterDto, pageable)
                .map(ProdutoResponseDto::fromModel);

        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDtos);
    }

    @GetMapping(value = "/{id}", version = "v1")
    public ResponseEntity<ProdutoResponseDto> getOneProduto(@PathVariable(value = "id") UUID id) {
        ProdutoResponseDto produtoResponseDto = ProdutoResponseDto.fromModel(produtoService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDto);
    }

    @PostMapping(version = "v1")
    public ResponseEntity<ProdutoResponseDto> saveProduto(@RequestBody @Valid ProdutoRequestDto produtoRequestDto) {
        ProdutoResponseDto produtoResponseDto = ProdutoResponseDto.fromModel(produtoService.save(produtoRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponseDto);
    }

    @PutMapping(value = "/{id}", version = "v1")
    public ResponseEntity<ProdutoResponseDto> updateProduto(@PathVariable(value = "id") UUID id,
                                                            @RequestBody @Valid ProdutoRequestDto produtoRequestDto) {
        ProdutoResponseDto produtoResponseDto = ProdutoResponseDto.fromModel(produtoService.update(id, produtoRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDto);
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Void> deleteProduto(@PathVariable(value = "id") UUID id) {
        produtoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
