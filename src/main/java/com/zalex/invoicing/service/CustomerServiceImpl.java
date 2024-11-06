package com.zalex.invoicing.service;

import com.zalex.invoicing.exception.ResourceNotFoundException;
import com.zalex.invoicing.model.Customer;
import com.zalex.invoicing.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        customer.calculateAccountBalance();
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
        customer.calculateAccountBalance();
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        customer.calculateAccountBalance();
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(Customer::calculateAccountBalance);
        return customers;
    }
}
