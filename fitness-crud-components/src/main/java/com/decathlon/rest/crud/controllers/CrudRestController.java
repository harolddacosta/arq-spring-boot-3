/* Decathlon (C)2023 */
package com.decathlon.rest.crud.controllers;

import com.decathlon.core.exceptions.ResourceNotFoundException;
import com.decathlon.rest.crud.api.CrudRestApi;
import com.decathlon.rest.crud.mappers.EntityMapper;
import com.decathlon.rest.crud.service.CrudService;
import com.decathlon.rest.data.domain.PageableRestResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class CrudRestController<
                D extends Serializable, L extends Serializable, E extends Serializable>
        implements CrudRestApi<D, L, E> {

    private final CrudService<E> crudService;
    private final EntityMapper<D, L, E> entityMapper;
    private final Validator validator;

    @Override
    public D findById(Long id) {
        E entity = findEntityForCrudOperation(id);

        return entityMapper.fromEntityToDto(entity);
    }

    @Override
    public D save(D dto) {
        return entityMapper.fromEntityToDto(
                crudService.save(entityMapper.fromDtoToEntityForSave(dto)));
    }

    @Override
    public D patch(Long id, D dto) {
        E entityToUpdate = findEntityForCrudOperation(id);

        log.trace("Patching Dto into entity");
        entityMapper.patchDtoIntoEntity(dto, entityToUpdate);

        log.trace("Mapping from entityToDto for validation");
        D dtoForValidation = entityMapper.fromEntityToDto(entityToUpdate);

        log.trace("Executing validators over Dto");
        Set<ConstraintViolation<D>> constraintViolations =
                validator.validate(dtoForValidation, Default.class);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        log.trace("Calling update in service and transforming from entity to Dto");
        return entityMapper.fromEntityToDto(crudService.update(entityToUpdate));
    }

    @Override
    public D update(Long id, D dto) {
        E entityToUpdate = findEntityForCrudOperation(id);

        entityMapper.mergeDtoIntoEntity(dto, entityToUpdate);

        return entityMapper.fromEntityToDto(crudService.update(entityToUpdate));
    }

    @Override
    public void delete(Long id) {
        E entityToDelete = findEntityForCrudOperation(id);

        crudService.delete(entityToDelete);
    }

    @Override
    public PageableRestResponse<L> findAllByPagination(Pageable pagination, String globalFilter) {
        // From entity pagination
        Page<E> entityPagination = crudService.findLike(globalFilter, pagination);

        // To Dto pagination
        return new PageableRestResponse<>(
                entityMapper.entitiesToDtos(entityPagination.getContent()),
                pagination,
                entityPagination.getTotalElements());
    }

    private E findEntityForCrudOperation(Long id) {
        log.trace("Finding by id:{}", id);

        return crudService.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }
}
