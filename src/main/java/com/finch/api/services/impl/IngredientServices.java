package com.finch.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finch.api.converter.DozerConverter;
import com.finch.api.data.model.Ingredient;
import com.finch.api.data.vo.IngredientVO;
import com.finch.api.exception.ResourceNotFoundException;
import com.finch.api.repository.IngredientRepository;
import com.finch.api.services.IIngredientService;
import com.finch.api.util.MessagesUtil;

@Service
public class IngredientServices implements IIngredientService {

    @Autowired
    IngredientRepository repository;

    @Override
    public IngredientVO create(IngredientVO ingredient) {
    	Ingredient entity = DozerConverter.parseObject(ingredient, Ingredient.class);
        return DozerConverter.parseObject(repository.save(entity), IngredientVO.class);
    }

    @Override
    public Page<IngredientVO> findIngredientByName(String name, Pageable pageable) {
        Page<Ingredient> page = repository.findIngredientByName(name, pageable);
        return page.map(this::convertToIngredientVO);
    }

    @Override
    public Page<IngredientVO> findAll(Pageable pageable) {
        Page<Ingredient> page = repository.findAll(pageable);
        return page.map(this::convertToIngredientVO);
    }

    private IngredientVO convertToIngredientVO(Ingredient entity) {
        return DozerConverter.parseObject(entity, IngredientVO.class);
    }

    @Override
    public IngredientVO findById(Long id) {
    	Ingredient entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        return DozerConverter.parseObject(entity, IngredientVO.class);
    }

    @Override
    public IngredientVO update(IngredientVO client) {
    	Ingredient entity = repository.findById(client.getKey())
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));

        entity.setName(client.getName());
        entity.setPrice(client.getPrice());

        return DozerConverter.parseObject(repository.save(entity), IngredientVO.class);
    }

    @Override
    public void delete(Long id) {
    	Ingredient entity = null;
    	entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        repository.delete(entity);
    }

}
