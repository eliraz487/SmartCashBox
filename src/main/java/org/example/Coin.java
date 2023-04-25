package org.example;

import com.sun.jdi.Value;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public enum Coin {

    PENNY("Penny", 0.01),
    NICKEL("Nickel", 0.05),
    DIME("Dime", 0.10),
    QUARTER("Quarter", 0.25);

    private final String name;
    private final double value;

    Coin(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public double getValue() {
        return this.value;
    }

}
