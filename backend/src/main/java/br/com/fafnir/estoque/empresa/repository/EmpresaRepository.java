package br.com.fafnir.estoque.empresa.repository;

import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaModel, UUID>, JpaSpecificationExecutor<EmpresaModel> {
}
