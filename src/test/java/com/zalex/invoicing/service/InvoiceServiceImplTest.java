package com.zalex.invoicing.service;

import com.zalex.invoicing.exception.ResourceNotFoundException;
import com.zalex.invoicing.model.Customer;
import com.zalex.invoicing.model.Invoice;
import com.zalex.invoicing.model.InvoiceItem;
import com.zalex.invoicing.model.Product;
import com.zalex.invoicing.repository.CustomerRepository;
import com.zalex.invoicing.repository.InvoiceRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Customer mockCustomer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCustomer = new Customer();
        mockCustomer.setCustomerId(1L);
        mockCustomer.setCustomerId(1L);
        mockCustomer.setFirstName(RandomStringUtils.randomAlphabetic(6));
        mockCustomer.setLastName(RandomStringUtils.randomAlphabetic(6));
    }

    @Test
    public void testCreateInvoice() {
        Product product = new Product();
        product.setProductId(1L);
        product.setPrice(BigDecimal.valueOf(50));

        Invoice invoice = new Invoice();
        invoice.setCustomer(mockCustomer);
        InvoiceItem item = new InvoiceItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setInvoice(invoice);
        invoice.setItems(Arrays.asList(item));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);

        Invoice createdInvoice = invoiceService.createInvoice(invoice);

        assertNotNull(createdInvoice);
        assertEquals(BigDecimal.valueOf(100), createdInvoice.getTotalAmount());
        verify(invoiceRepository, times(1)).save(invoice);
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    public void testGetInvoice() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1L);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.getInvoice(1L);

        assertNotNull(foundInvoice);
        assertEquals(1L, foundInvoice.getInvoiceId());
    }

    @Test
    public void testGetInvoice_NotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> invoiceService.getInvoice(1L));
    }

    @Test
    public void testUpdateInvoice() {
        Product product = new Product();
        product.setProductId(1L);
        product.setPrice(BigDecimal.valueOf(50));

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1L);
        invoice.setCustomer(mockCustomer);
        InvoiceItem item = new InvoiceItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setInvoice(invoice);
        invoice.setItems(Arrays.asList(item));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer)); // Ensure findById is mocked
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);

        Invoice updatedInvoice = invoiceService.updateInvoice(invoice);

        assertNotNull(updatedInvoice);
        assertEquals(BigDecimal.valueOf(100), updatedInvoice.getTotalAmount());
        verify(invoiceRepository, times(1)).save(invoice);
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    public void testDeleteInvoice() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1L);
        invoice.setCustomer(mockCustomer);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        invoiceService.deleteInvoice(1L);

        verify(invoiceRepository, times(1)).deleteById(1L);
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    public void testGetAllInvoices() {
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();
        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.getAllInvoices();

        assertNotNull(invoices);
        assertEquals(2, invoices.size());
    }
}
