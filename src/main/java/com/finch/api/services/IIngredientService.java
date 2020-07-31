package com.finch.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finch.api.data.vo.IngredientVO;

public interface IIngredientService {
	 public IngredientVO create(IngredientVO snack);
	 public Page<IngredientVO> findIngredientByName(String name, Pageable pageable);
	 public Page<IngredientVO> findAll(Pageable pageable);
	 public IngredientVO findById(Long id);
	 public IngredientVO update(IngredientVO ingredient);
	 public void delete(Long id);
}
