/* Decathlon (C)2023 */
package com.decathlon.rest.crud.api;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

public interface CrudRestApi<
        D extends Serializable, L extends Serializable, E extends Serializable> {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    D findById(@PathVariable("id") Long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    D save(@RequestBody @Valid D dto);

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    D patch(@PathVariable("id") Long id, @RequestBody D dto);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    D update(@PathVariable("id") Long id, @RequestBody @Valid D dto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id);

    @GetMapping("/paginated")
    Page<L> findAllByPagination(
            Pageable pagination, @RequestParam(required = false) String globalFilter);
}
