package br.com.fafnir.estoque.usuario.repository;

import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
}
