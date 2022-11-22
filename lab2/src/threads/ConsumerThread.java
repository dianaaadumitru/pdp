package threads;

public class ConsumerThread extends Thread {

    public int result = 0;
    public int length;
    private final ProducerConsumerBuffer buffer;

    public ConsumerThread(int length, ProducerConsumerBuffer buffer) {
        super("Consumer");
        this.length = length;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.length; i++) {
            try {
                result += buffer.get();
                System.out.println("Consumer: Result is now " + result + "\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumer: Final result is " + result + "\n");

    }
}

