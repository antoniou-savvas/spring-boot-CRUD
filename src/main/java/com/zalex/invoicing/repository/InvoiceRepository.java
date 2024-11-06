package com.zalex.invoicing.repository;

import com.zalex.invoicing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
