import mpi.MPI;

import java.util.List;

public class MainMPI {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            System.out.println("problem");
            return;
        }

        if (rank == 0) {
            System.out.println("master");
            List<Integer> a = List.of(1, 2, 3, 4, 5);
            List<Integer> b = List.of(6, 7, 8, 9, 10);
            masterProcess(a, b, size);
        } else {
            workerProcess(rank);
        }

        MPI.Finalize();
    }

    public static void masterProcess(List<Integer> a, List<Integer> b, int noProcesses) {
        int start = 0, finish = 0;
        int len = a.size() / (noProcesses - 1);
        for (int i = 1; i < noProcesses; i++) {
            start = finish;
            finish += len;

            if (i == noProcesses - 1) {
                finish = a.size();
            }

            MPI.COMM_WORLD.Send(new Object[]{a}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{b}, 0, 1, MPI.OBJECT, i, 0);

            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{finish}, 0, 1, MPI.INT, i, 0);
        }

        int[] results = new int[noProcesses - 1];
        for (int i = 1; i < noProcesses; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.INT, i, 0);
        }
        int sum = 0;
        for (int i = 0; i < results.length; i++) {
            sum += results[i];
        }

        System.out.println("sum= " + sum);
    }

    public static void workerProcess(int rank) {
        System.out.println("Hello from rank " + rank);

        Object[] aa = new Object[1];
        Object[] bb = new Object[1];

        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(aa, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(bb, 0, 1, MPI.OBJECT, 0, 0);

        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        List<Integer> a = (List<Integer>) aa[0];
        List<Integer> b = (List<Integer>) bb[0];

        int sum = 0;
        for (int i = start[0]; i < end[0]; i++) {
            sum += a.get(i) * b.get(i);
        }

        MPI.COMM_WORLD.Send(new int[]{sum}, 0, 1, MPI.INT, 0, 0);
    }
}
