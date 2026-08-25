package com.movem.backend.Mapper.BaseMapper;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractBaseMapper<E, R>
        implements BaseMapper<E, R> {

    @Override
    public List<R> toResponseList(List<E> entities) {

        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    protected LocalDateTime now() {
        return LocalDateTime.now();
    }
}