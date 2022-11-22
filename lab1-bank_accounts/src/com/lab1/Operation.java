package com.lab1;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Operation {
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    private int serialNumber;
    private OperationType operationType;
    private int senderId;
    private int receiverId;
    private int amount;
    private int sum;

    public Operation(OperationType operationType, int senderId, int receiverId, int sum) {
        this.operationType = operationType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sum = sum;
        this.serialNumber = atomicInteger.getAndIncrement();
        this.amount = sum;
    }

    public Operation(OperationType operationType, int senderId, int receiverId, int amount, int sum) {
        this.operationType = operationType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.sum = sum;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return amount == operation.amount && sum == operation.sum && operationType == operation.operationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationType, amount, sum);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "serialNumber=" + serialNumber +
                ", operationType=" + operationType +
                ", amount=" + amount +
                ", sum=" + sum +
                '}';
    }
}
