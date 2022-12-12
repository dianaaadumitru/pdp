package threads;

import model.Matrix;
import utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolThread {
    public static void run(Matrix a, Matrix b, Matrix c, int noThreads, String threadsType) {
        ExecutorService service = Executors.newFixedThreadPool(noThreads);
        switch (threadsType) {
            case "row":
                for (int i = 0; i < noThreads; i++) {
                    service.submit(Utils.initRowThread(i, a, b, c, noThreads));
                }
                break;

            case "column":
                for (int i = 0; i < noThreads; i++) {
                    service.submit(Utils.initColumnThread(i, a, b, c, noThreads));
                }
                break;

            case "kThread":
                for (int i = 0; i < noThreads; i++) {
                    service.submit(Utils.initKThread(i, a, b, c, noThreads));
                }
                break;
        }

        service.shutdown();
        try {
            if (!service.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
//            System.out.println("result:\n" + c.toString());
        } catch (InterruptedException ex) {
            service.shutdownNow();
        }
    }
}
