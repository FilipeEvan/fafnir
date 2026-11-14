package br.com.fafnir.estoque.empresa.controller;

import br.com.fafnir.estoque.empresa.dto.EmpresaFilterDto;
import br.com.fafnir.estoque.empresa.dto.EmpresaRequestDto;
import br.com.fafnir.estoque.empresa.dto.EmpresaResponseDto;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping(version = "v1")
    public ResponseEntity<Page<EmpresaResponseDto>> getAllEmpresas(EmpresaFilterDto empresaFilterDto,
                                                                   @PageableDefault(page = 0, size = 10, sort = "nomeFantasia") Pageable pageable) {
        Page<EmpresaResponseDto> empresaResponseDtos = empresaService.findAll(empresaFilterDto, pageable)
                .map(EmpresaResponseDto::fromModel);

        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDtos);
    }

    @GetMapping(value = "/{id}", version = "v1")
    public ResponseEntity<EmpresaResponseDto> getOneEmpresa(@PathVariable(value = "id") UUID id) {
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaService.findById(id));

        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDto);
    }

    @PostMapping(version = "v1")
    public ResponseEntity<EmpresaResponseDto> saveEmpresa(@RequestBody @Valid EmpresaRequestDto empresaRequestDto) {
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaService.save(empresaRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(empresaResponseDto);
    }

    @PutMapping(value = "/{id}", version = "v1")
    public ResponseEntity<EmpresaResponseDto> updateEmpresa(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid EmpresaRequestDto empresaRequestDto) {
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaService.update(id, empresaRequestDto));

        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDto);
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable(value = "id") UUID id) {
        empresaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
