package com.zalex.invoicing.controller;

import com.zalex.invoicing.model.Product;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private URI baseUri;

    private Product product;

    private String randomName;
    private String randomModelNo;
    private String randomDescription;
    private BigDecimal randomPrice;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        baseUri = new URI("http://localhost:" + port + "/products");

        randomName = RandomStringUtils.randomAlphabetic(6);
        randomModelNo = RandomStringUtils.randomAlphanumeric(6);
        randomDescription = RandomStringUtils.randomAlphanumeric(10);
        randomPrice = BigDecimal.valueOf(Double.parseDouble(RandomStringUtils.randomNumeric(3))).setScale(2, RoundingMode.UNNECESSARY);

        product = new Product();
        product.setName(randomName);
        product.setModelNo(randomModelNo);
        product.setDescription(randomDescription);
        product.setPrice(randomPrice);
    }

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateProduct() {
        ResponseEntity<Product> response = restTemplate.postForEntity(baseUri, product, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getName()).isEqualTo(randomName);
        assertThat(response.getBody().getModelNo()).isEqualTo(randomModelNo);
        assertThat(response.getBody().getDescription()).isEqualTo(randomDescription);
        assertThat(response.getBody().getPrice().setScale(2, RoundingMode.UNNECESSARY)).isEqualTo(randomPrice);

        product.setProductId(response.getBody().getProductId());
    }

    @Test
    @Order(2)
    public void testGetAllProducts() {
        ResponseEntity<List<Product>> response = restTemplate.exchange(
            baseUri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Product>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody()).isNotEmpty();
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetProductById() throws URISyntaxException {
        ResponseEntity<Product> createdResponse = restTemplate.postForEntity(baseUri, product, Product.class);
        assertNotNull(createdResponse.getBody());
        
        product.setProductId(createdResponse.getBody().getProductId());

        URI getUri = new URI(baseUri + "/" + product.getProductId());
        ResponseEntity<Product> response = restTemplate.getForEntity(getUri, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getName()).isEqualTo(randomName);
        assertThat(response.getBody().getModelNo()).isEqualTo(randomModelNo);
        assertThat(response.getBody().getDescription()).isEqualTo(randomDescription);
        assertThat(response.getBody().getPrice().setScale(2, RoundingMode.UNNECESSARY)).isEqualTo(randomPrice);
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateProduct() throws URISyntaxException {
        ResponseEntity<Product> createdResponse = restTemplate.postForEntity(baseUri, product, Product.class);
        assertNotNull(createdResponse.getBody());
        
        product.setProductId(createdResponse.getBody().getProductId());

        String updatedName = RandomStringUtils.randomAlphabetic(6);
        product.setName("UPD" + updatedName);

        URI uri = new URI(baseUri + "/" + product.getProductId());
        
        restTemplate.put(uri, product);

        ResponseEntity<Product> response = restTemplate.getForEntity(uri, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getName()).isEqualTo("UPD" + updatedName);
        assertThat(response.getBody().getModelNo()).isEqualTo(randomModelNo);
        assertThat(response.getBody().getDescription()).isEqualTo(randomDescription);
        assertThat(response.getBody().getPrice().setScale(2, RoundingMode.UNNECESSARY)).isEqualTo(randomPrice);
    }

    @SuppressWarnings("null")
    @Test
    @Order(5)
    public void testDeleteProduct() {
        ResponseEntity<Product> createdResponse = restTemplate.postForEntity(baseUri, product, Product.class);
        assertNotNull(createdResponse.getBody());

        product.setProductId(createdResponse.getBody().getProductId());

        restTemplate.delete(baseUri.resolve("/" + product.getProductId()));

        ResponseEntity<Product> response = restTemplate.getForEntity(baseUri.resolve("/" + product.getProductId()), Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
