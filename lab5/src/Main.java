import model.KaratsubaOperation;
import model.Polynomial;
import model.RegularOnOperation;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Polynomial x = new Polynomial(List.of(5, 0, 10, 6));
        Polynomial y = new Polynomial(List.of(1, 2, 4));

//        Polynomial x = new Polynomial(List.of(10, 6));
//        Polynomial y = new Polynomial(List.of(4));
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        RegularOnOperation regularOnOperation = new RegularOnOperation();
        System.out.println("regular O(n^2) operation:");
        System.out.println("sequential: " + regularOnOperation.sequentialForm(x, y));
        System.out.println("pool thread: " + regularOnOperation.parallelizedForm(x, y));

        KaratsubaOperation karatsubaOperation = new KaratsubaOperation();
        System.out.println("karatsuba operation:");
        System.out.println("sequential: " + karatsubaOperation.sequentialForm(x, y));
        System.out.println("pool thread: " + karatsubaOperation.parallelizedForm(x, y));

        System.out.println("---------------------------------------------");
        System.out.println("sum: " + karatsubaOperation.addPolynomials(x, y));
        System.out.println("subtract: " + karatsubaOperation.subtractPolynomials(x, y));
    }
}
