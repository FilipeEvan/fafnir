package br.com.fafnir.estoque.usuario.service;

import br.com.fafnir.estoque.config.TenantContext;
import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
import br.com.fafnir.estoque.usuario.dto.UsuarioFilterDto;
import br.com.fafnir.estoque.usuario.dto.UsuarioRequestDto;
import br.com.fafnir.estoque.usuario.model.PapelUsuario;
import br.com.fafnir.estoque.usuario.model.SituacaoUsuario;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import br.com.fafnir.estoque.usuario.repository.UsuarioRepository;
import br.com.fafnir.estoque.usuario.specification.UsuarioSpecification;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final EmpresaService empresaService;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaService empresaService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.empresaService = empresaService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioModel> findAll(UsuarioFilterDto usuarioFilterDto, Pageable pageable) {
        return usuarioRepository.findAll(UsuarioSpecification.getSpecification(usuarioFilterDto), pageable);
    }

    @Transactional(readOnly = true)
    public UsuarioModel findById(UUID id) {
        UsuarioModel usuarioModel =  usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        TenantContext.validateTenantAccess(usuarioModel.getEmpresa().getId());

        return usuarioModel;
    }

    @Transactional
    public UsuarioModel save(UsuarioRequestDto usuarioRequestDto) {
        UUID empresaId = resolveAndValidateEmpresaId(usuarioRequestDto.empresaId());

        validatePapel(usuarioRequestDto.papel());

        EmpresaModel empresaModel = empresaService.findById(empresaId);

        if (usuarioRepository.existsByEmailIgnoreCase(usuarioRequestDto.email())) {
            throw new EntityExistsException("E-mail já cadastrado.");
        }

        var usuarioModel = usuarioRequestDto.toModel();
        usuarioModel.setSituacao(SituacaoUsuario.ATIVO);
        usuarioModel.setEmpresa(empresaModel);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioRequestDto.senha()));

        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public UsuarioModel update(UUID id, UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioModel = findById(id);

        UUID empresaId = resolveAndValidateEmpresaId(usuarioRequestDto.empresaId());

        validatePapel(usuarioModel.getPapel());
        validatePapel(usuarioRequestDto.papel());

        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(usuarioRequestDto.email(), id)) {
            throw new EntityExistsException("E-mail já cadastrado.");
        }

        EmpresaModel empresaModel = empresaService.findById(empresaId);

        BeanUtils.copyProperties(usuarioRequestDto, usuarioModel);
        usuarioModel.setEmpresa(empresaModel);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioRequestDto.senha()));

        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public void delete(UUID id) {
        UsuarioModel usuarioModel = findById(id);
        validatePapel(usuarioModel.getPapel());
        usuarioRepository.delete(usuarioModel);
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

    private void validatePapel(PapelUsuario papelUsuario) {
        if (papelUsuario != null) {
            if (!TenantContext.isSuperAdmin() && papelUsuario == PapelUsuario.ADMINISTRADOR) {
                throw new AccessDeniedException("Apenas administradores podem definir papel de usuário de ADMINISTRADOR.");
            }
        }
    }
}