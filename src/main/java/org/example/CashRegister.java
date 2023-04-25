package org.example;

import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.YouAlreadyPayException;
import org.example.exceptions.NotEnoughMoneyInCashException;

import java.util.*;

public class CashRegister {
    private double purchaseAmount=0;


    private boolean purchased=false;
    private Map<Coin, Integer> excess;
    private double payment;
    private static Map<Coin, Integer> money_in_cash;//how many coin in the cash

    private static PriorityQueue<Coin> maxHeap;

    private static final int start_count = 0;


    public CashRegister() {
        money_in_cash = new HashMap<>();
        for (Coin c : Coin.values()) {
            money_in_cash.put(c, start_count);
        }
    }

    public void startPurchase(double purchaseAmount) {
        excess = null;
        purchased =false;
        this.purchaseAmount = purchaseAmount;
        payment = 0;
    }


    public synchronized boolean receivePayment(Coin coinType, int count) throws IllegalArgumentException, YouAlreadyPayException {
        boolean haveenoughmoney =false;
        if (excess != null) {
           throw new YouAlreadyPayException();
        }
        if (count <= 0) {
            throw new IllegalArgumentException("the count of the number can be zero or less");
        }
        addCoinsTo(money_in_cash, coinType, count);
        payment =(coinType.getValue()*count)+payment;
        payment= Math.round(payment*100.0)/100.0;
        if (purchaseAmount<=payment)
        {
            haveenoughmoney=true;
            return haveenoughmoney;
        }
        return haveenoughmoney;
    }

    public synchronized void addCoinsTo(Map<Coin, Integer> map, Coin coinType, int count) throws IllegalArgumentException {
        if (map == null) {
            throw new IllegalArgumentException("the map is not init");
        }
        if (map.containsKey(coinType)) {
            int currentCount = map.get(coinType);
            int newCount = currentCount + count;
            map.replace(coinType, currentCount, newCount);
            return;
        }
        map.put(coinType, count);
    }


    public synchronized Map<Coin, Integer> calculateExcess() throws NotEnoughMoneyInCashException, NotEnoughMoneyException {
        if (excess != null) {
            return excess;
        }
        if (purchased){
            return calculateExcess(payment);
        }
        double payment;
        payment = this.payment;
        if (purchaseAmount > payment) {
           throw new NotEnoughMoneyException(purchaseAmount-payment);
        }
        if (purchaseAmount== payment){
            purchased =true;
            return null;
        }
        Map<Coin, Integer> excessCoins = calculateExcess(payment);
        excess = excessCoins;
        purchased =true;
        return excessCoins;
    }

    private synchronized Map<Coin, Integer> calculateExcess(double money) throws NotEnoughMoneyInCashException {
        Map<Coin, Integer> excessCoins = new HashMap<>();
        double excess = Math.round((money - purchaseAmount) * 100.0) / 100.0;
        for (Coin coin : getMaxHeapCoin()) {
            while (excess >= coin.getValue()) {
                if (money_in_cash.get(coin) <= 0) {
                    break;
                }
                double temp = excess - coin.getValue();
                excess = Math.round((temp) * 100.0) / 100.0;
                addCoinsTo(money_in_cash, coin, -1);
                addCoinsTo(excessCoins, coin, 1);
            }
        }
        if (excess > 0) {
            for (Map.Entry<Coin,Integer>c:excessCoins.entrySet()){
                addCoinsTo(money_in_cash,c.getKey(),c.getValue());
            }
            this.excess=null;
            throw new NotEnoughMoneyInCashException(excess);
        }
        return excessCoins;
    }




    private
    PriorityQueue<Coin> getMaxHeapCoin() {
        if (maxHeap != null) {
            return maxHeap;
        }
        maxHeap = new PriorityQueue<>(Comparator.comparing(Coin::getValue).reversed());
        Collections.addAll(maxHeap, Coin.values());
        return maxHeap;
    }

    public boolean ispPurchaseFinish() {

        return purchased;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public Map<Coin, Integer> getExcess() {
        return excess;
    }

    public void setExcess(Map<Coin, Integer> excess) {
        this.excess = excess;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public static Map<Coin, Integer> getMoney_in_cash() {
        return money_in_cash;
    }

    public static void setMoney_in_cash(Map<Coin, Integer> money_in_cash) {
        CashRegister.money_in_cash = money_in_cash;
    }

    public static PriorityQueue<Coin> getMaxHeap() {
        return maxHeap;
    }

    public static void setMaxHeap(PriorityQueue<Coin> maxHeap) {
        CashRegister.maxHeap = maxHeap;
    }
}
