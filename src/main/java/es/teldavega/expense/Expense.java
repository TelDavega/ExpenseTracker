package es.teldavega.expense;

import java.math.BigDecimal;
import java.util.Date;

public class Expense {
    private int id;
    private String description;
    private BigDecimal amount;
    private Date date;
    private String category;

    public Expense(int id, String description, BigDecimal amount, Date date, String category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category.toUpperCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return Character.toUpperCase(category.charAt(0)) + category.substring(1).toLowerCase();
    }

    public void setCategory(String category) {
        this.category = category.toUpperCase();
    }

    @Override
    public String toString() {
        return id + "\t" + date + "\t" + description + "\t" + category + "\t$" + amount;
    }
}
