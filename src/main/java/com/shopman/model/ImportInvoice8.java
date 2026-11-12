package com.shopman.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImportInvoice8 {
    private Long id;
    private String importCode;
    private LocalDateTime importDate;
    private BigDecimal totalAmount;
    private Long supplierId;
    private Long createdByManagerId;
    private List<ImportInvoiceDetail8> details = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImportCode() { return importCode; }
    public void setImportCode(String importCode) { this.importCode = importCode; }
    public LocalDateTime getImportDate() { return importDate; }
    public void setImportDate(LocalDateTime importDate) { this.importDate = importDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getCreatedByManagerId() { return createdByManagerId; }
    public void setCreatedByManagerId(Long createdByManagerId) { this.createdByManagerId = createdByManagerId; }
    public List<ImportInvoiceDetail8> getDetails() { return details; }
    public void setDetails(List<ImportInvoiceDetail8> details) { this.details = details; }
}
