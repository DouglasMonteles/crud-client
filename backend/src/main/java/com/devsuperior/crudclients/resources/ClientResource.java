package com.devsuperior.crudclients.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.crudclients.dtos.ClientDTO;
import com.devsuperior.crudclients.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {

	@Autowired
	private ClientService clientService;
	
	@GetMapping
	public ResponseEntity<Page<ClientDTO>> findAllPageable(
				@RequestParam(name = "page", defaultValue = "0") Integer page,
				@RequestParam(name = "linesPerPage", defaultValue = "12") Integer linesPerPage,
				@RequestParam(name = "direction", defaultValue = "DESC") String direction,
				@RequestParam(name = "orderBy", defaultValue = "name") String orderBy
			) {
		var pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		var clientsPageDTO = this.clientService.findAllPageable(pageRequest);
		
		return ResponseEntity.ok().body(clientsPageDTO);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
		var clientDTO = this.clientService.findById(id);
		return ResponseEntity.ok().body(clientDTO);
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody ClientDTO clientDTO) {
		clientDTO = this.clientService.insert(clientDTO);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(clientDTO.getId())
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
		this.clientService.update(id, clientDTO);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		this.clientService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
