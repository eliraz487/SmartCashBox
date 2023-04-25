package org.example;

public enum CashFunction {
    Buy("buy"),
    Pay("Pay");

    private String name;
    CashFunction(String name) {
        this.name= name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
