import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> a = List.of(1, 2, 3, 4, 5, 1);
        List<Integer> b = List.of(6, 7, 8, 9, 10, 1);

        ScalarProduct scalarProduct = new ScalarProduct();
        System.out.println(scalarProduct.computeProduct(a, b, 4));

        ScalarProduct2 scalarProduct2 = new ScalarProduct2();
        System.out.println(scalarProduct2.computeProduct(a, b, 4));

    }
}
