import model.KaratsubaOperation;
import model.Polynomial;
import model.RegularOnOperation;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Polynomial x = new Polynomial(List.of(5, 0, 10, 6));
//        Polynomial y = new Polynomial(List.of(1, 2, 4));

        Polynomial x = new Polynomial(10);
        Polynomial y = new Polynomial(12);
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("---------------------------------------------");

        RegularOnOperation regularOnOperation = new RegularOnOperation();
        System.out.println("regular O(n^2) operation:");

        long startTime1 = System.currentTimeMillis();
        Polynomial p1 = regularOnOperation.sequentialForm(x, y);
        long endTime1 = System.currentTimeMillis();
        System.out.println("sequential: " + p1);
        System.out.println("Execution time: " + (endTime1 - startTime1) + " ms");

        long startTime2 = System.currentTimeMillis();
        Polynomial p2 = regularOnOperation.parallelizedForm(x, y);
        long endTime2 = System.currentTimeMillis();
        System.out.println("pool thread: " + p2);
        System.out.println("Execution time: " + (endTime2 - startTime2) + " ms");

        KaratsubaOperation karatsubaOperation = new KaratsubaOperation();
        System.out.println("---------------------------------------------");
        System.out.println("karatsuba operation:");

        long startTime3 = System.currentTimeMillis();
        Polynomial p3 = karatsubaOperation.sequentialForm(x, y);
        long endTime3 = System.currentTimeMillis();
        System.out.println("sequential: " + p3);
        System.out.println("Execution time: " + (endTime3 - startTime3) + " ms");

        long startTime4 = System.currentTimeMillis();
        Polynomial p4 = karatsubaOperation.parallelizedForm(x, y, 1);
        long endTime4 = System.currentTimeMillis();
        System.out.println("pool thread: " + p4);
        System.out.println("Execution time: " + (endTime4 - startTime4) + " ms");

//        System.out.println("---------------------------------------------");
//        System.out.println("sum: " + karatsubaOperation.addPolynomials(x, y));
//        System.out.println("subtract: " + karatsubaOperation.subtractPolynomials(x, y));
    }
}
