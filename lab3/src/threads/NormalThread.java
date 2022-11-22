package threads;

import model.Matrix;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NormalThread {
    public static void run(Matrix a, Matrix b, Matrix c, int noThreads, String threadsType) {
        List<Thread> threads = new ArrayList<>();

        switch (threadsType) {
            case "row":
                for (int i = 0; i < noThreads; i++) {
                    threads.add(Utils.initRowThread(i, a, b, c, noThreads));
                }
                break;

            case "column":
                for (int i = 0; i < noThreads; i++) {
                    threads.add(Utils.initColumnThread(i, a, b, c, noThreads));
                }
                break;

            case "kThread":
                for (int i = 0; i < noThreads; i++) {
                    threads.add(Utils.initKThread(i, a, b, c, noThreads));
                }
                break;
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("result:\n" + c);
    }
}
