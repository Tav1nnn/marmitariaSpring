package com.victortavin.marmitaria.service.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victortavin.marmitaria.dtos.menu.MenuDto;
import com.victortavin.marmitaria.dtos.menu.MenuUpdateDto;
import com.victortavin.marmitaria.entities.menu.MenuEntity;
import com.victortavin.marmitaria.repositories.menu.MenuRepository;

import jakarta.transaction.Transactional;

@Service
public class UpdateMenuService {
	
	@Autowired
	private MenuRepository repository;

	@Transactional
	public MenuDto update(Long id, MenuUpdateDto menuUpdateDto) {
		MenuEntity menuEntity = repository.getReferenceById(id);
		
		copyMenuUpdateDtoIntoEntity(menuUpdateDto, menuEntity);
		
		menuEntity = repository.save(menuEntity);
		
		return new MenuDto(menuEntity);
	}
	
	public void copyMenuUpdateDtoIntoEntity(MenuUpdateDto menuUpdateDto, MenuEntity menuEntity) {
		if (menuUpdateDto.getName() != null) {
			menuEntity.setName(menuUpdateDto.getName());
		}
		
		if (menuUpdateDto.getPrice() != 0) {
			menuEntity.setPrice(menuUpdateDto.getPrice());
		}
		
		if (menuUpdateDto.getDiscount() != 0) {
			menuEntity.setDiscount(menuUpdateDto.getDiscount());
		}
		
		if (menuUpdateDto.getDescription() != null) {
			menuEntity.setDescription(menuUpdateDto.getDescription());
		}
	}
}
