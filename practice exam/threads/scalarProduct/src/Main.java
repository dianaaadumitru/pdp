import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> a = List.of(1, 2, 3, 4, 5);
        List<Integer> b = List.of(6, 7, 8, 9, 10);

        ScalarProduct scalarProduct = new ScalarProduct();
        System.out.println(scalarProduct.computeProduct(a, b, 4));
    }
}
