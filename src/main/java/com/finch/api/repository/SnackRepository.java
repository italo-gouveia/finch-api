package com.finch.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finch.api.data.model.Snack;

@Repository
public interface SnackRepository extends JpaRepository<Snack, Long> {
    @Query("SELECT s FROM Snack s WHERE s.name LIKE LOWER(CONCAT ('%', :name, '%'))")
    Page<Snack> findSnackByName(@Param("name") String name, Pageable pageable);
}