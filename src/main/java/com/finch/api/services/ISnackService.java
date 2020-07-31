package com.finch.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finch.api.data.vo.SnackVO;

public interface ISnackService {
	 public SnackVO create(SnackVO snack);
	 public Page<SnackVO> findSnackByName(String name, Pageable pageable);
	 public Page<SnackVO> findAll(Pageable pageable);
	 public SnackVO findById(Long id);
	 public SnackVO update(SnackVO snack);
	 public void delete(Long id);

}
