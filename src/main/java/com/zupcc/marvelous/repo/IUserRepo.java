package com.zupcc.marvelous.repo;

import com.zupcc.marvelous.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepo extends JpaRepository<Person, Long> {

    @Query("select u from Person u where u.email = ?1")
    Person findByEmail(String email);
}
