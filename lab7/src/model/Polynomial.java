package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Polynomial implements Serializable {
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

    public static Polynomial buildEmptyPolynomial(int degree) {
        List<Integer> zeros = IntStream.range(0, degree).mapToObj(i -> 0).collect(Collectors.toList());
        return new Polynomial(zeros);
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
//            coefficients.add(r.nextInt(MAX_VALUE));
            coefficients.add(1);

        }
        coefficients.add(1);
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
