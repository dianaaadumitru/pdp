package com.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private final List<Account> accounts;
    private final int nrThreads;
    private final int nrAccounts;
    private int nrOperations;
    private final int operationsPerThread;

    public Lock mutex = new ReentrantLock();

    private boolean check = false;

    public Bank(int nrThreads, int nrAccounts, int nrOperations) {
        this.accounts = new ArrayList<>();
        this.nrThreads = nrThreads;
        this.nrAccounts = nrAccounts;
        this.nrOperations = nrOperations;
        this.operationsPerThread = nrOperations / nrThreads;
    }

    public void run() {
        createAccounts();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < nrThreads; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                Random r = new Random();
                for (int j = 0; j < operationsPerThread; j++) {
                    int senderId = r.nextInt(nrAccounts);
                    int receiverId = r.nextInt(nrAccounts);
                    if (senderId == receiverId) {
                        --j;
                        continue;
                    }

                    int sum = r.nextInt(20);
                    accounts.get(senderId).makeTransfer(accounts.get(receiverId), sum);

//                    System.out.println("Thread[" + finalI + "]: " + sum + " were transferred from account " +
//                            senderId + " to account " + receiverId);
                }
            }));

        }

        threads.forEach(Thread::start);

        // is done separately to not interfere with the other transactions
        Thread checker = new Thread(() -> {
            mutex.lock();
            while (!check) {
                mutex.unlock();
                Random r = new Random();
                if (r.nextInt(9) == 0) {
                    checkAccounts();
                }
                mutex.lock();
            }
            mutex.unlock();
        });

        checker.start();
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        mutex.lock();
        check = true;
        mutex.unlock();
        try {
            checker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mutex.lock();
        checkAccounts();
        mutex.unlock();
    }

    public void createAccounts() {
        for (int i = 0; i < nrAccounts; ++i) {
            accounts.add(new Account(200));
        }
    }

    private void checkAccounts() {
        System.out.println("Started checking logs");
        AtomicInteger failedAccounts = new AtomicInteger(0);
        accounts.forEach(account -> {
            // check the balance of each account
            account.getMutex().lock();
            if (!account.check()) {
                failedAccounts.getAndIncrement();
            }
            account.getMutex().unlock();
        });

        if (failedAccounts.get() > 0) {
            throw new RuntimeException("Accounts are no longer correct and consistent");
        }
        System.out.println("Ended checking logs");
    }

    @Override
    public String toString() {
        return "Bank{" +
                "accounts=" + accounts +
                '}';
    }
}
