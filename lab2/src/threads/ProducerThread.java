package threads;

import domain.Vector;

public class ProducerThread extends Thread {
    private final int length;
    private final Vector vector1;
    private final Vector vector2;
    private ProducerConsumerBuffer buffer;

    public ProducerThread(Vector vector1, Vector vector2, ProducerConsumerBuffer buffer) {
        super("Producer");
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.length = vector1.getLength();
        this.buffer = buffer;
    }

    public int getLength() {
        return length;
    }

    public Vector getVector1() {
        return vector1;
    }

    public Vector getVector2() {
        return vector2;
    }

    @Override
    public void run() {
        for (int i = 0; i < length; i++) {
            try {
                System.out.printf("Producer: Sending %d * %d = %d\n", vector1.get(i), vector2.get(i), vector1.get(i) * vector2.get(i));
                buffer.put(vector1.get(i) * vector2.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
