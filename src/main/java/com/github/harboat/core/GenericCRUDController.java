package com.github.harboat.core;

import com.github.harboat.clients.exceptions.InternalServerError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Validated
public abstract class GenericCRUDController
        <PostDto, GetDto, PutDto, ResponseDto, Service extends GenericCRUDService<PostDto, GetDto, PutDto, ResponseDto>>
{
    private final Service service;

    protected GenericCRUDController(final Service service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> create(
            @RequestBody @Valid final PostDto postDto
    ) {
        return new ResponseEntity<>(service.create(postDto), HttpStatus.CREATED);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<GetDto> read(
            @PathVariable final String identifier
    ) {
        return ResponseEntity.ok(service.get(identifier));
    }

    @GetMapping
    public ResponseEntity<Collection<GetDto>> readAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{identifier}")
    public ResponseEntity<GetDto> update(
            @PathVariable final String identifier,
            @RequestBody @Valid final PutDto putDto
    ) throws InternalServerError {
        return ResponseEntity.ok(service.update(identifier, putDto));
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable final String  identifier
    ) {
        return ResponseEntity.ok(service.delete(identifier));
    }
}
