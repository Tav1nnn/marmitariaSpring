package com.victortavin.marmitaria.service.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.victortavin.marmitaria.dtos.role.RoleDto;
import com.victortavin.marmitaria.dtos.role.RoleInsertDto;
import com.victortavin.marmitaria.entities.role.RoleEntity;
import com.victortavin.marmitaria.repositories.role.RoleRepository;
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
	
	@Transactional
	public RoleDto findByNameRole(String name) {
		Optional<RoleEntity> roleOptional = repository.findByName(name);
		
		RoleEntity roleEntity = roleOptional.orElseThrow(()-> new ResourceNotFoundException("Role not found: "+ name));
		
		return new RoleDto(roleEntity);
	}

	@Transactional
	public String delete(Long id){
		Optional<RoleEntity> roleOptional = repository.findById(id);

		if(roleOptional.isEmpty()) {
			throw new ResourceNotFoundException("Role não encontrada");
		}

		try {
			RoleEntity roleEntity = repository.getReferenceById(id);
			repository.delete(roleEntity);
			return roleEntity.getName();			
		}
		catch (Exception e) {
			throw new DataIntegrityViolationException("");
		}
		
	}

	@Transactional
	public List<RoleDto> getRoles() {
		List<RoleDto> dtoList = new ArrayList<RoleDto>();
		List<RoleEntity> entityList = repository.findAll();
		
		for (RoleEntity roleEntity : entityList) {
			dtoList.add(new RoleDto(roleEntity));
		}
		
		return dtoList;
	}
	
	@Transactional
	public RoleDto getRoleById(Long id) {
		try {
			RoleEntity entity = repository.getReferenceById(id);
			return new RoleDto(entity);
		}catch (Exception e) {
			throw new ResourceNotFoundException("Role não encontrada");
		}
	}

	@Transactional
	public RoleDto updateRole(Long id, @Valid RoleInsertDto insertDto) {
		RoleEntity entity = repository.getReferenceById(id);
		copyRoleInsertDtoToRoleEntity(insertDto, entity);
		entity = repository.save(entity);
		return new RoleDto(entity);
	}
	
	private void copyRoleInsertDtoToRoleEntity (RoleInsertDto insertDto, RoleEntity entity) {
		entity.setName(insertDto.getName());
	}
	
}

