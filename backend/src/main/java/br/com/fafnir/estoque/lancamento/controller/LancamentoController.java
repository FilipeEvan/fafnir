package br.com.fafnir.estoque.lancamento.controller;

import br.com.fafnir.estoque.lancamento.dto.LancamentoFilterDto;
import br.com.fafnir.estoque.lancamento.dto.LancamentoRequestDto;
import br.com.fafnir.estoque.lancamento.dto.LancamentoResponseDto;
import br.com.fafnir.estoque.lancamento.service.LancamentoService;
import jakarta.validation.Valid;
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
public class LancamentoController {

    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @GetMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<Page<LancamentoResponseDto>> getAllLancamentos(LancamentoFilterDto lancamentoFilterDto,
                                                                         @PageableDefault(page = 0, size = 10, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<LancamentoResponseDto> lancamentoResponseDtos = lancamentoService.findAll(lancamentoFilterDto, pageable)
                .map(LancamentoResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(lancamentoResponseDtos);
    }

    @GetMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<LancamentoResponseDto> getOneLancamento(@PathVariable(value = "id") UUID id) {
        LancamentoResponseDto lancamentoResponseDto = LancamentoResponseDto.fromModel(lancamentoService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(lancamentoResponseDto);
    }

    @PostMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<LancamentoResponseDto> saveLancamento(@RequestBody @Valid LancamentoRequestDto lancamentoRequestDto) {
        LancamentoResponseDto lancamentoResponseDto = LancamentoResponseDto.fromModel(lancamentoService.save(lancamentoRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoResponseDto);
    }

    @PutMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<LancamentoResponseDto> updateLancamento(@PathVariable(value = "id") UUID id,
                                                                  @RequestBody @Valid LancamentoRequestDto lancamentoRequestDto) {
        LancamentoResponseDto lancamentoResponseDto = LancamentoResponseDto.fromModel(lancamentoService.update(id, lancamentoRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(lancamentoResponseDto);
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteLancamento(@PathVariable(value = "id") UUID id) {
        lancamentoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}