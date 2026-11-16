package br.com.fafnir.estoque.config;

import br.com.fafnir.estoque.empresa.model.EmpresaModel;
import br.com.fafnir.estoque.empresa.model.Endereco;
import br.com.fafnir.estoque.empresa.model.SituacaoEmpresa;
import br.com.fafnir.estoque.empresa.repository.EmpresaRepository;
import br.com.fafnir.estoque.usuario.model.PapelUsuario;
import br.com.fafnir.estoque.usuario.model.SituacaoUsuario;
import br.com.fafnir.estoque.usuario.model.UsuarioModel;
import br.com.fafnir.estoque.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final EmpresaRepository empresaRepository;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.password}")
    private String adminPassword;

    public AdminUserConfig(EmpresaRepository empresaRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        EmpresaModel empresaModel = createEmpresaIfNotExists("30592404000182");

        createAdministradorIfNotExists(empresaModel, "amandaaandrade05@gmail.com", adminPassword, "Amanda Acosta de Andrade");

        createAdministradorIfNotExists(empresaModel, "filipegelista@gmail.com", adminPassword, "Filipe Evangelista Avila");
    }

    private EmpresaModel createEmpresaIfNotExists(String documento) {
        return empresaRepository.findByDocumento(documento).orElseGet(
                () -> {
                    Endereco endereco = new Endereco();
                    endereco.setCep("17525030");
                    endereco.setLogradouro("Rua Pedro Alpino");
                    endereco.setBairro("Jardim Araxa");
                    endereco.setNumero("401");
                    endereco.setMunicipio("Marília");
                    endereco.setUf("SP");

                    EmpresaModel empresaModel = new EmpresaModel();
                    empresaModel.setDocumento(documento);
                    empresaModel.setSituacao(SituacaoEmpresa.ATIVA);
                    empresaModel.setRazaoSocial("Fafnir Ltda.");
                    empresaModel.setNomeFantasia("Fafnir");
                    empresaModel.setTelefone("1434253302");
                    empresaModel.setEndereco(endereco);

                    return empresaRepository.save(empresaModel);
                });
    }

    private void createAdministradorIfNotExists(EmpresaModel empresaModel, String email, String senha, String nomeCompleto) {
        Optional<UsuarioModel> usuarioModelOptional = usuarioRepository.findByEmailIgnoreCase(email);

        usuarioModelOptional.ifPresentOrElse(
                usuarioModel -> {
                    System.out.println("Administrador já cadastrado");
                },
                () -> {
                    UsuarioModel usuarioModel = new UsuarioModel();
                    usuarioModel.setEmpresa(empresaModel);
                    usuarioModel.setSituacao(SituacaoUsuario.ATIVO);
                    usuarioModel.setPapel(PapelUsuario.ADMINISTRADOR);
                    usuarioModel.setNomeCompleto(nomeCompleto);
                    usuarioModel.setEmail(email);
                    usuarioModel.setSenha(passwordEncoder.encode(senha));
                    usuarioRepository.save(usuarioModel);
                }
        );
    }
}
