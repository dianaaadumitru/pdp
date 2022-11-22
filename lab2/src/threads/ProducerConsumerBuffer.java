package threads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ProducerConsumerBuffer {
    private static final int CAPACITY = 1;
    private final Queue<Integer> queue = new LinkedList();

    private final Lock mutex = new ReentrantLock();
    private final Condition notFull = mutex.newCondition();
    private final Condition notEmpty = mutex.newCondition();

    public void put(int val) throws InterruptedException {
        mutex.lock();
        try {
            while (queue.size() == CAPACITY) {
                System.out.println(Thread.currentThread().getName()
                        + ": Queue is full, waiting...");

                // block until the space becomes available
                notFull.await();
            }

            queue.add(val);
            System.out.printf("%s added %d into the queue %n", Thread
                    .currentThread().getName(), val);

            // notify that item is available
            notEmpty.signal();

        } finally {
            mutex.unlock();
        }
    }

    public int get() throws InterruptedException {
        mutex.lock();
        try {
            while (queue.size() == 0) {
                System.out.println(Thread.currentThread().getName()
                        + ": Buffer is empty, waiting");

                // block until the item becomes available
                notEmpty.await();
            }

            Integer value = queue.poll();
            if (value != null) {
                System.out.printf("%s consumed %d from the queue %n", Thread
                        .currentThread().getName(), value);

                // notify that space is available
                notFull.signal();
            }
            return value;
        } finally {
            mutex.unlock();
        }
    }
}