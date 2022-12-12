package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RegularOnOperation {
    public static final int NR_THREADS = 5;
    public Polynomial sequentialForm(Polynomial x, Polynomial y) {
        int sizeOfResult = x.getDegree() + y.getDegree() + 1;
        List<Integer> coefficients = new ArrayList<>();

        for (int i = 0; i < sizeOfResult; i++) {
            coefficients.add(0);
        }

        for (int i = 0; i < x.getCoefficients().size(); i++) {
            for (int j = 0; j < y.getCoefficients().size(); j++) {
                int index = i + j;
                int value = x.getCoefficients().get(i) * y.getCoefficients().get(j);
                coefficients.set(index, coefficients.get(index) + value);
            }
        }
        return new Polynomial(coefficients);
    }

    public Polynomial parallelizedForm(Polynomial x, Polynomial y) {
        int sizeOfResult = x.getDegree() + y.getDegree() + 1;
        List<Integer> coefficients = new ArrayList<>();

        for (int i = 0; i < sizeOfResult; i++) {
            coefficients.add(0);
        }

        Polynomial result = new Polynomial(coefficients);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NR_THREADS);
        int step = result.getLength() / NR_THREADS;
        if (step == 0) {
            step = 1;
        }

        int end;
        for (int i = 0; i < sizeOfResult; i += step) {
            end = i + step;
            Task task = new Task(x, y, result, i, end);
            executor.execute(task);
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }

        return result;
    }
}
