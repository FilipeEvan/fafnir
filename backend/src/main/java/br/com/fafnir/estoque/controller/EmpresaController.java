package br.com.fafnir.estoque.controller;

import br.com.fafnir.estoque.dto.EmpresaRecordDto;
import br.com.fafnir.estoque.model.EmpresaModel;
import br.com.fafnir.estoque.repository.EmpresaRepository;
import br.com.fafnir.estoque.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaRepository empresaRepository;

    public EmpresaController(EmpresaService empresaService, EmpresaRepository empresaRepository) {
        this.empresaService = empresaService;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping
    public ResponseEntity<EmpresaModel> saveEmpresa(@RequestBody @Valid EmpresaRecordDto empresaRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.save(empresaRecordDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmpresa(@PathVariable(value = "id")UUID id,
                                                @RequestBody @Valid EmpresaRecordDto empresaRecordDto) {
        return empresaService.findById(id)
                .map(empresa ->
                        ResponseEntity.status(HttpStatus.OK).body((Object) empresaService.update(empresa, empresaRecordDto))
                ).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada."));

//        Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
//        if (empresaModelOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada.");
//        }
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(empresaService.update(empresaModelOptional.get(), empresaRecordDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmpresa(@PathVariable(value = "id")UUID id) {
        return empresaService.findById(id)
                .map(empresa -> {
                    empresaService.delete(empresa);
                    return ResponseEntity.status(HttpStatus.OK).body((Object) "Empresa excluída com sucesso.");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada."));

//        Optional<EmpresaModel> empresaModelOptional = empresaService.findById(id);
//        if (empresaModelOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada.");
//        }
//        empresaService.delete(empresaModelOptional.get());
//        return ResponseEntity.status(HttpStatus.OK).body("Empresa excluída com sucesso.");
    }
}
