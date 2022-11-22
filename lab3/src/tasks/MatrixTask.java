package tasks;

import exception.MatrixException;
import model.Matrix;
import model.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class MatrixTask extends Thread{
    public List<Pair<Integer, Integer>> pairs;
    public Matrix a;
    public Matrix b;
    public Matrix result;
    public int iStart;
    public int jStart;
    public int sizeOfTask;
    public int k;

    public MatrixTask(Matrix a, Matrix b, Matrix result, int iStart, int jStart, int sizeOfTask) {
        this.pairs = new ArrayList<>();
        this.a = a;
        this.b = b;
        this.result = result;
        this.iStart = iStart;
        this.jStart = jStart;
        this.sizeOfTask = sizeOfTask;
        computeElements();
    }

    public MatrixTask(Matrix a, Matrix b, Matrix result, int iStart, int jStart, int sizeOfTask, int k) {
        this.pairs = new ArrayList<>();
        this.a = a;
        this.b = b;
        this.result = result;
        this.iStart = iStart;
        this.jStart = jStart;
        this.sizeOfTask = sizeOfTask;
        this.k = k;
        computeElements();
    }

    public abstract void computeElements();

    @Override
    public void run() {
        for (Pair<Integer, Integer> p: pairs) {
            int i = p.getFirst();
            int j = p.getSecond();
            try {
                result.setMatrixValue(i, j, Utils.buildElement(a, b, i, j));
            } catch (MatrixException e) {
                e.printStackTrace();
            }
        }
    }
}
