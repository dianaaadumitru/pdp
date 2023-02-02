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
            int[][] matrix = {{1, 2, 3, 4, 10}, {5, 6, 7, 8, 10}, {9, 10, 11, 12,10}, {13, 14, 15, 16,10}};
            masterProcess(4, 5, matrix, size);
        } else {
            workerProcess(rank);
        }

        MPI.Finalize();
    }

    public static void masterProcess(int noRows, int noColumns, int[][] matrix, int noProcesses) {
        int start = 0, finish = 0;
        int len = noRows / (noProcesses - 1);

        for (int i = 1; i < noProcesses; i++) {
            start = finish;
            finish += len;

            if (i == noProcesses - 1) {
                finish = noRows;
            }

            MPI.COMM_WORLD.Send(new Object[]{matrix}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{noColumns}, 0, 1, MPI.INT, i, 0);

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

        Object[] m = new Object[1];
        int[] columns = new int[1];

        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(m, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(columns, 0, 1, MPI.INT, 0, 0);

        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        int[][] matrix = (int[][]) m[0];

        int sum = 0;
        for (int i = start[0]; i < end[0]; i++) {
            for (int j = 0; j < columns[0]; j++) {
                sum += matrix[i][j];
            }
        }
        MPI.COMM_WORLD.Send(new int[]{sum}, 0, 1, MPI.INT, 0, 0);

    }
}
