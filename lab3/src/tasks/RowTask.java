package tasks;

import model.Matrix;
import model.Pair;

public class RowTask extends MatrixTask {
    public RowTask(Matrix a, Matrix b, Matrix result, int iStart, int jStart, int sizeOfResult) {
        super(a, b, result, iStart, jStart, sizeOfResult);
    }

    @Override
    public void computeElements() {
        int i = iStart;
        int j = jStart;
        int size = sizeOfTask;

        while (size > 0 && i < result.getN() && j < result.getM()) {
            pairs.add(new Pair<>(i, j));
            j++;
            size--;
            if (j == result.getN()) {
                j = 0;
                i++;
            }
        }
    }
}
