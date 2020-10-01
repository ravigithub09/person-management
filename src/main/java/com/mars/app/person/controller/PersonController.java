package com.mars.app.person.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mars.app.person.model.Address;
import com.mars.app.person.model.Person;
import com.mars.app.person.service.AddressService;
import com.mars.app.person.service.PersonService;

@Controller
public class PersonController {

	@Autowired
	PersonService personService;

	@Autowired
	AddressService addressService;

	@GetMapping("/")
	public String getPersons(Model model) {
		model.addAttribute("persons", getAllPersons());
		model.addAttribute("personsCount", getAllPersons().size());
		return "person";
	}
	
	private List<Person> getAllPersons() {
		return personService.getAllPersons();
	}

	@PostMapping("/addEditPerson")
	public String addPerson(Person person, Model model) {
		personService.saveOrUpdatePerson(person);
		model.addAttribute("persons", getAllPersons());
		return "redirect:/";
	}

	@GetMapping(path = {"/add", "/edit/{id}"})
	public String editPersonById(Model model, @PathVariable("id") Optional<Integer> id) {
		if (id.isPresent()) {
			Person person = personService.getPersonById(id.get());
			model.addAttribute("person", person);
		} else {
			model.addAttribute("person", new Person());
		}
		return "add-edit-person";
	}
	
	@RequestMapping("/delete/{id}")
	public String deletePerson(Model model, @PathVariable("id") int id) {
		personService.delete(id);
		return "redirect:/";		
	}
	
	@GetMapping("/address/{personId}")
	public String getPersonAddresses(Model model, @PathVariable("personId") Optional<Integer> personId) {
		model.addAttribute("addresses", addressService.getAllPersonAddresses(personId.get()));
		model.addAttribute("personId", personId.get());
		return "person-address";
	}
	
	@GetMapping(path = {"/addPersonAddress/{personId}"})
	public String addPersonAddress(Model model, @PathVariable("personId") Optional<Integer> personId) {
		Address address = new Address();
		address.setPersonId(personId.get());
		model.addAttribute("address", address);
		return "add-edit-person-address";
	}
	
	@GetMapping(path = {"/editAddress/{id}"})
	public String editPersonAddress(Model model, @PathVariable("id") Optional<Integer> id) {
		Address address = addressService.getAddressById(id.get());
		model.addAttribute("address", address);
		return "add-edit-person-address";
	}
	
	@PostMapping("/addEditAddress")
	public String addEditAddress(Address address, Model model) {
		addressService.saveOrUpdatePersonAddress(address);
		return "redirect:/address/" + address.getPersonId();
	}
	
	@RequestMapping("/address/delete/{id}")
	public String deleteAddress(Model model, @PathVariable("id") int id) {
		Address address = addressService.getAddressById(id);
		addressService.deletePersonAddress(id);
		return "redirect:/address/" + address.getPersonId();
	}

}
