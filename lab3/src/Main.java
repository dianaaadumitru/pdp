import model.Matrix;
import threads.NormalThread;


public class Main {

    private static int n1 = 3;
    private static int m1 = 3;
    private static int n2 = 3;
    private static int m2 = 3;

    private static int NO_THREADS = 4;
    private static String APPROACH = "normal";
    private static String FUNCTION = "row";

    public static void main(String[] args) {
        Matrix a = new Matrix(n1, m1);
        Matrix b = new Matrix(n2, m2);

        a.populateMatrix();
        b.populateMatrix();

        System.out.println(a);
        System.out.println(b);

        if (a.getM() == b.getN()) {
            Matrix result = new Matrix(a.getN(), b.getM());
            if (APPROACH.equals("normal")) {
                NormalThread.run(a, b, result, NO_THREADS, FUNCTION);
            }
        } else {
            System.err.println("You cannot multiply these matrices!");
        }
    }
}
