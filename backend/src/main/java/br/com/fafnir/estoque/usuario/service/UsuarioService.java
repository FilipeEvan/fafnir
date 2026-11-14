package br.com.fafnir.estoque.usuario.service;

import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
import br.com.fafnir.estoque.usuario.dto.UsuarioFilterDto;
import br.com.fafnir.estoque.usuario.dto.UsuarioRequestDto;
import br.com.fafnir.estoque.usuario.model.SituacaoUsuario;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import br.com.fafnir.estoque.usuario.repository.UsuarioRepository;
import br.com.fafnir.estoque.usuario.specification.UsuarioSpecification;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    @Transactional
    public UsuarioModel save(UsuarioRequestDto usuarioRequestDto) {
        EmpresaModel empresaModel = empresaService.findById(usuarioRequestDto.empresaId());

        if (usuarioRepository.existsByEmail(usuarioRequestDto.email())) {
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

        if (usuarioRepository.existsByEmailAndIdNot(usuarioRequestDto.email(), id)) {
            throw new EntityExistsException("E-mail já cadastrado.");
        }

        EmpresaModel  empresaModel = empresaService.findById(usuarioRequestDto.empresaId());
        BeanUtils.copyProperties(usuarioRequestDto, usuarioModel);
        usuarioModel.setEmpresa(empresaModel);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioRequestDto.senha()));

        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public void delete(UUID id) {
        UsuarioModel usuarioModel = findById(id);
        usuarioRepository.delete(usuarioModel);
    }
}