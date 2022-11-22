import model.Matrix;
import threads.NormalThread;


public class Main {

    private static int n1 = 10;
    private static int m1 = 10;
    private static int n2 = 10;
    private static int m2 = 10;

    private static int NO_THREADS = 4;
    private static String APPROACH = "normal";
    private static String FUNCTION = "kThread"; // row column kThread

    public static void main(String[] args) {
        Matrix a = new Matrix(n1, m1);
        Matrix b = new Matrix(n2, m2);

        Matrix test1 = new Matrix(3, 3);
        Matrix test2 = new Matrix(3, 3);

        test1.setMatrixValue(0,0,1);
        test1.setMatrixValue(0,1,2);
        test1.setMatrixValue(0,2,3);
        test1.setMatrixValue(1,0,4);
        test1.setMatrixValue(1,1,5);
        test1.setMatrixValue(1,2,6);
        test1.setMatrixValue(2,0,7);
        test1.setMatrixValue(2,1,8);
        test1.setMatrixValue(2,2,9);

        test2.setMatrixValue(0,0,9);
        test2.setMatrixValue(0,1,8);
        test2.setMatrixValue(0,2,7);
        test2.setMatrixValue(1,0,6);
        test2.setMatrixValue(1,1,5);
        test2.setMatrixValue(1,2,4);
        test2.setMatrixValue(2,0,3);
        test2.setMatrixValue(2,1,2);
        test2.setMatrixValue(2,2,1);



        a.populateMatrix();
        b.populateMatrix();

//        System.out.println(a);
//        System.out.println(b);
        System.out.println(test1);
        System.out.println(test2);

        if (test1.getM() == test2.getN()) {
            Matrix result = new Matrix(test1.getN(), test2.getM());
            float start = System.nanoTime() / 1_000_000;
            if (APPROACH.equals("normal")) {
                NormalThread.run(test1, test2, result, NO_THREADS, FUNCTION);
            }

            float end = System.nanoTime() / 1_000_000;

            System.out.println("Time elapsed: " + (end-start)/1000 + " seconds");
        } else {
            System.err.println("You cannot multiply these matrices!");
        }
    }
}
