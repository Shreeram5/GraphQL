package com.augustine.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augustine.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer>{

	Person findByEmail(String email);

}
