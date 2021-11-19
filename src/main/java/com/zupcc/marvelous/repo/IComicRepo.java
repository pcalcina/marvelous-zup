package com.zupcc.marvelous.repo;

import com.zupcc.marvelous.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IComicRepo extends JpaRepository<Comic, Long> {

}