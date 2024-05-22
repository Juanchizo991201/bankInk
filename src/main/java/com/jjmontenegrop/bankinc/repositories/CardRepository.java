package com.jjmontenegrop.bankinc.repositories;

import com.jjmontenegrop.bankinc.entities.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, String> {}
