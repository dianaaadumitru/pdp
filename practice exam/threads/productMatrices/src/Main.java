import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        int[][] a = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        int[][] b = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        Matrix matrix = new Matrix();
        System.out.println(Arrays.deepToString(matrix.computeProduct(4, 4, a, 4, 4, b, 3)));
    }
}
