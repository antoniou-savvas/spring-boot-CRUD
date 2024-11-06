package com.zalex.invoicing.service;

import com.zalex.invoicing.model.Customer;
import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomer(Long customerId);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Long customerId);
    List<Customer> getAllCustomers();
}
