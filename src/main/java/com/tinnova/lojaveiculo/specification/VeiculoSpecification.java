package com.tinnova.lojaveiculo.specification;

import com.tinnova.lojaveiculo.entity.Veiculo;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class VeiculoSpecification {

    public static Specification<Veiculo> hasMarca(String marca) {

        return (root, query, builder) ->
                builder.equal(root.get("marca"), marca);

    }

    public static Specification<Veiculo> hasColor(String cor) {

        return (root, query, builder) ->
                builder.equal(root.get("cor"), cor);

    }

    public static Specification<Veiculo> hasAno(Integer ano) {

        return (root, query, builder) ->
                builder.equal(root.get("ano"), ano);
    }

    public static Specification<Veiculo> precoMaiorQue(BigDecimal minPreco) {

        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get("precoUsd"),
                        minPreco);

    }
public static Specification<Veiculo> precoMenorQue(
            BigDecimal maxPreco) {

        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get("precoUsd"),
                        maxPreco);
    }

    public static Specification<Veiculo> isAtivo() {

        return (root, query, builder) ->
                builder.isTrue(root.get("ativo"));

    }

    public static Specification<Veiculo> withFilters(
            String marca,
            Integer ano,
            String cor,
            BigDecimal minPreco,
            BigDecimal maxPreco) {

        Specification<Veiculo> specification =
                Specification.where(isAtivo());

        if (marca != null && !marca.isBlank()) {
            specification = specification.and(hasMarca(marca));
        }

        if (ano != null) {
            specification = specification.and(hasAno(ano));
        }

        if (cor != null && !cor.isBlank()) {
            specification = specification.and(hasColor(cor));
        }

        if (minPreco != null) {
            specification = specification.and(precoMaiorQue(minPreco));
        }

        if (maxPreco != null) {
            specification = specification.and(precoMenorQue(maxPreco));
        }

        return specification;
    }
}
