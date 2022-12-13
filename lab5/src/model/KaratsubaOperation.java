package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KaratsubaOperation {
    public static final int NR_THREADS = 4;
    private static final int MAX_DEPTH = 4;

    public Polynomial sequentialForm(Polynomial x, Polynomial y) {
        if (x.getDegree() < 2 || y.getDegree() < 2) {
            RegularOnOperation regularOnOperation = new RegularOnOperation();
            return regularOnOperation.sequentialForm(x, y);
        }

        // divide polynomials
        int len = Math.max(x.getDegree(), y.getDegree()) / 2;
        Polynomial firstHalfX = new Polynomial(x.getCoefficients().subList(0, len));
        Polynomial secondHalfX = new Polynomial(x.getCoefficients().subList(len, x.getLength()));
        Polynomial firstHalfY = new Polynomial(y.getCoefficients().subList(0, len));
        Polynomial secondHalfY = new Polynomial(y.getCoefficients().subList(len, y.getLength()));

        // step1 (a*c)
        Polynomial p1 = sequentialForm(firstHalfX, firstHalfY);
        // step2 (b*d)
        Polynomial p2 = sequentialForm(secondHalfX, secondHalfY);
        // step3 (a+b)(c+d)
        Polynomial p3 = sequentialForm(addPolynomials(firstHalfX, secondHalfX), addPolynomials(firstHalfY, secondHalfY));

        // add zeros
        Polynomial q1 = addZeros(p2, 2 * len);
        // step4 p3 - p2 - p1 + add zeros
        Polynomial q2 = addZeros(subtractPolynomials(subtractPolynomials(p3, p2), p1), len);
        // step5 final result: p1 + q1 + q2
        return addPolynomials(addPolynomials(q1, q2), p1);
    }

    public Polynomial parallelizedForm(Polynomial x, Polynomial y, int currentDepth) throws ExecutionException, InterruptedException {
        //the idea is that from a point it's not worth to split more
        if (currentDepth > MAX_DEPTH) {
            return sequentialForm(x, y);
        }

        if (x.getDegree() < 2 || y.getDegree() < 2) {
            return sequentialForm(x, y);
        }

        // divide polynomials
        int len = Math.max(x.getDegree(), y.getDegree()) / 2;
        Polynomial firstHalfX = new Polynomial(x.getCoefficients().subList(0, len));
        Polynomial secondHalfX = new Polynomial(x.getCoefficients().subList(len, x.getLength()));
        Polynomial firstHalfY = new Polynomial(y.getCoefficients().subList(0, len));
        Polynomial secondHalfY = new Polynomial(y.getCoefficients().subList(len, y.getLength()));

        ExecutorService executor = Executors.newFixedThreadPool(NR_THREADS);

        // step1 (a*c)
        Future<Polynomial> f1 = executor.submit(() -> parallelizedForm(firstHalfX, firstHalfY, currentDepth + 1));
        // step2 (b*d)
        Future<Polynomial> f2 = executor.submit(() -> parallelizedForm(addPolynomials(firstHalfX, secondHalfX), addPolynomials(firstHalfY, secondHalfY), currentDepth + 1));
        // step3 (a+b)(c+d)
        Future<Polynomial> f3 = executor.submit(() -> parallelizedForm(secondHalfX, secondHalfY, currentDepth + 1));

        executor.shutdown();

        Polynomial p1 = f1.get();
        Polynomial p2 = f2.get();
        Polynomial p3 = f3.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        //add zeros
        Polynomial q1 = addZeros(p3, 2 * len);
        // step4 p3 - p2 - p1 + add zeros
        Polynomial q2 = addZeros(subtractPolynomials(subtractPolynomials(p2, p3), p1), len);
        // step5 final result: p1 + q1 + q2
        return addPolynomials(addPolynomials(q1, q2), p1);
    }

    public Polynomial addPolynomials(Polynomial x, Polynomial y) {
        int minDegree = Math.min(x.getDegree(), y.getDegree());
        int maxDegree = Math.max(x.getDegree(), y.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(x.getCoefficients().get(i) + y.getCoefficients().get(i));
        }

        addRemainingCoefficients(x, y, minDegree, maxDegree, coefficients);

        return new Polynomial(coefficients);
    }

    public Polynomial subtractPolynomials(Polynomial x, Polynomial y) {
        int minDegree = Math.min(x.getDegree(), y.getDegree());
        int maxDegree = Math.max(x.getDegree(), y.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(x.getCoefficients().get(i) - y.getCoefficients().get(i));
        }

        addRemainingCoefficients(x, y, minDegree, maxDegree, coefficients);

        int i = coefficients.size() - 1;
        while (i > 0 && coefficients.get(i) == 0) {
            coefficients.remove(i);
            i--;
        }

        return new Polynomial(coefficients);
    }

    private void addRemainingCoefficients(Polynomial x, Polynomial y, int minDegree, int maxDegree, List<Integer> coefficients) {
        if (minDegree != maxDegree) {
            if (maxDegree == x.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(x.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(y.getCoefficients().get(i));
                }
            }
        }
    }

    public Polynomial addZeros(Polynomial x, int offset) {
        List<Integer> coefficients = IntStream.range(0, offset).mapToObj(i -> 0).collect(Collectors.toList());
        coefficients.addAll(x.getCoefficients());
        return new Polynomial(coefficients);
    }
}
