package br.com.fafnir.estoque.dispositivo.repository;

import br.com.fafnir.estoque.dispositivo.model.DispositivoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DispositivoRepository extends JpaRepository<DispositivoModel, UUID>, JpaSpecificationExecutor<DispositivoModel> {

    boolean existsByIdentificador(String identificador);

    boolean existsByIdentificadorAndIdNot(String identificador, UUID id);
}
