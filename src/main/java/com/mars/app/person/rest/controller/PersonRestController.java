package com.mars.app.person.rest.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mars.app.person.model.Address;
import com.mars.app.person.model.Person;
import com.mars.app.person.service.AddressService;
import com.mars.app.person.service.PersonService;

@RestController
public class PersonRestController {

	@Autowired
	PersonService personService;
	
	@Autowired
	AddressService addressService;

	@PostMapping("/create")
	public ResponseEntity<Object> saveOrUpdatePerson(@RequestBody Person person) {
		boolean isNew = false;
		if(Objects.isNull(person.getId())) {
			isNew = true;
		}
		person = personService.saveOrUpdatePerson(person);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/id/{id}")
				.buildAndExpand(person.getId()).toUri();
		if(Objects.nonNull(person)) {
			if(isNew) {
				return ResponseEntity.created(location).body(person);
			} else {
				return ResponseEntity.ok(person);
			}
		}
		return new ResponseEntity<>("Unable to add person", HttpStatus.EXPECTATION_FAILED);
	}
	
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> removePerson(@PathVariable("id") int id) {
		if(personService.delete(id)) {
			return new ResponseEntity<>("Person deleted successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Unable to delete person", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
		Person person = personService.getPersonById(id);
		if(Objects.nonNull(person)) {
			return new ResponseEntity<>(person, HttpStatus.OK);
		}
		return new ResponseEntity<>(person, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/all")
	public List<Person> getAllPersons() {
		return personService.getAllPersons();
	}
	
	@GetMapping("/count")
	public int getPersonsCount() {
		return getAllPersons().size();
	}
	
	@PostMapping("/address")
	public ResponseEntity<Object> saveOrUpdateAddress(@RequestBody Address address) {
		boolean isNew = false;
		if(Objects.isNull(address.getId())) {
			isNew = true;
		}
		address = addressService.saveOrUpdatePersonAddress(address);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{personId}/address/{id}")
				.buildAndExpand(address.getPersonId(), address.getId()).toUri();
		if(Objects.nonNull(address)) {
			if(isNew) {
				return ResponseEntity.created(location).body(address);
			} else {
				return ResponseEntity.ok(address);
			}
		}
		return new ResponseEntity<>("Unable to add or update address", HttpStatus.EXPECTATION_FAILED);
	}
	
	@GetMapping("/{personId}/address/{id}")
	public Address getAddressById(@PathVariable("personId") int personId, @PathVariable("id") int id) {
		return addressService.getAddressById(id);		
	}
	
	@GetMapping("/personAddresses/{personId}")
	public List<Address> getAllPersonAddresses(@PathVariable("personId") int personId) {
		return addressService.getAllPersonAddresses(personId);
	}
	
	@DeleteMapping("/address/remove/{id}")
	public ResponseEntity<String> removeAddress(@PathVariable("id") int id) {
		if(addressService.deletePersonAddress(id)) {
			return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Unable to delete address", HttpStatus.BAD_REQUEST);
	}
}
