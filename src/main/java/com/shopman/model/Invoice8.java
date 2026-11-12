package com.shopman.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice8 {
    private Long id;
    private String invoiceCode;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String status;
    private Long customerId;
    private Long approvedBySalesStaffId;
    private Long deliveryStaffId;
    private List<InvoiceDetail8> details = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvoiceCode() { return invoiceCode; }
    public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getApprovedBySalesStaffId() { return approvedBySalesStaffId; }
    public void setApprovedBySalesStaffId(Long approvedBySalesStaffId) { this.approvedBySalesStaffId = approvedBySalesStaffId; }
    public Long getDeliveryStaffId() { return deliveryStaffId; }
    public void setDeliveryStaffId(Long deliveryStaffId) { this.deliveryStaffId = deliveryStaffId; }
    public List<InvoiceDetail8> getDetails() { return details; }
    public void setDetails(List<InvoiceDetail8> details) { this.details = details; }
}
