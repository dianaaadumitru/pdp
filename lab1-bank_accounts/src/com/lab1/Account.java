package com.lab1;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private int accountId;
    private int balance;
    private int initialBalance;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private Logs logs;

    private Lock mutex;

    public Account(int balance) {
        this.accountId = atomicInteger.getAndIncrement();
        this.balance = balance;
        this.initialBalance = balance;
        this.logs = new Logs();
        this.mutex = new ReentrantLock();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    public static AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public static void setAtomicInteger(AtomicInteger atomicInteger) {
        Account.atomicInteger = atomicInteger;
    }

    public Logs getLogs() {
        return logs;
    }

    public void setLogs(Logs logs) {
        this.logs = logs;
    }

    public Lock getMutex() {
        return mutex;
    }

    public void setMutex(Lock mutex) {
        this.mutex = mutex;
    }

    public boolean makeTransfer(Account receiver, int sum) {
        if (sum > balance) {
            return false;
        }
        // when a transfer is being made, account mutexes are locked
        // the order of locks matters bc if both accounts want to make transfers to each others at the same time
        // deadlocks will appear
        if (this.accountId < receiver.accountId) {
            this.mutex.lock();
            receiver.mutex.lock();
        } else {
            receiver.mutex.lock();
            this.mutex.lock();
        }

        balance -= sum;
        receiver.balance += sum;
        logTransfer(OperationType.SEND, this.accountId, receiver.accountId, sum);
        receiver.logTransfer(OperationType.RECEIVE, receiver.accountId, this.accountId, sum);

        // after their balance is changed they are unlocked
        this.mutex.unlock();
        receiver.mutex.unlock();

        return true;
    }


    public void logTransfer(OperationType type, int senderId, int receiverId, int sum) {
        logs.log(type, senderId, receiverId, sum);
    }

    public boolean check() {
//        System.out.println("init balance: " + initialBalance);
        int initBalance = this.initialBalance;
        for (Operation operation : this.logs.getOperations()) {
            if (operation.getOperationType() == OperationType.SEND)
                initBalance -= operation.getAmount();
            else
                initBalance += operation.getAmount();
        }
//        System.out.println("actual balance: " + initBalance);
//        System.out.println("expected balance: " + balance + "\n");
        return initBalance == this.balance;
    }
}
