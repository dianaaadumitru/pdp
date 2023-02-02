import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> a = List.of(1, 2, 3);
        List<Integer> b = List.of(1, 2, 3);

        Convolution convolution = new Convolution();
        System.out.println(convolution.computeConvolution(a, b, 4));
    }
}
