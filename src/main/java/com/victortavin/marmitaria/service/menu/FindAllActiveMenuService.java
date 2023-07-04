package com.victortavin.marmitaria.service.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victortavin.marmitaria.dtos.menu.MenuDto;
import com.victortavin.marmitaria.entities.menu.MenuEntity;
import com.victortavin.marmitaria.repositories.menu.MenuRepository;

import jakarta.transaction.Transactional;

@Service
public class FindAllActiveMenuService {

	@Autowired
	private MenuRepository repository;
	
	@Transactional
	public List<MenuDto> findAllActiveMenu(){
		List<MenuEntity> listEntity = new ArrayList<MenuEntity>();
		List<MenuDto> listDto = new ArrayList<MenuDto>();
		
		listEntity = repository.findAllActiveMenu();
		
		for (MenuEntity menuEntity : listEntity) {
			listDto.add(new MenuDto(menuEntity));
		}
		
		return listDto;
		
	}
}
