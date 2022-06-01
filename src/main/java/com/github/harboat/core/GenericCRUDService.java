package com.github.harboat.core;

import com.github.harboat.clients.exceptions.InternalServerError;

import java.util.Collection;

public interface GenericCRUDService<PostDto, GetDto, PutDto, ResponseDto> {
    ResponseDto create(PostDto postDto);
    GetDto get(String id);
    Collection<GetDto> getAll();
    GetDto update(String id, PutDto putDto) throws InternalServerError;
    ResponseDto delete(String id);
}
