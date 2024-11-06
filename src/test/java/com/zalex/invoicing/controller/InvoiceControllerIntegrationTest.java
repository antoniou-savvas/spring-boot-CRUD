package com.zalex.invoicing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalex.invoicing.model.Customer;
import com.zalex.invoicing.model.Invoice;
import com.zalex.invoicing.model.InvoiceItem;
import com.zalex.invoicing.model.Product;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InvoiceControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private URI baseUri;

    private Customer customer;
    private Product product1;
    private Product product2;
    private Invoice invoice;

    @SuppressWarnings("null")
    @BeforeAll
    public void setUp() throws URISyntaxException {
        baseUri = new URI("http://localhost:" + port + "/invoices");

        customer = new Customer();
        customer.setFirstName(RandomStringUtils.randomAlphabetic(6));
        customer.setLastName(RandomStringUtils.randomAlphabetic(6));
        customer.setAddress(RandomStringUtils.randomAlphanumeric(10));
        customer.setPhone(RandomStringUtils.randomNumeric(10));

        ResponseEntity<Customer> customerResponse = restTemplate.postForEntity(new URI("http://localhost:" + port + "/customers"), customer, Customer.class);
        assertNotNull(customerResponse.getBody());
        customer.setCustomerId(customerResponse.getBody().getCustomerId());

        product1 = new Product();
        product1.setName("Kamado 1000");
        product1.setModelNo("K1000");
        product1.setDescription("Kamado grill model 1000");
        product1.setPrice(BigDecimal.valueOf(199).setScale(2, RoundingMode.UNNECESSARY));

        ResponseEntity<Product> productResponse1 = restTemplate.postForEntity(new URI("http://localhost:" + port + "/products"), product1, Product.class);
        assertNotNull(productResponse1.getBody());
        product1.setProductId(productResponse1.getBody().getProductId());

        product2 = new Product();
        product2.setName("Kamado 2000");
        product2.setModelNo("K2000");
        product2.setDescription("Kamado grill model 2000");
        product2.setPrice(BigDecimal.valueOf(299).setScale(2, RoundingMode.UNNECESSARY));

        ResponseEntity<Product> productResponse2 = restTemplate.postForEntity(new URI("http://localhost:" + port + "/products"), product2, Product.class);
        assertNotNull(productResponse2.getBody());
        product2.setProductId(productResponse2.getBody().getProductId());

        invoice = new Invoice();
        invoice.setCustomer(customer);
        InvoiceItem item1 = new InvoiceItem();
        item1.setProduct(product1);
        item1.setQuantity(2);
        InvoiceItem item2 = new InvoiceItem();
        item2.setProduct(product2);
        item2.setQuantity(1);
        invoice.setItems(Arrays.asList(item1, item2));
    }

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateInvoice() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String invoiceJson = objectMapper.writeValueAsString(invoice);
        System.out.println("Sending JSON: " + invoiceJson);

        ResponseEntity<Invoice> response = restTemplate.postForEntity(baseUri, invoice, Invoice.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getCustomer().getCustomerId()).isEqualTo(customer.getCustomerId());
        assertThat(response.getBody().getItems().size()).isEqualTo(2);

        invoice.setInvoiceId(response.getBody().getInvoiceId());
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetInvoiceById() throws URISyntaxException {
        ResponseEntity<Invoice> createdResponse = restTemplate.postForEntity(baseUri, invoice, Invoice.class);
        assertNotNull(createdResponse.getBody());

        invoice.setInvoiceId(createdResponse.getBody().getInvoiceId());

        URI getUri = new URI(baseUri + "/" + invoice.getInvoiceId());
        ResponseEntity<Invoice> response = restTemplate.getForEntity(getUri, Invoice.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getCustomer().getCustomerId()).isEqualTo(customer.getCustomerId());
        assertThat(response.getBody().getItems().size()).isEqualTo(2);
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testUpdateInvoice() throws URISyntaxException {
        ResponseEntity<Invoice> createdResponse = restTemplate.postForEntity(baseUri, invoice, Invoice.class);
        assertNotNull(createdResponse.getBody());

        invoice.setInvoiceId(createdResponse.getBody().getInvoiceId());

        InvoiceItem updatedItem = new InvoiceItem();
        updatedItem.setProduct(product2);
        updatedItem.setQuantity(3);
        invoice.setItems(Arrays.asList(updatedItem));

        URI uri = new URI(baseUri + "/" + invoice.getInvoiceId());

        restTemplate.put(uri, invoice);

        ResponseEntity<Invoice> response = restTemplate.getForEntity(uri, Invoice.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getItems().size()).isEqualTo(1);
        assertThat(response.getBody().getItems().get(0).getQuantity()).isEqualTo(3);
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testDeleteInvoice() {
        ResponseEntity<Invoice> createdResponse = restTemplate.postForEntity(baseUri, invoice, Invoice.class);
        assertNotNull(createdResponse.getBody());

        invoice.setInvoiceId(createdResponse.getBody().getInvoiceId());

        restTemplate.delete(baseUri.resolve("/" + invoice.getInvoiceId()));

        ResponseEntity<Invoice> response = restTemplate.getForEntity(baseUri.resolve("/" + invoice.getInvoiceId()), Invoice.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
