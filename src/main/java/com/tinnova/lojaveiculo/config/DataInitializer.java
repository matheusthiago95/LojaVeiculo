package com.tinnova.lojaveiculo.config;

import com.tinnova.lojaveiculo.entity.Role;
import com.tinnova.lojaveiculo.entity.Usuario;
import com.tinnova.lojaveiculo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        criarAdmin();

        criarUser();
    }

    private void criarAdmin() {

        if (!usuarioRepository.existsByLogin("admin")) {

            Usuario admin = new Usuario();

            admin.setNome("Administrador");

            admin.setLogin("admin");

            admin.setSenha(
                    passwordEncoder.encode("123456"));

            admin.setRole(Role.ROLE_ADMIN);

            usuarioRepository.save(admin);
        }
    }

    private void criarUser() {

        if (!usuarioRepository.existsByLogin("user")) {

            Usuario user = new Usuario();

            user.setNome("Usuário");

            user.setLogin("user");

            user.setSenha(
                    passwordEncoder.encode("123456"));

            user.setRole(Role.ROLE_USER);

            usuarioRepository.save(user);
        }
    }

}
