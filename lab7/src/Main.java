import jdk.dynalink.Operation;
import model.Polynomial;
import model.RegularOnOperation;
import mpi.*;

public class Main {
    private static final String MULTIPLICATION = "Simple";

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        System.out.println("Hello world from <"+me+"> of <"+size+">");
        MPI.Finalize();

        if (size < 2) {
            System.out.println("Need at least 2 MPI tasks. Quitting...\n");
        } else {
            if (me == 0) {
                // master process
                System.out.println("Master process generating polynomials:");
                Polynomial p = new Polynomial(5);
                Polynomial q = new Polynomial(5);

                System.out.println(p);
                System.out.println(q);

                multiplicationMaster(p, q, size, MULTIPLICATION);

            } else {
                if ("Karatsuba".compareTo(MULTIPLICATION) == 0){
//                    multiplyKaratsubaWorker(me);
                }
                else{
                    multiplySimpleWorker(me);
                }
            }
        }
    }

    private static void multiplicationMaster(Polynomial p, Polynomial q, int nrProcs, String type) {
        long startTime = System.currentTimeMillis();
        int start = 0, finish = 0;
        int len = p.getLength() / (nrProcs - 1);

        for (int i = 1; i < nrProcs; i++) {
            start = finish;
            finish += len;
            if (i == nrProcs - 1) {
                finish = p.getLength();
            }
            MPI.COMM_WORLD.Send(new Object[]{p}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{q}, 0, 1, MPI.OBJECT, i, 0);

            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{finish}, 0, 1, MPI.INT, i, 0);

        }

        Object[] results = new Object[nrProcs - 1];
        for (int i = 1; i < nrProcs; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }

        Polynomial result = RegularOnOperation.buildResult(results);
        long endTime = System.currentTimeMillis();
        System.out.println(type + " multiplication of polynomials:\n" + result.toString());
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    private static void multiplySimpleWorker(int me) {
        System.out.printf("Worker %d started\n", me);

        Object[] p = new Object[2];
        Object[] q = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(q, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial result = RegularOnOperation.multiplySimple(p[0], q[0], begin[0], end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);

    }
}
