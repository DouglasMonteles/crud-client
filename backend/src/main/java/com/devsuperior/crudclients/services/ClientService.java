package com.devsuperior.crudclients.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.crudclients.dtos.ClientDTO;
import com.devsuperior.crudclients.entities.Client;
import com.devsuperior.crudclients.repositories.ClientRepository;
import com.devsuperior.crudclients.services.exceptions.DatabaseException;
import com.devsuperior.crudclients.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPageable(PageRequest pageRequest) {
		var clientsPage = this.clientRepository.findAll(pageRequest);
		var clientsPageDTO = clientsPage.map(client -> new ClientDTO(client));
		
		return clientsPageDTO;
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		var optionalClient = this.clientRepository.findById(id);
		var client = optionalClient.orElseThrow(() ->
					new EntityNotFoundException("Entity not found"));
		
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO clientDTO) {
		var client = new Client();
		
		this.copyDtoToEntity(clientDTO, client);
		
		client = this.clientRepository.save(client);
		
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO clientDTO) {
		try {
			var client = this.clientRepository.getById(id);
			
			this.copyDtoToEntity(clientDTO, client);
			
			client = this.clientRepository.save(client);
			
			return new ClientDTO(client);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found: " + clientDTO.getId());
		}
	}
	
	public void delete(Long id) {
		try {
			this.clientRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
	
	private void copyDtoToEntity(ClientDTO clientDTO, Client client) {
		client.setName(clientDTO.getName());
		client.setCpf(clientDTO.getCpf());
		client.setIncome(clientDTO.getIncome());
		client.setBirthDate(clientDTO.getBirthDate());
		client.setChildren(clientDTO.getChildren());
	}
	
}
