package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

}
