package tasks;

import model.Matrix;
import model.Pair;

public class ColumnTask extends MatrixTask {
    public ColumnTask(Matrix a, Matrix b, Matrix result, int iStart, int jStart, int sizeOfTask) {
        super(a, b, result, iStart, jStart, sizeOfTask);
    }

    @Override
    public void computeElements() {
        int i = iStart;
        int j = jStart;
        int size = sizeOfTask;

        while (size > 0 && i < result.getN() && j < result.getM()) {
            pairs.add(new Pair<>(i, j));
            i++;
            size--;
            if (i == result.getM()) {
                i = 0;
                j++;
            }
        }
    }
}
