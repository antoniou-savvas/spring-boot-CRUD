package com.zalex.invoicing.service;

import com.zalex.invoicing.exception.ResourceNotFoundException;
import com.zalex.invoicing.model.Customer;
import com.zalex.invoicing.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customer);

        assertNotNull(createdCustomer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomer(1L);

        assertNotNull(foundCustomer);
        assertEquals(1L, foundCustomer.getCustomerId());
    }

    @Test
    public void testGetCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(1L));
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer updatedCustomer = customerService.updateCustomer(customer);

        assertNotNull(updatedCustomer);
        assertEquals(1L, updatedCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testDeleteCustomer() {
        customerService.deleteCustomer(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllCustomers() {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(2, customers.size());
    }
}
