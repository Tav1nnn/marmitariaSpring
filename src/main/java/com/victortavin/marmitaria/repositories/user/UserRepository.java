package com.victortavin.marmitaria.repositories.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortavin.marmitaria.entities.role.RoleEntity;
import com.victortavin.marmitaria.entities.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	UserEntity findByEmail(String email);
	UserEntity findByCpf(String cpf);
	List<UserEntity> findAllByRole(RoleEntity role);
}
