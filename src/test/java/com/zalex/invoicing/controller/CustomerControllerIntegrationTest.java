package com.zalex.invoicing.controller;

import com.zalex.invoicing.model.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private URI baseUri;

    private Customer customer;

    private String randomFirstName;
    private String randomLastName;
    private String randomAddress;
    private String randomPhone;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        baseUri = new URI("http://localhost:" + port + "/customers");

        randomFirstName = RandomStringUtils.randomAlphabetic(6);
        randomLastName = RandomStringUtils.randomAlphabetic(6);
        randomAddress = RandomStringUtils.randomAlphanumeric(10);
        randomPhone = RandomStringUtils.randomNumeric(10);

        customer = new Customer();
        customer.setFirstName(randomFirstName);
        customer.setLastName(randomLastName);
        customer.setAddress(randomAddress);
        customer.setPhone(randomPhone);
    }

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateCustomer() {
        ResponseEntity<Customer> response = restTemplate.postForEntity(baseUri, customer, Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getFirstName()).isEqualTo(randomFirstName);
        assertThat(response.getBody().getLastName()).isEqualTo(randomLastName);
        assertThat(response.getBody().getAddress()).isEqualTo(randomAddress);
        assertThat(response.getBody().getPhone()).isEqualTo(randomPhone);

        customer.setCustomerId(response.getBody().getCustomerId());
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetCustomerById() throws URISyntaxException {
        ResponseEntity<Customer> createdResponse = restTemplate.postForEntity(baseUri, customer, Customer.class);
        assertNotNull(createdResponse.getBody());
        
        customer.setCustomerId(createdResponse.getBody().getCustomerId());

        URI getUri = new URI(baseUri + "/" + customer.getCustomerId());
        ResponseEntity<Customer> response = restTemplate.getForEntity(getUri, Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getFirstName()).isEqualTo(randomFirstName);
        assertThat(response.getBody().getLastName()).isEqualTo(randomLastName);
        assertThat(response.getBody().getAddress()).isEqualTo(randomAddress);
        assertThat(response.getBody().getPhone()).isEqualTo(randomPhone);

    }


    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testUpdateCustomer() throws URISyntaxException {
        ResponseEntity<Customer> createdResponse = restTemplate.postForEntity(baseUri, customer, Customer.class);
        assertNotNull(createdResponse.getBody());
        
        customer.setCustomerId(createdResponse.getBody().getCustomerId());
    
        String updatedFirstName = RandomStringUtils.randomAlphabetic(6);
        customer.setFirstName("UPD" + updatedFirstName);
    
        URI uri = new URI(baseUri + "/" + customer.getCustomerId());
        
        restTemplate.put(uri, customer);
    
        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);
    
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getFirstName()).isEqualTo("UPD" + updatedFirstName);
        assertThat(response.getBody().getLastName()).isEqualTo(randomLastName);
        assertThat(response.getBody().getAddress()).isEqualTo(randomAddress);
        assertThat(response.getBody().getPhone()).isEqualTo(randomPhone);
    }
    

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testDeleteCustomer() {
        ResponseEntity<Customer> createdResponse = restTemplate.postForEntity(baseUri, customer, Customer.class);
        assertNotNull(createdResponse.getBody());

        customer.setCustomerId(createdResponse.getBody().getCustomerId());

        restTemplate.delete(baseUri.resolve("/" + customer.getCustomerId()));

        ResponseEntity<Customer> response = restTemplate.getForEntity(baseUri.resolve("/" + customer.getCustomerId()), Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
