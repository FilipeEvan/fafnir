package br.com.fafnir.estoque.lancamento.repository;


import br.com.fafnir.estoque.lancamento.model.LancamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface LancamentoRepository extends JpaRepository<LancamentoModel, UUID>, JpaSpecificationExecutor<LancamentoModel> {
}
