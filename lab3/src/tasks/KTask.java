package tasks;

import model.Matrix;
import model.Pair;

public class KTask extends MatrixTask {
    public KTask(Matrix a, Matrix b, Matrix result, int iStart, int jStart, int sizeOfTask, int k) {
        super(a, b, result, iStart, jStart, sizeOfTask, k);
    }

    @Override
    public void computeElements() {
        int i = iStart;
        int j = jStart;
        int size = sizeOfTask;

        while (size > 0 && i < result.getN()) {
            pairs.add(new Pair<>(i, j));
            size--;
            i += (j + k) / result.getM();
            j = (j + k ) % result.getM();
        }
    }
}
