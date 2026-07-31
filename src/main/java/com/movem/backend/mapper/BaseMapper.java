package com.movem.backend.mapper;

public interface BaseMapper<E, R> {

    R toResponse(E entity);

}