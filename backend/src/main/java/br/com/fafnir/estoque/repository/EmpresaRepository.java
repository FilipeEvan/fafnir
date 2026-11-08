package br.com.fafnir.estoque.repository;

import br.com.fafnir.estoque.model.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<EmpresaModel, UUID> {
}
