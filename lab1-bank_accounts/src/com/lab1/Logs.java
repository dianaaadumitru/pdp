package com.lab1;

import java.util.ArrayList;
import java.util.List;

public class Logs {
    private final List<Operation> operations;

    public Logs() {
        this.operations = new ArrayList<>();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void log(OperationType type, int senderId, int receiverId, int sum) {
        operations.add(new Operation(type, senderId, receiverId, sum));
    }
}
