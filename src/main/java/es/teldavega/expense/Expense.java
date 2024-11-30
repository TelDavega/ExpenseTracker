package es.teldavega.expense;

import java.math.BigDecimal;
import java.util.Date;

public class Expense {
    private int id;
    private String description;
    private BigDecimal amount;
    private Date date;

    public Expense(int id, String description, BigDecimal amount, Date date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
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

    @Override
    public String toString() {
        return id + "\t" + date + "\t" + description + "\t$" + amount;
    }
}
