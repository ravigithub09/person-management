package com.mars.app.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mars.app.person.domain.PersonEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

}
