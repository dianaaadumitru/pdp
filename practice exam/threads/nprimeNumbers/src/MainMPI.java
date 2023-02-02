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
            List<Integer> firstPrimes = List.of(2, 3, 5, 7);
            masterProcess(firstPrimes, 50, size);
        } else {
            workerProcess(rank);
        }


        MPI.Finalize();
    }

    private static void masterProcess(List<Integer> firstPrimes, int n, int noProcesses) {
        int start = 0, finish = 0;
        int len = (n - (int) (Math.sqrt(n))) / (noProcesses - 1);
        for (int i = 1; i < noProcesses; i++) {
            start = finish;
            finish += len;

            if (i == noProcesses - 1) {
                finish = n - (int) (Math.sqrt(n));
            }
            MPI.COMM_WORLD.Send(new Object[]{firstPrimes}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{n}, 0, 1, MPI.INT, i, 0);

            MPI.COMM_WORLD.Send(new int[]{start}, 0, 1, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new int[]{finish}, 0, 1, MPI.INT, i, 0);
        }

        Object[] results = new Object[noProcesses - 1];
//        List<List<Integer>> results = new ArrayList<>();
        for (int i = 1; i < noProcesses; i++) {
            MPI.COMM_WORLD.Recv(results, i - 1, 1, MPI.OBJECT, i, 0);
        }
        List<Integer> finalResults = new ArrayList<>();
        for (Object result : results) {
            finalResults.addAll((Collection<? extends Integer>) result);
        }

        System.out.println(finalResults);
    }

    private static void workerProcess(int rank) {
        System.out.println("Hello from rank " + rank);
        Object[] primes = new Object[1];
        int[] n = new int[1];

        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(primes, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(n, 0, 1, MPI.INT, 0, 0);


        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        List<Integer> firstPrimes = (List<Integer>) primes[0];

        List<Integer> finalPrimes = new ArrayList<>();
        for (int i = start[0]; i < end[0]; i++) {
            if (isDivisible(firstPrimes, (int) (i + Math.sqrt(n[0])))) {
                finalPrimes.add((int) (i + Math.sqrt(n[0])));
            }
        }

        MPI.COMM_WORLD.Send(new Object[]{finalPrimes}, 0, 1, MPI.OBJECT, 0, 0);


    }

    private static boolean isDivisible(List<Integer> firstPrimes, int n) {
        for (int prime: firstPrimes) {
            if (n % prime == 0)
                return false;
        }
        return true;
    }
}
