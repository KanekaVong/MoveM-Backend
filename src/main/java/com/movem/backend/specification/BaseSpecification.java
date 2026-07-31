package com.movem.backend.specification;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseSpecification<T> {

    protected Specification<T> alwaysTrue() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
    }
}