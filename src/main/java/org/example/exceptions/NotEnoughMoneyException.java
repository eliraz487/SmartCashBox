package org.example.exceptions;

public class NotEnoughMoneyException extends Exception{

    private double less;

    public NotEnoughMoneyException(double less) {
        this.less = less;
    }

    public double getLess() {
        return less;
    }
}
