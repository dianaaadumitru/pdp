package utils;

import exception.MatrixException;
import model.Matrix;
import tasks.MatrixTask;
import tasks.RowTask;

public class Utils {
    public static int buildElement(Matrix a, Matrix b, int i, int j) throws MatrixException {
        if (i < a.getN() && j < b.getM()) {
            int element = 0;
            for (int k = 0; k < a.getM(); k++) {
                element += a.getMatrixValue(i, k) * b.getMatrixValue(k, j);
            }
            return element;
        } else
            throw new MatrixException("Row/column out of bounds!");
    }

    public static MatrixTask initRowThread(int index, Matrix a, Matrix b, Matrix c, int noThreads) {
        int resultSize = c.getN() * c.getM();
        int count = resultSize / noThreads;

        int iStart = count * index / c.getN();
        int jStart = count * index % c.getN();

        if (index == noThreads - 1)
            count += resultSize % noThreads;

        return new RowTask(a, b, c, iStart, jStart, count);
    }

}
