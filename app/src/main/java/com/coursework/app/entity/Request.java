package com.coursework.app.entity;

import java.time.LocalDate;

public class Request {
    private int requestId;
    private final SalePoint salePoint;
    private final boolean isProcessed;
    private final LocalDate creationDate;
    private LocalDate completeDate;

    public Request(SalePoint salePoint, boolean isProcessed, LocalDate creationDate) {
        this.salePoint = salePoint;
        this.isProcessed = isProcessed;
        this.creationDate = creationDate;
    }

    public Request(int requestId, SalePoint salePoint, boolean isProcessed, LocalDate creationDate, LocalDate completeDate) {
        this.requestId = requestId;
        this.salePoint = salePoint;
        this.isProcessed = isProcessed;
        this.creationDate = creationDate;
        this.completeDate = completeDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDate completeDate) {
        this.completeDate = completeDate;
    }
}
