package com.tinnova.lojaveiculo.security;

import com.tinnova.lojaveiculo.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna as permissões (Roles) do usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(usuario.getRole().name())
        );
    }

    /**
     * Retorna a senha criptografada.
     */
    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    /**
     * Retorna o login do usuário.
     */
    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    /**
     * Indica se a conta não expirou.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta não está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais não expiraram.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário está ativo.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Retorna a entidade completa do usuário.
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
