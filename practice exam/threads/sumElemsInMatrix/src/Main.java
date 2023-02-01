import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[][] matrix = {{1, 2, 3, 4, 10}, {5, 6, 7, 8, 10}, {9, 10, 11, 12,10}, {13, 14, 15, 16,10}};
//        Matrix result = new Matrix();
//        System.out.println(result.computeSum(matrix, 0, matrix.length));
        Matrix2 matrix2 = new Matrix2();
        System.out.println(matrix2.sumMatrix(4,5,matrix, 2));

        Matrix3 matrix3 = new Matrix3();
        System.out.println(matrix3.sumMatrix(4,5,matrix, 2));
    }
}
