package com.victortavin.marmitaria.service.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.victortavin.marmitaria.dtos.balance.BalanceDto;
import com.victortavin.marmitaria.dtos.role.RoleDto;
import com.victortavin.marmitaria.dtos.user.UserDto;
import com.victortavin.marmitaria.dtos.user.UserInsertDto;
import com.victortavin.marmitaria.dtos.user.UserUpdateDto;
import com.victortavin.marmitaria.entities.balance.BalanceEntity;
import com.victortavin.marmitaria.entities.role.RoleEntity;
import com.victortavin.marmitaria.entities.user.UserEntity;
import com.victortavin.marmitaria.repositories.role.RoleRepository;
import com.victortavin.marmitaria.repositories.user.UserRepository;
import com.victortavin.marmitaria.service.balance.NewBalanceService;
import com.victortavin.marmitaria.service.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private NewBalanceService newBalanceService; 
	
	@Transactional
	public UserDto addUser(UserInsertDto userInsertDto) {
		UserEntity userEntity = new UserEntity();
		
		standardizeCpf(userInsertDto);
		copyUserInsertDtoToUserEntity(userInsertDto, userEntity);
		addRoleInUser(userEntity);
		addBalanceInUser(userEntity);
		
		userEntity = repository.save(userEntity);
		
		return new UserDto(userEntity);
		
	}
	
	@Transactional
	public UserDto findByidUser(Long id) {
		Optional<UserEntity> userOptional = repository.findById(id);
		
		UserEntity userEntity = userOptional.orElseThrow(()-> new ResourceNotFoundException("Id not found: " + id));
		
		return new UserDto(userEntity);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = repository.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("Email not found");
		}
		return user;
	}
	
	private void standardizeCpf(UserInsertDto userInsertDto) {
		String cpf = userInsertDto.getCpf();
		cpf = cpf.replaceAll("[^0-9]", "");
		userInsertDto.setCpf(cpf);
	}
	
	private void copyUserInsertDtoToUserEntity(UserInsertDto userInsertDto, UserEntity userEntity) {
		userEntity.setFirstName(userInsertDto.getFirstName());
		userEntity.setLastName(userInsertDto.getLastName());
		userEntity.setCpf(userInsertDto.getCpf());
		userEntity.setEmail(userInsertDto.getEmail());
		//criptografa a senha
		userEntity.setPassword(passwordEncoder.encode(userInsertDto.getPassword()));		
	}
	
	private void addRoleInUser(UserEntity userEntity) {
		Optional<RoleEntity> roleOptional = roleRepository.findByName("User");
		
		RoleEntity roleEntity = roleOptional.orElseThrow(()-> new ResourceNotFoundException("Role not found: User"));
		
		userEntity.setRole(roleEntity);
	}
	
	private void addBalanceInUser(UserEntity userEntity) {
		BalanceDto balanceDto = newBalanceService.newBalance();
		
		BalanceEntity balanceEntity = new BalanceEntity(balanceDto.getId(), balanceDto.getValue());
		
		userEntity.setBalance(balanceEntity);
		System.out.println(userEntity.getBalance().getId());
	
	}

	@Transactional
	public UserDto update(UserDto userDto, UserUpdateDto updateDto) {
		if(passwordEncoder.matches(updateDto.getOldPassword(), userDto.getPassword()))
		{
			UserEntity userEntity = copyUpdateDtoToEntity(userDto.getEmail(), updateDto);
			userEntity = repository.save(userEntity);
			return new UserDto(userEntity);
		}
		else {
			throw new BadCredentialsException("Senha inválida");
		}
	}
	
	public UserEntity copyUpdateDtoToEntity(String email, UserUpdateDto updateDto) {
		UserEntity user = repository.findByEmail(email);
		
		if(updateDto.getFirstName() != null) {
			user.setFirstName(updateDto.getFirstName());
		}
		
		if(updateDto.getLastName() != null) {
			user.setLastName(updateDto.getLastName());
		}
		
		if(updateDto.getCpf() != null) {
			user.setCpf(updateDto.getCpf());
		}
		
		if(updateDto.getNewPassword() != null) {
			user.setPassword(passwordEncoder.encode(updateDto.getNewPassword()));
		}
		
		return user;
	}
	
	@Transactional
	public UserDto findByEmailUser(String email) {
		Optional<UserEntity> userOptional = Optional.of(repository.findByEmail(email));
		
		UserEntity userEntity = userOptional.orElseThrow(()-> new ResourceNotFoundException("Email not found: " + email));
		UserDto userDto = new UserDto(userEntity);
		userDto.setPassword(userEntity.getPassword());  // fiz isso pq preciso da senha ao fazer o update
		return userDto;
	}

	@Transactional
	public void delete(UserDto userDto, String password) {
		if(passwordEncoder.matches(password, userDto.getPassword())) {
			repository.deleteById(userDto.getId());
		}
		else {
			throw new BadCredentialsException("Senha inválida");
		}
		
	}

	@Transactional
	public List<UserDto> getAllUsers() {
		List<UserEntity> entityList= repository.findAll();
		List<UserDto> dtoList = new ArrayList<UserDto>();
		for (UserEntity userEntity : entityList) {
			dtoList.add(new UserDto(userEntity));
		}
		return dtoList;
	}

	public List<String> updateUserRole(Long idUser, String roleName) {
		try {
			UserEntity userEntity = repository.getReferenceById(idUser);	
			Optional<RoleEntity> roleOptional = roleRepository.findByName(roleName);
			
			RoleEntity roleEntity = roleOptional.orElseThrow(() -> new ResourceNotFoundException("Esse role não existe"));
			
			userEntity.setRole(roleEntity);
			repository.save(userEntity);
			
			List<String> names = new ArrayList<String>();
			names.add(userEntity.getFirstName());
			names.add(roleEntity.getName());
			return names;
		}
		catch (Exception e) {
			throw new ResourceNotFoundException("User ou Role não encontrados");
		}
		
	}
	
	@Transactional
	public List<UserDto> findAllByRole(RoleDto roleDto){
		RoleEntity roleEntity = new RoleEntity(roleDto.getId(), roleDto.getName());
		
		List<UserEntity> listEntity = repository.findAllByRole(roleEntity);
		
		return listEntity.stream().map(UserDto::new).collect(Collectors.toList());
	}
	
}