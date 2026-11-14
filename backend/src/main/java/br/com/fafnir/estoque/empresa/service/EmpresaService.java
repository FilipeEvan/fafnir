package br.com.fafnir.estoque.empresa.service;

import br.com.fafnir.estoque.empresa.dto.EmpresaFilterDto;
import br.com.fafnir.estoque.empresa.dto.EmpresaRequestDto;
import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.model.SituacaoEmpresa;
import br.com.fafnir.estoque.empresa.repository.EmpresaRepository;
import br.com.fafnir.estoque.empresa.specification.EmpresaSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public Page<EmpresaModel> findAll(EmpresaFilterDto empresaFilterDto, Pageable pageable) {
        return empresaRepository.findAll(EmpresaSpecification.getSpecification(empresaFilterDto), pageable);
    }

    @Transactional(readOnly = true)
    public EmpresaModel findById(UUID id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    @Transactional
    public EmpresaModel save(EmpresaRequestDto empresaRequestDto) {
        var empresaModel = empresaRequestDto.toModel();
        empresaModel.setSituacao(SituacaoEmpresa.ATIVA);
        empresaModel.setEndereco(empresaRequestDto.endereco().toModel());

        return empresaRepository.save(empresaModel);
    }

    @Transactional
    public EmpresaModel update(UUID id, EmpresaRequestDto empresaRequestDto) {
        EmpresaModel empresaModel = findById(id);
        BeanUtils.copyProperties(empresaRequestDto, empresaModel);
        empresaModel.setEndereco(empresaRequestDto.endereco().toModel());

        return empresaRepository.save(empresaModel);
    }

    @Transactional
    public void delete(UUID id) {
        EmpresaModel empresaModel = findById(id);
        empresaRepository.delete(empresaModel);
    }
}
