import model.KaratsubaOperation;
import model.Operation;
import model.Polynomial;
import model.RegularOnOperation;
import mpi.MPI;

public class Main {
    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            System.out.println("Need at least 2 MPI tasks. Quitting...");
            return;
        }

        if (me == 0) {
            // master process
            System.out.println("Generating polynomials: ");
            Polynomial x = new Polynomial(100);
            Polynomial y = new Polynomial(100);

            System.out.println("x: " + x);
            System.out.println("y: " + y);

            multiplicationMaster(x, y, size);
        } else {
//            multiplyRegular(me);
            multiplyKaratsubaWorker(me);
        }
        MPI.Finalize();
    }

    private static void multiplicationMaster(Polynomial x, Polynomial y, int size) {
        long startTime = System.currentTimeMillis();
        int start = 0, finish = 0;
        int len = x.getLength() / (size - 1);

        for (int i = 1; i < size; i++) {
            start = finish;
            finish += len;
            if (i == size - 1) {
                finish = x.getLength();
            }
            MPI.COMM_WORLD.Send(new Object[]{x}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{y}, 0, 1, MPI.OBJECT, i, 0);

            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{finish}, 0, 1, MPI.INT, i, 0);
        }

        Object[] results = new Object[size - 1];
        for (int i = 1; i < size; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }

        Polynomial result = Operation.buildResult(results);
        long endTime = System.currentTimeMillis();
        System.out.println("multiplication of polynomials:\n" + result);
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    private static void multiplyRegular(int me) {
        System.out.printf("Worker %d started\n", me);

        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(q, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial result = RegularOnOperation.sequentialForm(p[0], q[0], begin[0], end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);

    }

    private static void multiplyKaratsubaWorker(int me) {
        System.out.printf("Worker %d started\n", me);

        Object[] x = new Object[2];
        Object[] y = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(x, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(y, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial p = (Polynomial) x[0];
        Polynomial q = (Polynomial) y[0];

        for (int i = 0; i < begin[0]; i++) {
            p.getCoefficients().set(i, 0);
        }
        for (int j = end[0]; j < p.getCoefficients().size(); j++) {
            p.getCoefficients().set(j, 0);

        }

        Polynomial result = KaratsubaOperation.sequentialForm(p, q);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }
}