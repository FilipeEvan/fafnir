package br.com.fafnir.estoque.controller;

import br.com.fafnir.estoque.dto.EmpresaRequestDto;
import br.com.fafnir.estoque.dto.EmpresaResponseDto;
import br.com.fafnir.estoque.model.EmpresaModel;
import br.com.fafnir.estoque.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping(version = "v1")
    public ResponseEntity<List<EmpresaResponseDto>> getAllEmpresas() {
        List<EmpresaResponseDto> empresaResponseDtos = empresaService.findAll()
                .stream()
                .map(EmpresaResponseDto::fromModel)
                .toList();
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
        EmpresaModel empresaModel = empresaService.update(empresaService.findById(id), empresaRequestDto);
        EmpresaResponseDto empresaResponseDto = EmpresaResponseDto.fromModel(empresaModel);
        return ResponseEntity.status(HttpStatus.OK).body(empresaResponseDto);
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable(value = "id") UUID id) {
        EmpresaModel empresaModel = empresaService.findById(id);
        empresaService.delete(empresaModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
