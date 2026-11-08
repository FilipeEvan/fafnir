package br.com.fafnir.estoque.service;

import br.com.fafnir.estoque.dto.EmpresaRecordDto;
import br.com.fafnir.estoque.model.EmpresaModel;
import br.com.fafnir.estoque.model.SituacaoEmpresa;
import br.com.fafnir.estoque.repository.EmpresaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Optional<EmpresaModel> findById(UUID id) {
        return empresaRepository.findById(id);
    }

    public EmpresaModel save(EmpresaRecordDto empresaRecordDto) {
        var empresaModel = new EmpresaModel();
        BeanUtils.copyProperties(empresaRecordDto, empresaModel);
        empresaModel.setSituacao(SituacaoEmpresa.ATIVA);
        return empresaRepository.save(empresaModel);
    }

    public EmpresaModel update(EmpresaModel empresaModel, EmpresaRecordDto empresaRecordDto) {
        BeanUtils.copyProperties(empresaRecordDto, empresaModel);
        return empresaRepository.save(empresaModel);
    }

    public void delete(EmpresaModel empresaModel) {
        empresaRepository.delete(empresaModel);
    }
}
