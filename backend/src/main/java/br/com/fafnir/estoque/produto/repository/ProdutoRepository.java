package br.com.fafnir.estoque.produto.repository;

import br.com.fafnir.estoque.produto.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, UUID>, JpaSpecificationExecutor<ProdutoModel> {

    boolean existsByNomeIgnoreCaseAndMarcaIgnoreCaseAndEmpresaId(String nome, String marca, UUID empresaId);

    boolean existsByNomeIgnoreCaseAndMarcaIgnoreCaseAndEmpresaIdAndIdNot(String nome, String marca, UUID empresaId, UUID id);
}
