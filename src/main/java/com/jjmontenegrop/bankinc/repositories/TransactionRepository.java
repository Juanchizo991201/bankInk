package com.jjmontenegrop.bankinc.repositories;

import com.jjmontenegrop.bankinc.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

}
