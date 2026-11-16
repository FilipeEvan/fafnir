package br.com.fafnir.estoque.dispositivo.controller;

import br.com.fafnir.estoque.dispositivo.dto.DispositivoFilterDto;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoRequestDto;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoResponseDto;
import br.com.fafnir.estoque.dispositivo.service.DispositivoService;
import jakarta.validation.Valid;
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
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @GetMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<Page<DispositivoResponseDto>> getAllDispositivos(DispositivoFilterDto dispositivoFilterDto,
                                                                           @PageableDefault(page = 0, size = 10, sort = "identificador") Pageable pageable) {
        Page<DispositivoResponseDto> dispositivoResponseDtos = dispositivoService.findAll(dispositivoFilterDto, pageable)
                .map(DispositivoResponseDto::fromModel);
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoResponseDtos);
    }

    @GetMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR', 'SCOPE_LEITOR')")
    public ResponseEntity<DispositivoResponseDto> getOneDispositivo(@PathVariable(value = "id") UUID id) {
        DispositivoResponseDto dispositivoResponseDto = DispositivoResponseDto.fromModel(dispositivoService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoResponseDto);
    }

    @PostMapping(version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<DispositivoResponseDto> saveDispositivo(@RequestBody @Valid DispositivoRequestDto dispositivoRequestDto) {
        DispositivoResponseDto dispositivoResponseDto = DispositivoResponseDto.fromModel(dispositivoService.save(dispositivoRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoResponseDto);
    }

    @PutMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<DispositivoResponseDto> updateDispositivo(@PathVariable(value = "id") UUID id,
                                                                    @RequestBody @Valid DispositivoRequestDto dispositivoRequestDto) {
        DispositivoResponseDto dispositivoResponseDto = DispositivoResponseDto.fromModel(dispositivoService.update(id, dispositivoRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoResponseDto);
    }

    @DeleteMapping(value = "/{id}", version = "v1")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMINISTRADOR', 'SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteDispositivo(@PathVariable(value = "id") UUID id) {
        dispositivoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}