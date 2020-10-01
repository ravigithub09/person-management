package com.mars.app.person.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mars.app.person.domain.AddressEntity;
import com.mars.app.person.domain.PersonEntity;
import com.mars.app.person.model.Address;
import com.mars.app.person.model.Person;
import com.mars.app.person.repository.AddressRepository;
import com.mars.app.person.repository.PersonRepository;

@Service
public class PersonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	public Person saveOrUpdatePerson(Person person) {
		try {
			final PersonEntity personEntity;
			if(Objects.isNull(person.getId())) {
				personEntity = new PersonEntity();
			} else {
				personEntity = personRepository.findById(person.getId()).get();
			}
			BeanUtils.copyProperties(person, personEntity, "id");
			personRepository.save(personEntity);			
			saveOrUpdateAddress(person, personEntity);
			BeanUtils.copyProperties(personEntity, person);
			return person;
		} catch (Exception e) {
			LOGGER.error("Exception occurred in saveOrUpdatePerson():", e);
		}
		return null;
	}

	private void saveOrUpdateAddress(Person person, final PersonEntity personEntity) {
		if(!CollectionUtils.isEmpty(person.getAddress())) {
			person.getAddress().stream().forEach(address -> {
				AddressEntity addressEntity = null;
				if(CollectionUtils.isEmpty(personEntity.getAddress())) {
					addressEntity = new AddressEntity();
				} else {
					addressEntity = addressRepository.findById(address.getId()).get();
				}
				addressEntity.setPerson(personEntity);
				BeanUtils.copyProperties(address, addressEntity, "id");
				addressRepository.save(addressEntity);
			});
		}
	}
	
	public Person getPersonById(int id) {
		try {
			Optional<PersonEntity> personEntity = personRepository.findById(id);
			if(personEntity.isPresent()) {
				Person person = new Person();
				BeanUtils.copyProperties(personEntity.get(), person);
				return person;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getPersonById():", e);
		}
		return null;
	}
	
	public List<Person> getAllPersons() {
		List<Person> persons = new ArrayList<>();
		try {
			List<PersonEntity> personEntityList = personRepository.findAll();
			personEntityList.stream().forEach(personEntity -> {
				Person person = new Person();
				List<Address> addresses = new ArrayList<>();
				if(!CollectionUtils.isEmpty(personEntity.getAddress())) {
					personEntity.getAddress().stream().forEach(addressEntity -> {
						Address address = new Address();
						BeanUtils.copyProperties(addressEntity, address);
						address.setPersonId(personEntity.getId());
						addresses.add(address);
					});
				}
				person.setAddress(addresses);
				BeanUtils.copyProperties(personEntity, person);
				persons.add(person);
			});
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getAllPersons():", e);
		}
		return persons;
	}
	
	public boolean delete(int id) {
		try {
			personRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception occurred in delete:", e);
			return false;
		}
	}
	
	public List<Address> getAllPersonAddresses(int personId) {
		List<Address> addresses = new ArrayList<>();
		try {
			Person person = getPersonById(personId);
			return person.getAddress();
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getAllPersonAddresses():", e);
		}
		return addresses;
	}
}
