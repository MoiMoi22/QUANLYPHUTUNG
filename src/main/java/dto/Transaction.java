package dto;

import java.sql.Date;

public class Transaction {
    private int transactionId;
    private Date transactionDate;
    private String transactionType;
    private float amount;
    private String title;
    private String description;
    private int accountantId;
    private int relatedOrderId;

    // Constructor không tham số
    public Transaction() {}

    // Constructor có tham số
    public Transaction(int transactionId, Date transactionDate, String transactionType, float amount, String title, String description, int accountantId, int relatedOrderId) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.amount = amount;
        this.title = title;
        this.description = description;
        this.accountantId = accountantId;
        this.relatedOrderId = relatedOrderId;
    }

    // Getter và Setter cho từng thuộc tính
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAccountantId() {
        return accountantId;
    }

    public void setAccountantId(int accountantId) {
        this.accountantId = accountantId;
    }

    public int getRelatedOrderId() {
        return relatedOrderId;
    }

    public void setRelatedOrderId(int relatedOrderId) {
        this.relatedOrderId = relatedOrderId;
    }
}
