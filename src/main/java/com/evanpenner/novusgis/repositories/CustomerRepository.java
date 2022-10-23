package com.evanpenner.novusgis.repositories;

import com.evanpenner.novusgis.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByNameEqualsIgnoreCase(String name);
}
