package com.victortavin.marmitaria.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.victortavin.marmitaria.dtos.UserDto;
import com.victortavin.marmitaria.dtos.UserInsertDto;
import com.victortavin.marmitaria.service.UserService;

import jakarta.validation.Valid;

@RestController(value = "/users")
public class UserController {
	
	@Autowired
	public UserService service;
	
	@PostMapping
	public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserInsertDto userInsert) {
			UserDto userDto = service.addUser(userInsert);
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id")
					.buildAndExpand(userInsert.getId()).toUri();
			
			return ResponseEntity.created(uri).body(userDto);
		}
	}
}
