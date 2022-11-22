package com.lab1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter number of threads: ");
        int nrThreads = scan.nextInt();
        System.out.print("Enter number of accounts: ");
        int nrAccounts = scan.nextInt();
        System.out.print("Enter number of operations: ");
        int nrOperations = scan.nextInt();

        Bank bank = new Bank(nrThreads, nrAccounts, nrOperations);
        bank.run();
    }
}
