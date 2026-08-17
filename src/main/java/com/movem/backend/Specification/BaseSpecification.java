package com.movem.backend.Specification;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseSpecification<T> {

    protected Specification<T> alwaysTrue() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
    }
}