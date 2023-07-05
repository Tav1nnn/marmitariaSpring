package com.victortavin.marmitaria.service.menu;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.victortavin.marmitaria.entities.menu.MenuEntity;
import com.victortavin.marmitaria.repositories.menu.MenuRepository;
import com.victortavin.marmitaria.service.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class DeleteMenuService {

	@Autowired
	private MenuRepository repository;

	@Transactional
	public String deleteMenu(Long id) {
		checkMenuEmptyById(id);
		
		try {
			return deleteMenuById(id);				
		}
		catch (Exception e) {
			throw new DataIntegrityViolationException("");
		}
	}
	
	public void checkMenuEmptyById(Long id) {
		Optional<MenuEntity> menuOptional = repository.findById(id);
		
		if(menuOptional.isEmpty()) {
			throw new ResourceNotFoundException("Menu não encontrado");
		}
	}
	
	public String deleteMenuById(Long id){
		MenuEntity menuEntity = repository.getReferenceById(id);
		repository.delete(menuEntity);
		return menuEntity.getName();
	}
}
