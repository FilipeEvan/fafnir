package br.com.fafnir.estoque.produto.service;

import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.service.EmpresaService;
import br.com.fafnir.estoque.produto.dto.ProdutoFilterDto;
import br.com.fafnir.estoque.produto.dto.ProdutoRequestDto;
import br.com.fafnir.estoque.produto.model.ProdutoModel;
import br.com.fafnir.estoque.produto.repository.ProdutoRepository;
import br.com.fafnir.estoque.produto.specification.ProdutoSpecification;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final EmpresaService empresaService;

    public ProdutoService(ProdutoRepository produtoRepository, EmpresaService empresaService) {
        this.produtoRepository = produtoRepository;
        this.empresaService = empresaService;
    }

    @Transactional(readOnly = true)
    public Page<ProdutoModel> findAll(ProdutoFilterDto produtoFilterDto, Pageable pageable) {
        return produtoRepository.findAll(ProdutoSpecification.getSpecification(produtoFilterDto), pageable);
    }

    @Transactional(readOnly = true)
    public ProdutoModel findById(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
    }

    @Transactional
    public ProdutoModel save(ProdutoRequestDto produtoRequestDto) {
        EmpresaModel empresaModel = empresaService.findById(produtoRequestDto.empresaId());

        if (produtoRepository.existsByNomeIgnoreCaseAndMarcaIgnoreCaseAndEmpresaId(produtoRequestDto.nome(), produtoRequestDto.marca(), produtoRequestDto.empresaId())) {
            throw new EntityExistsException("Produto já cadastrado.");
        }

        var produtoModel = produtoRequestDto.toModel();
        produtoModel.setEmpresa(empresaModel);

        return produtoRepository.save(produtoModel);
    }

    @Transactional
    public ProdutoModel update(UUID id, ProdutoRequestDto produtoRequestDto) {
        ProdutoModel produtoModel = findById(id);

        if (produtoRepository.existsByNomeIgnoreCaseAndMarcaIgnoreCaseAndEmpresaIdAndIdNot(produtoRequestDto.nome(), produtoRequestDto.marca(), produtoRequestDto.empresaId(), id)) {
            throw new EntityExistsException("Produto já cadastrado.");
        }

        EmpresaModel  empresaModel = empresaService.findById(produtoRequestDto.empresaId());
        BeanUtils.copyProperties(produtoRequestDto, produtoModel);
        produtoModel.setEmpresa(empresaModel);

        return produtoRepository.save(produtoModel);
    }

    @Transactional
    public void delete(UUID id) {
        ProdutoModel produtoModel = findById(id);
        produtoRepository.delete(produtoModel);
    }
}