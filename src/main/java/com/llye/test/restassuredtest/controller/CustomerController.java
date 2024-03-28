package com.llye.test.restassuredtest.controller;

import com.llye.test.restassuredtest.dto.CustomerDto;
import com.llye.test.restassuredtest.dto.CustomerRequestDto;
import com.llye.test.restassuredtest.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        Optional<CustomerDto> maybeCustomer = customerService.create(customerRequestDto);
        return maybeCustomer.map(customer -> new ResponseEntity<>(customer, HttpStatus.CREATED))
                            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping
    private ResponseEntity<List<CustomerDto>> getAllCustomers(@RequestParam(defaultValue = "0") int pageNumber,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        List<CustomerDto> customerDtos = customerService.findAll(pageNumber, pageSize);
        if (CollectionUtils.isEmpty(customerDtos)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customerDtos, HttpStatus.OK);
    }
}
