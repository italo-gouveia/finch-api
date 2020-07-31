package com.finch.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finch.api.converter.DozerConverter;
import com.finch.api.data.model.Snack;
import com.finch.api.data.vo.SnackVO;
import com.finch.api.exception.ResourceNotFoundException;
import com.finch.api.repository.SnackRepository;
import com.finch.api.services.ISnackService;
import com.finch.api.util.MessagesUtil;

@Service
public class SnackServices implements ISnackService {

    @Autowired
    SnackRepository repository;

    @Override
    public SnackVO create(SnackVO snack) {
        Snack entity = DozerConverter.parseObject(snack, Snack.class);
        return DozerConverter.parseObject(repository.save(entity), SnackVO.class);
    }

    @Override
    public Page<SnackVO> findSnackByName(String name, Pageable pageable) {
        Page<Snack> page = repository.findSnackByName(name, pageable);
        return page.map(this::convertToSnackVO);
    }

    @Override
    public Page<SnackVO> findAll(Pageable pageable) {
        Page<Snack> page = repository.findAll(pageable);
        return page.map(this::convertToSnackVO);
    }

    private SnackVO convertToSnackVO(Snack entity) {
        return DozerConverter.parseObject(entity, SnackVO.class);
    }

    @Override
    public SnackVO findById(Long id) {
    	Snack entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        return DozerConverter.parseObject(entity, SnackVO.class);
    }

    @Override
    public SnackVO update(SnackVO client) {
        Snack entity = repository.findById(client.getKey())
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));

        entity.setName(client.getName());
        entity.setIngredients(client.getIngredients());

        return DozerConverter.parseObject(repository.save(entity), SnackVO.class);
    }

    @Override
    public void delete(Long id) {
    	Snack entity = null;
    	entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        repository.delete(entity);
    }

}
