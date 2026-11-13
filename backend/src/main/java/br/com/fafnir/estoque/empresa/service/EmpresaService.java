package br.com.fafnir.estoque.empresa.service;

import br.com.fafnir.estoque.empresa.dto.EmpresaRequestDto;
import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.model.SituacaoEmpresa;
import br.com.fafnir.estoque.empresa.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<EmpresaModel> findAll() {
        return empresaRepository.findAll();
    }

    public EmpresaModel findById(UUID id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    public EmpresaModel save(EmpresaRequestDto empresaRequestDto) {
        var empresaModel = empresaRequestDto.toModel();
        empresaModel.setSituacao(SituacaoEmpresa.ATIVA);
        empresaModel.setEndereco(empresaRequestDto.endereco().toModel());
        return empresaRepository.save(empresaModel);
    }

    public EmpresaModel update(EmpresaModel empresaModel, EmpresaRequestDto empresaRequestDto) {
        BeanUtils.copyProperties(empresaRequestDto, empresaModel);
        empresaModel.setEndereco(empresaRequestDto.endereco().toModel());
        return empresaRepository.save(empresaModel);
    }

    public void delete(EmpresaModel empresaModel) {
        empresaRepository.delete(empresaModel);
    }
}
