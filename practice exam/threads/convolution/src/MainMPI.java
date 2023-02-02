import mpi.MPI;

import java.util.ArrayList;
import java.util.Collection;
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
            List<Integer> a = List.of(1, 2, 3);
            List<Integer> b = List.of(1, 2, 3);
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

        Object[] results = new Object[noProcesses - 1];
        for (int i = 1; i < noProcesses; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }

        List<Integer> finalList = new ArrayList<>();


        for (Object result : results) {
            finalList.addAll((Collection<? extends Integer>) result);
        }

        System.out.println(finalList);
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

        List<Integer> result = new ArrayList<>();

        for (int j = start[0]; j < end[0]; j++) {
            int sum = 0;
            for (int k = 0; k < a.size(); k++) {
                sum += a.get(j) * b.get((j - k + a.size()) % a.size());
            }
            result.add(sum);
        }

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }
}
