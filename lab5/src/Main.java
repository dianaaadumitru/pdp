import model.Polynomial;
import model.RegularOnOperation;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Polynomial x = new Polynomial(List.of(5, 0, 10, 6));
        Polynomial y = new Polynomial(List.of(1, 2, 4));
        System.out.println(x);
        System.out.println(y);
        RegularOnOperation regularOnOperation = new RegularOnOperation();
        System.out.println(regularOnOperation.sequentialForm(x, y));
        System.out.println(regularOnOperation.parallelizedForm(x, y));
    }
}
