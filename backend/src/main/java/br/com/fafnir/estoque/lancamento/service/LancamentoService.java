package br.com.fafnir.estoque.lancamento.service;

import br.com.fafnir.estoque.config.TenantContext;
import br.com.fafnir.estoque.dispositivo.model.DispositivoModel;
import br.com.fafnir.estoque.dispositivo.service.DispositivoService;
import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
import br.com.fafnir.estoque.lancamento.dto.LancamentoFilterDto;
import br.com.fafnir.estoque.lancamento.dto.LancamentoRequestDto;
import br.com.fafnir.estoque.lancamento.model.LancamentoModel;
import br.com.fafnir.estoque.lancamento.repository.LancamentoRepository;
import br.com.fafnir.estoque.lancamento.specification.LancamentoSpecification;
import br.com.fafnir.estoque.produto.model.ProdutoModel;
import br.com.fafnir.estoque.produto.service.ProdutoService;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import br.com.fafnir.estoque.usuario.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    private final EmpresaService empresaService;

    private final ProdutoService produtoService;

    private final DispositivoService dispositivoService;

    private final UsuarioService usuarioService;

    public LancamentoService(LancamentoRepository lancamentoRepository, EmpresaService empresaService, ProdutoService produtoService, DispositivoService dispositivoService, UsuarioService usuarioService) {
        this.lancamentoRepository = lancamentoRepository;
        this.empresaService = empresaService;
        this.produtoService = produtoService;
        this.dispositivoService = dispositivoService;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public Page<LancamentoModel> findAll(LancamentoFilterDto lancamentoFilterDto, Pageable pageable) {
        return lancamentoRepository.findAll(LancamentoSpecification.getSpecification(lancamentoFilterDto), pageable);
    }

    @Transactional(readOnly = true)
    public LancamentoModel findById(UUID id) {
        LancamentoModel lancamentoModel = lancamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado."));

        TenantContext.validateTenantAccess(lancamentoModel.getEmpresa().getId());

        return lancamentoModel;
    }

    @Transactional
    public LancamentoModel save(LancamentoRequestDto lancamentoRequestDto) {
        UUID empresaId = resolveAndValidateEmpresaId(lancamentoRequestDto.empresaId());
        EmpresaModel empresaModel = empresaService.findById(empresaId);
        ProdutoModel produtoModel = produtoService.findById(lancamentoRequestDto.produtoId());
        DispositivoModel dispositivoModel = dispositivoService.findById(lancamentoRequestDto.dispositivoId());

        UsuarioModel usuarioModel = lancamentoRequestDto.usuarioId() != null
                ? usuarioService.findById(lancamentoRequestDto.usuarioId())
                : null;

        var lancamentoModel = lancamentoRequestDto.toModel();
        lancamentoModel.setEmpresa(empresaModel);
        lancamentoModel.setProduto(produtoModel);
        lancamentoModel.setDispositivo(dispositivoModel);
        lancamentoModel.setUsuario(usuarioModel);

        return lancamentoRepository.save(lancamentoModel);
    }

    @Transactional
    public LancamentoModel update(UUID id, LancamentoRequestDto lancamentoRequestDto) {
        LancamentoModel lancamentoModel = findById(id);

        UUID empresaId = resolveAndValidateEmpresaId(lancamentoRequestDto.empresaId());

        EmpresaModel empresaModel = empresaService.findById(empresaId);
        ProdutoModel produtoModel = produtoService.findById(lancamentoRequestDto.produtoId());
        DispositivoModel dispositivoModel = dispositivoService.findById(lancamentoRequestDto.dispositivoId());

        UsuarioModel usuarioModel = lancamentoRequestDto.usuarioId() != null
                ? usuarioService.findById(lancamentoRequestDto.usuarioId())
                : null;

        BeanUtils.copyProperties(lancamentoRequestDto, lancamentoModel);
        lancamentoModel.setEmpresa(empresaModel);
        lancamentoModel.setProduto(produtoModel);
        lancamentoModel.setDispositivo(dispositivoModel);
        lancamentoModel.setUsuario(usuarioModel);

        return lancamentoRepository.save(lancamentoModel);
    }

    @Transactional
    public void delete(UUID id) {
        LancamentoModel lancamentoModel = findById(id);
        lancamentoRepository.delete(lancamentoModel);
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
