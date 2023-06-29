package com.victortavin.marmitaria.service;



import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victortavin.marmitaria.dtos.MessageDto;
import com.victortavin.marmitaria.dtos.RoleDto;
import com.victortavin.marmitaria.dtos.UserDto;
import com.victortavin.marmitaria.entities.MessageEntity;
import com.victortavin.marmitaria.entities.RoleEntity;
import com.victortavin.marmitaria.entities.UserEntity;
import com.victortavin.marmitaria.repositories.MessageRepository;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class MessageService {

	@Autowired
	private MessageRepository repository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Transactional(readOnly = true)
	public List<MessageDto> findAllPage(){
		UserEntity userEntity = recuperandoEmail();
		String email = userEntity.getEmail();
			
		List<MessageEntity> list = repository.findAllByRecipient(email);
		
		Collections.reverse(list);
		
		return list.stream().map(MessageDto::new).collect(Collectors.toList());
	}
	
	public void userRegisteredSuccessfully(String email, String name) {
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setRecipient(email);
		messageEntity.setTitle("Cadastro feito com sulcesso");
		messageEntity.setMessage("Ola "+name+", seja bem vindo ao marmitaria Spring!");
		messageEntity.setInstant(Instant.now());
		
		repository.save(messageEntity);
	}
	
	public void userLogin (String email, String dispositivo) {
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setRecipient(email);
		messageEntity.setTitle("Login feito em um dispositivo");
		messageEntity.setMessage("Um dispositvo ("+dispositivo+") fez login com o seu usuário, é você mesmo?");
		messageEntity.setInstant(Instant.now());
		
		repository.save(messageEntity);
	}
	
	public void addSaldoUser(float saldo) {
		UserEntity userEntity = recuperandoEmail();
		String email = userEntity.getEmail();
		
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setRecipient(email);
		messageEntity.setTitle("Saldo adicionado com sulcesso");
		messageEntity.setMessage("Olá, você acabou adicionar um saldo de "+saldo+
				" , agora só esperar a confirmação do banco para o saldo ser adicioando à carteira.");
		messageEntity.setInstant(Instant.now());
		
		repository.save(messageEntity);
	}
	
	public void addSaldoBanco(float saldo) {
		UserEntity userEntity = recuperandoEmail();
		String email = userEntity.getEmail();
		
		RoleDto roleDto = roleService.findByNameRole("Bank");
		
		List<UserDto> list = userService.findAllByRole(roleDto);
		
		for (UserDto userDto : list) {
			if (userDto.getRole().getName().equals("Bank")) {
				MessageEntity messageEntity = new MessageEntity();
				
				messageEntity.setRecipient(userDto.getEmail());
				messageEntity.setTitle("Solicitação de saldo");
				messageEntity.setMessage("Olá " + userDto.getFirstName() + ". o " 
						+ userEntity.getFirstName() + " adicionou um novo saldo de " 
						+ saldo + "e está esperando a aceitação do banco.");
				messageEntity.setInstant(Instant.now());
				
				repository.save(messageEntity);
			}
		}
		
	}
	
	public String informacoesDoDispositivo(HttpServletRequest request) {
        String userAgentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        
        String navegador = userAgent.getBrowser().getName();
        String sistemaOperacional = userAgent.getOperatingSystem().getName();
        
        return "navagador: " + navegador + " sitema operacional: " + sistemaOperacional;
	}
	
	private UserEntity recuperandoEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserEntity) {
		    UserEntity user = (UserEntity) authentication.getPrincipal();
		    // Faça o que precisar com o objeto User recuperado
		    return user;
		}
		return null;
	}
}
