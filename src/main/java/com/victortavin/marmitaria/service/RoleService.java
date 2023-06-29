package com.victortavin.marmitaria.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victortavin.marmitaria.dtos.RoleDto;
import com.victortavin.marmitaria.dtos.RoleInsertDto;
import com.victortavin.marmitaria.entities.RoleEntity;
import com.victortavin.marmitaria.repositories.RoleRepository;
import com.victortavin.marmitaria.service.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository repository;

	@Transactional
	public RoleDto addRole(@Valid RoleInsertDto roleInsertDto) {
		RoleEntity roleEntity = new RoleEntity();
		copyRoleInsertDtoToRoleEntity(roleInsertDto, roleEntity);
		
		roleEntity = repository.save(roleEntity);
		
		return new RoleDto(roleEntity);
		
	}
	
	private void copyRoleInsertDtoToRoleEntity(RoleInsertDto roleDto, RoleEntity roleEntity) {
		roleEntity.setName(roleDto.getName());
	}

	public String delete(Long id) {
		try {
			RoleEntity roleEntity = repository.getReferenceById(id);
			repository.delete(roleEntity);
			return roleEntity.getName();
		}
		catch (Exception e) {
			throw new ResourceNotFoundException("Role não encontrada ou está em uso");
		}

	}

	public List<RoleDto> getRoles() {
		List<RoleDto> dtoList = new ArrayList<RoleDto>();
		List<RoleEntity> entityList = repository.findAll();
		
		for (RoleEntity roleEntity : entityList) {
			dtoList.add(new RoleDto(roleEntity));
		}
		
		return dtoList;
	}
	
	public RoleDto getRoleById(Long id) {
		try {
			RoleEntity entity = repository.getReferenceById(id);
			return new RoleDto(entity);
		}catch (Exception e) {
			throw new ResourceNotFoundException("Role não encontrada");
		}
	}

	public RoleDto updateRole(Long id, @Valid RoleInsertDto insertDto) {
		RoleEntity entity = repository.getReferenceById(id);
		copyRoleInsertDtoToRoleEntity(insertDto, entity);
		entity = repository.save(entity);
		return new RoleDto(entity);
	}
}
