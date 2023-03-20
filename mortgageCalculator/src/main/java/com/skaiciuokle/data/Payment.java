package com.skaiciuokle.data;

import javafx.beans.property.SimpleStringProperty;

public class Payment {
    private SimpleStringProperty monthCol;
    private SimpleStringProperty amountCol;
    private SimpleStringProperty percentCol;
    private SimpleStringProperty unpaidCol;

    public Payment(String monthCol, String amountCol, String percentCol, String unpaidCol) {
        this.monthCol = new SimpleStringProperty(monthCol);
        this.amountCol = new SimpleStringProperty(amountCol);
        this.percentCol = new SimpleStringProperty(percentCol);
        this.unpaidCol = new SimpleStringProperty(unpaidCol);
    }

    public String getMonthCol() {
        return monthCol.get();
    }

    public void setMonthCol(String monthCol) {
        this.monthCol.set(monthCol);
    }

    public String getAmountCol() {
        return amountCol.get();
    }

    public void setAmountCol(String amountCol) {
        this.amountCol.set(amountCol);
    }

    public String getPercentCol() {
        return percentCol.get();
    }

    public void setPercentCol(String percentCol) {
        this.percentCol.set(percentCol);
    }

    public String getUnpaidCol() {
        return unpaidCol.get();
    }

    public void setUnpaidCol(String unpaidCol) {
        this.unpaidCol.set(unpaidCol);
    }
}
