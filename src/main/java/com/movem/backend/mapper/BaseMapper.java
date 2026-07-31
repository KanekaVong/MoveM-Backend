package com.movem.backend.mapper;

import java.util.List;

public interface BaseMapper<E, R> {

    R toResponse(E entity);

    List<R> toResponseList(List<E> entities);
}