package com.tinnova.lojaveiculo.repository;

import com.tinnova.lojaveiculo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo login.
     * Utilizado pelo Spring Security durante a autenticação.
     */
    Optional<Usuario> findByLogin(String login);

    /**
     * Verifica se já existe um usuário com o login informado.
     */
    boolean existsByLogin(String login);

}