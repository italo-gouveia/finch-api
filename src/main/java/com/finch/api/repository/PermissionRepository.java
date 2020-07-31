package com.finch.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finch.api.data.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
