package com.llye.test.restassuredtest.service;

import com.llye.test.restassuredtest.dto.CustomerDto;
import com.llye.test.restassuredtest.dto.CustomerRequestDto;
import com.llye.test.restassuredtest.entity.Customer;
import com.llye.test.restassuredtest.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<CustomerDto> create(CustomerRequestDto customerRequestDto) {
        Customer customer = customerRepository.save(Customer.builder()
                                                            .firstName(customerRequestDto.getFirstName())
                                                            .lastName(customerRequestDto.getLastName())
                                                            .build());
        return Optional.of(CustomerDto.toDto(customer));
    }

    public List<CustomerDto> findAll(int pageNumber, int pageSize) {
        LOG.info("Customers are fetched from DB.");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Customer> customerPage = customerRepository.findAll(pageRequest);
        List<Customer> customers = customerPage.getContent();
        if (customers.isEmpty()) {
            return Collections.emptyList();
        }
        return customers.stream()
                        .map(CustomerDto::toDto)
                        .toList();
    }
}
