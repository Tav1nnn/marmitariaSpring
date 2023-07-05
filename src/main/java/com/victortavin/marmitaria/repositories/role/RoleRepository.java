package com.victortavin.marmitaria.repositories.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortavin.marmitaria.entities.role.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
	Optional<RoleEntity> findByName(String name);
	Optional<RoleEntity> findById(Long id);
}
