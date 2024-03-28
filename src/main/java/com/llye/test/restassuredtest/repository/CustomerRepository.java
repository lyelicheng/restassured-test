package com.llye.test.restassuredtest.repository;


import com.llye.test.restassuredtest.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
