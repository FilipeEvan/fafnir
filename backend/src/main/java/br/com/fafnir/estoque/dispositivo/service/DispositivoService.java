package br.com.fafnir.estoque.dispositivo.service;

import br.com.fafnir.estoque.config.TenantContext;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoFilterDto;
import br.com.fafnir.estoque.dispositivo.dto.DispositivoRequestDto;
import br.com.fafnir.estoque.dispositivo.model.DispositivoModel;
import br.com.fafnir.estoque.dispositivo.model.StatusDispositivo;
import br.com.fafnir.estoque.dispositivo.repository.DispositivoRepository;
import br.com.fafnir.estoque.dispositivo.specification.DispositivoSpecification;
import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
import br.com.fafnir.estoque.produto.model.ProdutoModel;
import br.com.fafnir.estoque.produto.service.ProdutoService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;

    private final EmpresaService empresaService;

    private final ProdutoService produtoService;

    public DispositivoService(DispositivoRepository dispositivoRepository, EmpresaService empresaService, ProdutoService produtoService) {
        this.dispositivoRepository = dispositivoRepository;
        this.empresaService = empresaService;
        this.produtoService = produtoService;
    }

    @Transactional(readOnly = true)
    public Page<DispositivoModel> findAll(DispositivoFilterDto dispositivoFilterDto, Pageable pageable) {
        return dispositivoRepository.findAll(DispositivoSpecification.getSpecification(dispositivoFilterDto), pageable);
    }

    @Transactional(readOnly = true)
    public DispositivoModel findById(UUID id) {
        DispositivoModel dispositivoModel = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado."));

        TenantContext.validateTenantAccess(dispositivoModel.getEmpresa().getId());

        return dispositivoModel;
    }

    @Transactional
    public DispositivoModel save(DispositivoRequestDto dispositivoRequestDto) {
        UUID empresaId = resolveAndValidateEmpresaId(dispositivoRequestDto.empresaId());
        EmpresaModel empresaModel = empresaService.findById(empresaId);

        if (dispositivoRepository.existsByIdentificador(dispositivoRequestDto.identificador())) {
            throw new EntityExistsException("Dispositivo já cadastrado.");
        }

        ProdutoModel produtoModel = dispositivoRequestDto.produtoId() != null
                ? produtoService.findById(dispositivoRequestDto.produtoId())
                : null;

        var dispositivoModel = dispositivoRequestDto.toModel();
        dispositivoModel.setStatus(StatusDispositivo.EXECUTANDO);
        dispositivoModel.setEmpresa(empresaModel);
        dispositivoModel.setProduto(produtoModel);

        return dispositivoRepository.save(dispositivoModel);
    }

    @Transactional
    public DispositivoModel update(UUID id, DispositivoRequestDto dispositivoRequestDto) {
        DispositivoModel dispositivoModel = findById(id);

        UUID empresaId = resolveAndValidateEmpresaId(dispositivoRequestDto.empresaId());

        if (dispositivoRepository.existsByIdentificadorAndIdNot(dispositivoRequestDto.identificador(), id)) {
            throw new EntityExistsException("Dispositivo já cadastrado.");
        }

        EmpresaModel empresaModel = empresaService.findById(empresaId);
        ProdutoModel produtoModel = dispositivoRequestDto.produtoId() != null
                ? produtoService.findById(dispositivoRequestDto.produtoId())
                : null;

        BeanUtils.copyProperties(dispositivoRequestDto, dispositivoModel);
        dispositivoModel.setEmpresa(empresaModel);
        dispositivoModel.setProduto(produtoModel);

        return dispositivoRepository.save(dispositivoModel);
    }

    @Transactional
    public void delete(UUID id) {
        DispositivoModel dispositivoModel = findById(id);
        dispositivoRepository.delete(dispositivoModel);
    }

    private UUID resolveAndValidateEmpresaId(UUID empresaId) {
        if (TenantContext.isSuperAdmin()) {
            return empresaId != null ? empresaId : TenantContext.getTenantId();
        } else {
            if (empresaId != null) {
                throw new AccessDeniedException("Apenas administradores podem definir a empresa no corpo da requisição.");
            }

            UUID tenantId = TenantContext.getTenantId();
            if (tenantId == null) {
                throw new IllegalStateException("Empresa não encontrada no contexto da requisição.");
            }
            return tenantId;
        }
    }
}
