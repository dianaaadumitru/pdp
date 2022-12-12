package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Polynomial {
    private final List<Integer> coefficients;
    private final int degree;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
        this.degree = coefficients.size() - 1;
    }

    public Polynomial(int degree) {
        this.degree = degree;
        coefficients = new ArrayList<>(degree + 1);
        generateCoefficients();
    }

    public int getLength() {
        return this.coefficients.size();
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    public int getDegree() {
        return degree;
    }

    private void generateCoefficients() {
        Random r = new Random();
        int MAX_VALUE = 10;
        for (int i = 0; i < degree; i++) {
            coefficients.add(r.nextInt(MAX_VALUE));
        }
        coefficients.add(r.nextInt(MAX_VALUE) + 1);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int power = 0;
        for (int i = 0; i <= this.degree; i++) {
            str.append(" ").append(coefficients.get(i)).append("x^").append(power).append(" +");
            power++;
        }
        str.deleteCharAt(str.length() - 1); //delete last +
        return str.toString();
    }
}
