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

import com.mars.app.person.domain.AddressEntity;
import com.mars.app.person.domain.PersonEntity;
import com.mars.app.person.model.Address;
import com.mars.app.person.repository.AddressRepository;
import com.mars.app.person.repository.PersonRepository;

@Service
public class AddressService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);
	
	@Autowired
	PersonRepository personRepository;

	@Autowired
	AddressRepository addressRepository;
	
	public Address getAddressById(int id) {
		try {
			AddressEntity addressEntity = findByAddressId(id);
			if(Objects.nonNull(addressEntity)) {
				Address address = new Address();
				BeanUtils.copyProperties(addressEntity, address);
				address.setPersonId(addressEntity.getPerson().getId());
				return address;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getAddressById():", e);
		}
		return null;
	}

	private AddressEntity findByAddressId(int id) {
		AddressEntity addressEntity = null;
		Optional<AddressEntity> optAddressEntity = addressRepository.findById(id);
		if(optAddressEntity.isPresent()) {
			addressEntity = optAddressEntity.get();
		}
		return addressEntity;
	}
	
	public List<Address> getAllAddresses() {
		List<Address> addresses = new ArrayList<>();
		try {
			List<AddressEntity> addressEntityList = addressRepository.findAll();
			addressEntityList.stream().forEach(addressEntity -> {
				Address address = new Address();
				BeanUtils.copyProperties(addressEntity, address);
				address.setPersonId(addressEntity.getPerson().getId());
				addresses.add(address);
			});
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getAllAddresses():", e);
		}
		return addresses;
	}
	
	public List<Address> getAllPersonAddresses(int personId) {
		List<Address> addresses = new ArrayList<>();
		try {
			List<AddressEntity> addressEntityList = addressRepository.findAll();
			addressEntityList.stream().filter(addressEntity -> addressEntity.getPerson().getId() == personId)
			.forEach(addressEntity -> {
				Address address = new Address();
				BeanUtils.copyProperties(addressEntity, address);
				address.setPersonId(personId);
				addresses.add(address);
			});
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getAllPersonAddresses():", e);
		}
		return addresses;
	}
	
	public boolean deletePersonAddress(int id) {
		try {
			addressRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception occurred in deletePersonAddress():", e);
			return false;
		}
	}
	
	public Address saveOrUpdatePersonAddress(Address address) {
		try {
			final AddressEntity addressEntity;
			if(Objects.nonNull(address.getId())) {
				addressEntity = findByAddressId(address.getId());
			} else {
				addressEntity = new AddressEntity();
				if(Objects.isNull(address.getPersonId())) {
					return null;
				}
				PersonEntity person = personRepository.findById(address.getPersonId()).get();
				addressEntity.setPerson(person);
			}
			BeanUtils.copyProperties(address, addressEntity, "id");
			addressRepository.save(addressEntity);
			BeanUtils.copyProperties(addressEntity, address);
			address.setPersonId(addressEntity.getPerson().getId());
			return address;
		} catch (Exception e) {
			LOGGER.error("Exception occurred in saveOrUpdatePersonAddress():", e);
		}
		return null;
	}
}
