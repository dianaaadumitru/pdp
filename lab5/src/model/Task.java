package model;

public class Task implements Runnable {

    private final Polynomial x;
    private final Polynomial y;
    private final Polynomial result;
    private final int start;
    private final int end;

    public Task(Polynomial x, Polynomial y, Polynomial result, int start, int end) {
        this.x = x;
        this.y = y;
        this.result = result;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            // if there are no more elements
            if (i > result.getLength()) {
                return;
            }

            for (int j = 0; j <= i; j++) {
                if (j < x.getLength() && (i - j) < y.getLength()) {
                    int value = x.getCoefficients().get(j) * y.getCoefficients().get(i - j);
                    result.getCoefficients().set(i, result.getCoefficients().get(i) + value);
                }
            }
        }
    }
}
