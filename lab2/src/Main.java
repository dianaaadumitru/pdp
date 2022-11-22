import domain.Vector;
import threads.ConsumerThread;
import threads.ProducerConsumerBuffer;
import threads.ProducerThread;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        List<Integer> array = IntStream.rangeClosed(1, 100).boxed().toList();
        Vector vec1 = new Vector(array);
        Vector vec2 = new Vector(array);

        ProducerConsumerBuffer buffer = new ProducerConsumerBuffer();

        ProducerThread producer = new ProducerThread(vec1, vec2, buffer);
        ConsumerThread consumer = new ConsumerThread(vec1.getLength(), buffer);

        producer.start();
        consumer.start();
    }
}
