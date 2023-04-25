package org.example.exceptions;
public class NotEnoughMoneyInCashException extends Exception {
    private double excessAmount;

    public NotEnoughMoneyInCashException(double excessAmount) {
        this.excessAmount = excessAmount;
    }

    public double getExcessAmount() {
        return excessAmount;
    }
}