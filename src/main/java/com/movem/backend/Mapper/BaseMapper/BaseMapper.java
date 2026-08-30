package com.movem.backend.Mapper.BaseMapper;

import java.util.List;

public interface BaseMapper<E, R> {

    R toResponse(E entity);

    List<R> toResponseList(List<E> entities);
}