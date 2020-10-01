package com.mars.app.person.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.app.person.model.Address;
import com.mars.app.person.model.Person;
import com.mars.app.person.rest.controller.PersonRestController;
import com.mars.app.person.service.AddressService;
import com.mars.app.person.service.PersonService;

@WebMvcTest(controllers = PersonRestController.class)
@ActiveProfiles("test")
public class PersonRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;
    
    @MockBean
    private AddressService addressService;
    
    @Autowired
    private ObjectMapper objectMapper;

    private List<Person> personList;
    
    @BeforeEach
    void setUp() {
        this.personList = new ArrayList<>();
        this.personList.add(new Person(1, "Venkat", "Cherukumilli"));
        this.personList.add(new Person(2, "Ram", "CH"));
        this.personList.add(new Person(3, "Kishore", "K"));
    }
    
    @Test
    void testFetchAllPersons() throws Exception {
        given(personService.getAllPersons()).willReturn(personList);
        this.mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(personList.size())));
    }
    
    @Test
    void testFetchPersonById() throws Exception {
        final Integer personId = 1;
        final Person person = new Person(1, "Venkat", "Cherukumilli");

        given(personService.getPersonById(personId)).willReturn(person);

        this.mockMvc.perform(get("/id/{id}", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())));
    }
    
    @Test
    void testReturn404WhenFindPersonById() throws Exception {
    	final Integer personId = 1;
        given(personService.getPersonById(personId)).willReturn(null);

        this.mockMvc.perform(get("/id/{id}", personId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateNewPerson() throws Exception {
        given(personService.saveOrUpdatePerson(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        Person person = new Person(null, "Uday", "Cherukumilli");
        this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())));
    }
    
    @Test
    void testUpdatePerson() throws Exception {
        Integer personId = 1;
        Person person = new Person(personId, "Uday", "Cherukumilli");
        given(personService.getPersonById(personId)).willReturn(person);
        given(personService.saveOrUpdatePerson(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc.perform(post("/create", person.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())));

    }
    
    @Test
    void testDeleteUser() throws Exception {
    	Integer personId = 1;
    	Person person = new Person(personId, "Uday", "Cherukumilli");
        given(personService.getPersonById(personId)).willReturn(person);
        given(personService.delete(person.getId())).willReturn(true);

        this.mockMvc.perform(delete("/remove/{id}", person.getId()))
                .andExpect(status().isOk());
    }
    
    @Test
    void testReturn404WhenDeletingNonExistingPerson() throws Exception {
    	Integer personId = 1;
        given(personService.getPersonById(personId)).willReturn(null);

        this.mockMvc.perform(delete("/remove/{id}", personId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateNewPersonWithAddress() throws Exception {
        given(personService.saveOrUpdatePerson(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("street", "city", "state", "123456"));
        addresses.add(new Address("street1", "city1", "state1", "123456"));
        
        Person person = new Person("Venkat", "Cherukumilli", addresses);
        this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.address.size()", is(person.getAddress().size())));
    }
    
    @Test
    void testUpdateExistingPersonWithAddress() throws Exception {
    	Integer personId = 1;
    	List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("street", "city", "state", "123456"));
        addresses.add(new Address("street1", "city1", "state1", "123456"));
    	Person person = new Person(personId, "Venkat", "Cherukumilli", addresses);
        given(personService.getPersonById(personId)).willReturn(person);
        given(personService.saveOrUpdatePerson(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        
        this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.address.size()", is(person.getAddress().size())));
    }
    
}
