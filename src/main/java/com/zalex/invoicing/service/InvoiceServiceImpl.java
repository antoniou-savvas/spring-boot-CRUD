package com.zalex.invoicing.service;

import com.zalex.invoicing.exception.ResourceNotFoundException;
import com.zalex.invoicing.model.Customer;
import com.zalex.invoicing.model.Invoice;
import com.zalex.invoicing.repository.CustomerRepository;
import com.zalex.invoicing.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        final Customer invoiceCustomer = customerRepository.findById(invoice.getCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + invoice.getCustomer().getCustomerId()));

        invoice.setCustomer(invoiceCustomer);

        invoice.getItems().forEach(item -> {
            item.setProductName(item.getProduct().getName());
            item.calculateAmount();
            item.setInvoice(invoice);
        });

        invoice.calculateTotalAmount();
        invoice.setDateCreated(LocalDate.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);

        invoiceCustomer.calculateAccountBalance();
        customerRepository.save(invoiceCustomer);

        return savedInvoice;
    }

    @Override
    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id " + invoiceId));
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        final Customer invoiceCustomer = customerRepository.findById(invoice.getCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + invoice.getCustomer().getCustomerId()));

        invoice.setCustomer(invoiceCustomer);

        invoice.getItems().forEach(item -> {
            item.setProductName(item.getProduct().getName());
            item.calculateAmount();
            item.setInvoice(invoice);
        });

        invoice.calculateTotalAmount();

        Invoice updatedInvoice = invoiceRepository.save(invoice);

        invoiceCustomer.calculateAccountBalance();
        customerRepository.save(invoiceCustomer);

        return updatedInvoice;
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id " + invoiceId));
        Customer customer = invoice.getCustomer();
        invoiceRepository.deleteById(invoiceId);

        if (customer != null) {
            customer.calculateAccountBalance();
            customerRepository.save(customer);
        }
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}
