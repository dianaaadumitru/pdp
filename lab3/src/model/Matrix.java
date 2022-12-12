package model;

import java.util.Random;

public class Matrix {
    private final int n;
    private final int m;
    private final int[][] matrix;

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        this.matrix = new int[n][m];
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public void populateMatrix() {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = 1;
            }
        }
    }

    public int getMatrixValue(int n, int m) {
        return matrix[n][m];
    }

    public void setMatrixValue(int n, int m, int value) {
        matrix[n][m] = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
