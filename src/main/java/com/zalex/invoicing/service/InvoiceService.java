package com.zalex.invoicing.service;

import com.zalex.invoicing.model.Invoice;
import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);
    Invoice getInvoice(Long invoiceId);
    Invoice updateInvoice(Invoice invoice);
    void deleteInvoice(Long invoiceId);
    List<Invoice> getAllInvoices();
}
